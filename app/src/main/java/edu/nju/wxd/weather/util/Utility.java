package edu.nju.wxd.weather.util;

import android.text.TextUtils;

import edu.nju.wxd.weather.dao.WeatherDB;
import edu.nju.wxd.weather.model.City;
import edu.nju.wxd.weather.model.County;
import edu.nju.wxd.weather.model.Province;

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
}
