package edu.nju.wxd.weather.util;

/**
 * Created by wxd on 2016/12/23.
 */

public class ResultData {

    private ResponseCode responseCode;
    private Object data;
    private String description;

    public ResultData() {
        this.responseCode = responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public Object getData() {
        return data;
    }

    public String getDescription() {
        return description;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "[responseCode: "+this.responseCode+"data: "+this.data+"description: "+description+"]";
    }
}
