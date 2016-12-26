package edu.nju.wxd.weather.util;

/**
 * Created by wxd on 2016/12/24.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);

}
