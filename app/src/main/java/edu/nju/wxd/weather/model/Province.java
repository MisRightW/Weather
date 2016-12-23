package edu.nju.wxd.weather.model;

import edu.nju.wxd.weather.model.entity.Entity;

/**
 * Created by wxd on 2016/12/23.
 */

public class Province extends Entity{

    private String provinceId;
    private String name;
    private String code;

    public Province() {
        super();
    }

    public Province(String provinceId, String name, String code) {
        this.provinceId = provinceId;
        this.name = name;
        this.code = code;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "[Id: "+this.provinceId+"name: "+this.name+"code: "+this.code+"]";
    }
}
