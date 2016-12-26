package edu.nju.wxd.weather.activity;



import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.nju.wxd.weather.R;
import edu.nju.wxd.weather.dao.WeatherDB;
import edu.nju.wxd.weather.model.City;
import edu.nju.wxd.weather.model.County;
import edu.nju.wxd.weather.model.Province;
import edu.nju.wxd.weather.util.HttpCallbackListener;
import edu.nju.wxd.weather.util.HttpUtil;
import edu.nju.wxd.weather.util.ResponseCode;
import edu.nju.wxd.weather.util.ResultData;
import edu.nju.wxd.weather.util.Utility;

public class ChooseAreaActivity extends AppCompatActivity implements View.OnClickListener{

    public static final int LEVEL_PROVINCE=0;

    public static final int LEVEL_CITY=1;

    public static final int LEVEL_COUNTY=2;

    private static final String TAG="ChooseAreaActivity";

    private ProgressDialog progressDialog;

    private TextView titleText;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private WeatherDB weatherDB;

    private List<String> dataList=new ArrayList<>();

    private List<Province> provinceList=new ArrayList<>();

    private List<City> cityList=new ArrayList<>();

    private List<County> countyList=new ArrayList<>();

    private Province selectedProvince;

    private City selectedCity;

    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_area);

        listView=(ListView) findViewById(R.id.list_view);
        titleText=(TextView)findViewById(R.id.title_text);
        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);

        weatherDB=WeatherDB.getInstance(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince=provinceList.get(i);
                    queryCities();
                }else if (currentLevel==LEVEL_CITY){
                    selectedCity=cityList.get(i);
                    queryCounties();
                }
            }
        });

        queryProvinces();

    }


    @Override
    public void onClick(View view) {

    }

    private void queryProvinces(){
        Map<String,Object> condition=new HashMap<>();
        ResultData queryResponse=weatherDB.queryProvince(condition);
        if (queryResponse.getResponseCode()== ResponseCode.RESPONSE_OK){
            provinceList=(List<Province>)queryResponse.getData();
            if (provinceList.size()>0){
                dataList.clear();
                for (Province p:provinceList){
                    dataList.add(p.getName());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                titleText.setText("中国");
                currentLevel=LEVEL_PROVINCE;

            }else {
                queryFromServer(null,"province");
            }
        }else {
            Log.e(TAG,"queryProvinces error");
        }

    }

    private void queryCities(){
        Map<String,Object> condition=new HashMap<>();
        condition.put("selection","province_id=?");
        condition.put("selectionArgs",new  String[]{selectedProvince.getProvinceId()});
        ResultData queryResponse=weatherDB.queryCity(condition);
        if (queryResponse.getResponseCode()== ResponseCode.RESPONSE_OK){
            cityList=(List<City>)queryResponse.getData();
            if (cityList.size()>0){
                dataList.clear();
                for (City c:cityList){
                    dataList.add(c.getName());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                titleText.setText(selectedProvince.getName());
                currentLevel=LEVEL_CITY;

            }else {
                queryFromServer(selectedProvince.getCode(),"city");
            }
        }else {
            Log.e(TAG,"queryCities error");
        }
    }

    private void queryCounties(){
        Map<String,Object> condition=new HashMap<>();
        condition.put("selection","city_id=?");
        condition.put("selectionArgs",new  String[]{selectedCity.getCityId()});
        ResultData queryResponse=weatherDB.queryCounty(condition);

        if (queryResponse.getResponseCode()== ResponseCode.RESPONSE_OK){
            countyList=(List<County>)queryResponse.getData();
            if (countyList.size()>0){
                dataList.clear();
                for (County c:countyList){
                    dataList.add(c.getName());
                }
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                titleText.setText(selectedCity.getName());
                currentLevel=LEVEL_COUNTY;

            }else {
                queryFromServer(selectedCity.getCode(),"county");
            }
        }else {
            Log.e(TAG,"queryCounties error");
        }
    }

    private void queryFromServer(final String code,final String type){
        String address;
        if (!TextUtils.isEmpty(code)){
            address="http://www.weather.com.cn/data/list3/city"+code+".xml";
        }else {
            address="http://www.weather.com.cn/data/list3/city.xml";
        }
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                ResultData resultData=new ResultData();
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROE);
                if ("province".equals(type)){
                    resultData=Utility.handleProvinceResponse(weatherDB,response);
                }else if ("city".equals(type)){
                    resultData=Utility.handleCityResponse(weatherDB,response,selectedProvince.getProvinceId());
                }else if ("county".equals(type)){
                    resultData=Utility.handleCountyResponse(weatherDB,response,selectedCity.getCityId());
                }

                if (resultData.getResponseCode()==ResponseCode.RESPONSE_OK){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if ("city".equals(type)){
                                queryCities();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }else {
                    Log.e(TAG,"queryFromServer error: "+resultData.toString());
                }

            }

            @Override
            public void onError(Exception e) {
                //通过runOnUiThread方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseAreaActivity.this,"加载失败",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog=new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if (currentLevel==LEVEL_COUNTY){
            queryCities();
        }else if (currentLevel==LEVEL_CITY){
            queryProvinces();
        }else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        closeProgressDialog();
    }
}
