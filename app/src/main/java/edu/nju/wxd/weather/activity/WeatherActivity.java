package edu.nju.wxd.weather.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.nju.wxd.weather.R;
import edu.nju.wxd.weather.util.HttpCallbackListener;
import edu.nju.wxd.weather.util.HttpUtil;
import edu.nju.wxd.weather.util.Utility;

public class WeatherActivity extends AppCompatActivity {

    private TextView cityName;
    private TextView publichTime;
    private TextView currentTime;
    private TextView temp1;
    private TextView temp2;
    private TextView weatherDesp;
    private LinearLayout weatherInfoLayout;
    private static final String TAG="WeatherActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherInfoLayout=(LinearLayout) findViewById(R.id.weather_info_layout);
        cityName=(TextView)findViewById(R.id.city_name);
        publichTime=(TextView)findViewById(R.id.publih_text);
        currentTime=(TextView)findViewById(R.id.current_date);
        temp1=(TextView)findViewById(R.id.temp1);
        temp2=(TextView)findViewById(R.id.temp2);
        weatherDesp=(TextView)findViewById(R.id.weather_desp);

        String countyCode=getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)){
            //有县级代号就去查询天气
            publichTime.setText("同步中...");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityName.setVisibility(View.INVISIBLE);
            queryWeatherCode(countyCode);

        }else {
            //没有县级号码就直接显示本地天气
            showWeather();

        }
    }

    /*
    查询县级代号对应的天气代号
     */
    private void queryWeatherCode(String countyCode){
         String address="http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
         queryFromServer(address,"countyCode");
    }

    /*
    查询天气代号对应的天气
     */
    private void queryWeatherInfo(String weatherCode){
        String address="http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
        queryFromServer(address,"weatherCode");
    }

    /*
    根据传进来的code类别查询天气代号或天气信息
     */
    private void queryFromServer(final String address,final String type){
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

                if ("countyCode".equals(type)){
                    if (!TextUtils.isEmpty(response)){
                        String[] array=response.split("\\|");
                        if (array!=null&&array.length==2){
                            String weatherCode=array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }

                }else if ("weatherCode".equals(type)){
                    Utility.handleWeatherResponse(WeatherActivity.this,response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });

                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        publichTime.setText("同步失败");
                    }
                });
            }
        });

    }

    /*
    从SharedPreference中读取天气数据并显示到界面中
     */
    private void showWeather(){
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        cityName.setText(preferences.getString("city_name",""));
        Log.d(TAG,preferences.getString("city_name",""));
        publichTime.setText("今天"+preferences.getString("publish_time","")+"发布");
        currentTime.setText(preferences.getString("current_date",""));
        temp1.setText(preferences.getString("temp1",""));
        temp2.setText(preferences.getString("temp2",""));
        weatherDesp.setText(preferences.getString("weather_desp",""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);


    }
}
