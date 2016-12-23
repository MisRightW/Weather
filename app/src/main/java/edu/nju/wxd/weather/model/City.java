package edu.nju.wxd.weather.model;

import edu.nju.wxd.weather.model.entity.Entity;

/**
 * Created by wxd on 2016/12/23.
 */

public class City extends Entity {
    private String cityId;
    private String name;
    private String code;
    private Province province;

    public City() {
        super();
    }

    public City(String cityId, String name, String code, Province province) {
        this.cityId = cityId;
        this.name = name;
        this.code = code;
        this.province = province;
    }

    public String getCityId() {
        return cityId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    @Override
    public String toString() {
        return "[Id: "+this.cityId+"name: "+this.name+"code: "+this.code+"province: "+this.province.toString()+"]";
    }
}
