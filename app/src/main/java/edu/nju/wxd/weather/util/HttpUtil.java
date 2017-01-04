package edu.nju.wxd.weather.util;

import android.os.Build;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by wxd on 2016/12/24.
 */

public class HttpUtil {

    private static final String TAG="HttpUtil";

//    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
//         new Thread(new Runnable() {
//             @Override
//             public void run() {
//                 HttpURLConnection connection=null;
//
////                 if (Build.VERSION.SDK != null && Build.VERSION.SDK_INT > 13) {
////                     connection.setRequestProperty("Connection", "close");
////                 }
//                 Log.d("test...",address);
//                 try{
//                     URL url=new URL(address);
//                     connection=(HttpURLConnection)url.openConnection();
//                     connection.setRequestMethod("GET");
//                     connection.setConnectTimeout(8000);
//                     connection.setReadTimeout(8000);
//                     // 设定传送的内容类型是可序列化的java对象    (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
//                     //connection.setRequestProperty("Content-type", "application/x-java-serialized-object");
//                     InputStream in=connection.getInputStream();
//                     BufferedReader reader=new BufferedReader(new InputStreamReader(in));
//                     StringBuffer response=new StringBuffer();
//                     String line;
//                     while ((line=reader.readLine())!=null){
//                         Log.d(TAG,line);
//                         response.append(line);
//                     }
//                     if (listener!=null){
//                         listener.onFinish(response.toString());
//                     }
//
//                 }catch (Exception e){
//
//                     if (listener!=null){
//                         listener.onError(e);
//                     }
//
//                 }finally {
////                     if (connection!=null){
////                         connection.disconnect();
////                     }
//
//                 }
//
//             }
//         }).start();
//    }
//}


    public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient client=new DefaultHttpClient();

                Log.d("test...",address);
                try{
                    HttpGet httpget = new HttpGet(address);
                    HttpResponse res= client.execute(httpget);
                    HttpEntity entity = res.getEntity();
                    if (entity != null) {
                        entity = new BufferedHttpEntity(entity);
                        InputStream in = entity.getContent();
                        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                        StringBuffer response=new StringBuffer();
                        String line;
                        while ((line=reader.readLine())!=null){
                            Log.d(TAG,line);
                            response.append(line);
                        }
                        if (listener!=null){
                            listener.onFinish(response.toString());
                        }
                    }

                }catch (Exception e){

                    if (listener!=null){
                        listener.onError(e);
                    }

                }finally {
//                     if (connection!=null){
//                         connection.disconnect();
//                     }

                }

            }
        }).start();
    }
}
