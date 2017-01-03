package edu.nju.wxd.weather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import edu.nju.wxd.weather.dao.WeatherDB;
import edu.nju.wxd.weather.model.City;
import edu.nju.wxd.weather.model.County;
import edu.nju.wxd.weather.model.Province;
import edu.nju.wxd.weather.model.Weather;

/**
 * Created by wxd on 2016/12/25.
 */

public class Utility {

    private static final String TAG="Utility";

    public synchronized static ResultData handleProvinceResponse(WeatherDB weatherDB,String response){
        ResultData resultData=new ResultData();
        if (!TextUtils.isEmpty(response)){
            String[] allProvinces=response.split(",");
            if (allProvinces!=null&&allProvinces.length>0){
                for (String p:allProvinces){
                    String[] array=p.split("\\|");
                    Province province=new Province();
                    province.setCode(array[0]);
                    province.setName(array[1]);
                    ResultData insertResponse=weatherDB.insertProvince(province);
                    resultData.setResponseCode(insertResponse.getResponseCode());
                    if (insertResponse.getResponseCode()==ResponseCode.RESPONSE_OK){
                        resultData.setData(insertResponse.getData());
                    }else{
                        resultData.setDescription(insertResponse.getDescription());
                    }
                }
            }

        }
        return resultData;
    }

    public synchronized static ResultData handleCityResponse(WeatherDB weatherDB,String response,String provinceId){
        ResultData resultData=new ResultData();
        if (!TextUtils.isEmpty(response)){
            String[] allCitys=response.split(",");
            if (allCitys!=null&&allCitys.length>0){
                for (String c:allCitys){
                    String[] array=c.split("\\|");
                    City city=new City();
                    city.setCode(array[0]);
                    city.setName(array[1]);
                    Province province=new Province();
                    province.setProvinceId(provinceId);
                    city.setProvince(province);
                    ResultData insertResponse=weatherDB.insertCity(city);
                    resultData.setResponseCode(insertResponse.getResponseCode());
                    if (insertResponse.getResponseCode()==ResponseCode.RESPONSE_OK){
                        resultData.setData(insertResponse.getData());
                    }else{
                        resultData.setDescription(insertResponse.getDescription());
                    }
                }
            }

        }
        return resultData;
    }

    public synchronized static ResultData handleCountyResponse(WeatherDB weatherDB,String response,String cityId){
        ResultData resultData=new ResultData();
        if (!TextUtils.isEmpty(response)){
            String[] allCounties=response.split(",");
            if (allCounties!=null&&allCounties.length>0){
                for (String c:allCounties){
                    String[] array=c.split("\\|");
                    County county=new County();
                    county.setCode(array[0]);
                    county.setName(array[1]);
                    City city=new City();
                    city.setCityId(cityId);
                    county.setCity(city);
                    ResultData insertResponse=weatherDB.insertCounty(county);
                    resultData.setResponseCode(insertResponse.getResponseCode());
                    if (insertResponse.getResponseCode()==ResponseCode.RESPONSE_OK){
                        resultData.setData(insertResponse.getData());
                    }else{
                        resultData.setDescription(insertResponse.getDescription());
                    }
                }
            }

        }
        return resultData;
    }

    /*
      解析服务器返回的天气的json数据，并将结果存储到本地
     */
    public synchronized static ResultData handleWeatherResponse(Context context,String response){
        ResultData resultData=new ResultData();
        try {
            JSONObject object=new JSONObject(response);
            JSONObject weatherInfo=object.getJSONObject("weatherinfo");
            Weather weather=new Weather();
            weather.setCityName(weatherInfo.getString("city"));
            weather.setWeatherCode(weatherInfo.getString("cityid"));
            weather.setPublichTime(weatherInfo.getString("ptime"));
            weather.setTemp1(weatherInfo.getString("temp1"));
            weather.setTemp2(weatherInfo.getString("temp2"));
            weather.setWeatherDesp(weatherInfo.getString("weather"));
            saveWeatherInfo(context,weather);
        }catch (JSONException e){
            Log.e(TAG,e.getMessage());
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROE);
            resultData.setDescription(e.getMessage());
        }finally {
            return resultData;
        }

    }

    /*
      将所有天气信息存储到SharedPreference中
     */
    private synchronized static ResultData saveWeatherInfo(Context context, Weather weather){
        ResultData resultData=new ResultData();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(context).edit();

        try {
            editor.putBoolean("city_selected",true);
            editor.putString("city_name",weather.getCityName());
            editor.putString("weather_code",weather.getWeatherCode());
            editor.putString("temp1",weather.getTemp1());
            editor.putString("temp2",weather.getTemp2());
            editor.putString("weather_desp",weather.getWeatherDesp());
            editor.putString("publish_time",weather.getPublichTime());
            editor.putString("current_date",sdf.format(new Date()));
            editor.commit();
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROE);
            resultData.setDescription(e.getMessage());
        }finally {

            return resultData;
        }

    }
}
