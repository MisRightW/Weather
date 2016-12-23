package edu.nju.wxd.weather.model;

import edu.nju.wxd.weather.model.entity.Entity;

/**
 * Created by wxd on 2016/12/23.
 */

public class County extends Entity {
    private String countyId;
    private String name;
    private String code;
    private City city;

    public County() {
        super();
    }

    public County(String countyId, String name, String code, City city) {
        this.countyId = countyId;
        this.name = name;
        this.code = code;
        this.city = city;
    }

    public String getCountyId() {
        return countyId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public City getCity() {
        return city;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "[Id: "+this.countyId+"name: "+this.name+"code: "+this.code+"city: "+this.city.toString()+"]";
    }
}
