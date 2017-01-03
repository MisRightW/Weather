package edu.nju.wxd.weather.model;

/**
 * Created by wxd on 2017/1/2.
 */

public class Weather {

    private String cityName;
    private String weatherCode;
    private String temp1;
    private String temp2;
    private String weatherDesp;
    private String publichTime;

    public Weather(String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publichTime) {
        this.cityName = cityName;
        this.weatherCode = weatherCode;
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.weatherDesp = weatherDesp;
        this.publichTime = publichTime;
    }

    public Weather() {
        super();
    }

    public String getCityName() {
        return cityName;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public String getTemp1() {
        return temp1;
    }

    public String getTemp2() {
        return temp2;
    }

    public String getWeatherDesp() {
        return weatherDesp;
    }

    public String getPublichTime() {
        return publichTime;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public void setTemp1(String temp1) {
        this.temp1 = temp1;
    }

    public void setTemp2(String temp2) {
        this.temp2 = temp2;
    }

    public void setWeatherDesp(String weatherDesp) {
        this.weatherDesp = weatherDesp;
    }

    public void setPublichTime(String publichTime) {
        this.publichTime = publichTime;
    }
}
