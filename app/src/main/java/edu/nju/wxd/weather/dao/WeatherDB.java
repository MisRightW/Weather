package edu.nju.wxd.weather.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.nju.wxd.weather.model.City;
import edu.nju.wxd.weather.model.County;
import edu.nju.wxd.weather.model.Province;
import edu.nju.wxd.weather.util.IDGenerator;
import edu.nju.wxd.weather.util.ResponseCode;
import edu.nju.wxd.weather.util.ResultData;

/**
 * Created by wxd on 2016/12/23.
 */

public class WeatherDB {

    private static final String TAG="WeatherDB";

    public static final String DB_NAME="weather";
    public static final int VERSION=1;
    private static WeatherDB weatherDB;
    private SQLiteDatabase db;
    private Object lock=new Object();

    /*
    单例模式
     */
    private WeatherDB(Context context){
        WeatherOpenHelper weatherOpenHelper=new WeatherOpenHelper(context,DB_NAME,null,VERSION);
        db=weatherOpenHelper.getWritableDatabase();
    }

    public synchronized static WeatherDB getInstance(Context context){
        if(weatherDB==null){
            weatherDB=new WeatherDB(context);
        }
        return weatherDB;
    }

    public ResultData insertProvince(Province province){
        ResultData resultData=new ResultData();
        synchronized (lock){
             try {
                 if (province!=null){
                     ContentValues values=new ContentValues();
                     province.setProvinceId(IDGenerator.generate("PRO"));
                     values.put("id",province.getProvinceId());
                     values.put("name",province.getName());
                     values.put("code",province.getCode());
                     values.put("block_flag",province.isBlockFlag());
                     db.insert("province",null,values);
                 }

             }catch (Exception e){
                 Log.e(TAG,e.getMessage());
                 resultData.setResponseCode(ResponseCode.RESPONSE_ERROE);
                 resultData.setDescription(e.getMessage());

             }finally {
                 return resultData;

             }
        }
    }

    public ResultData insertCity(City city){
        ResultData resultData=new ResultData();
        synchronized (lock){
            try {
                if (city!=null){
                    ContentValues values=new ContentValues();
                    city.setCityId(IDGenerator.generate("CIT"));
                    values.put("id",city.getCityId());
                    values.put("name",city.getName());
                    values.put("code",city.getCode());
                    values.put("province_id",city.getProvince().getProvinceId());
                    values.put("block_flag",city.isBlockFlag());
                    db.insert("province",null,values);
                }

            }catch (Exception e){
                Log.e(TAG,e.getMessage());
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROE);
                resultData.setDescription(e.getMessage());

            }finally {
                return resultData;

            }
        }
    }

    public ResultData insertCounty(County county){
        ResultData resultData=new ResultData();
        synchronized (lock){
            try {
                if (county!=null){
                    ContentValues values=new ContentValues();
                    county.setCountyId(IDGenerator.generate("COU"));
                    values.put("id",county.getCountyId());
                    values.put("name",county.getName());
                    values.put("code",county.getCode());
                    values.put("city_id",county.getCity().getCityId());
                    values.put("block_flag",county.isBlockFlag());
                    db.insert("province",null,values);
                }

            }catch (Exception e){
                Log.e(TAG,e.getMessage());
                resultData.setResponseCode(ResponseCode.RESPONSE_ERROE);
                resultData.setDescription(e.getMessage());

            }finally {
                return resultData;

            }
        }
    }

    public ResultData queryProvince(Map<String,Object> condition){
        ResultData resultData=new ResultData();
        List<Province> list=new ArrayList<>();
        try {
            String table =  "province" ;
            String[] columns =null;//String[] columns = new  String[] { "CustomerName" ,  "SUM(OrderPrice)" };
            String selection =null;//String selection = "name=? and code=?" ;
            String[] selectionArgs=null;//String[] selectionArgs = new  String[]{ "zhejiang" ,"1"};
            String groupBy = null ;//String groupBy = "CustomerName" ;
            String having = null ;//String having = "SUM(OrderPrice)>500" ;
            String orderBy = null ;//String orderBy = "CustomerName" ;
            String limit=null;
            if (condition.containsKey("selection")){
                selection=(String) condition.get("selection");
            }
            if (condition.containsKey("selectionArgs")){
                selectionArgs=(String[]) condition.get("selectionArgs");
            }
            if (condition.containsKey("columns")){
                columns=(String[]) condition.get("columns");
            }
            if (condition.containsKey("groupBy")){
                groupBy=(String) condition.get("groupBy");
            }
            if (condition.containsKey("having")){
                having=(String) condition.get("having");
            }
            if (condition.containsKey("orderBy")){
                orderBy=(String) condition.get("orderBy");
            }
            if (condition.containsKey("limit")){
                limit=(String) condition.get("limit");
            }
            Cursor cursor=db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor.moveToFirst()){
                do {
                    Province province=new Province();
                    province.setProvinceId(cursor.getString(cursor.getColumnIndex("id")));
                    province.setCode(cursor.getString(cursor.getColumnIndex("code")));
                    province.setName(cursor.getString(cursor.getColumnIndex("name")));
                    if (cursor.getInt(cursor.getColumnIndex("block_flag"))==0){
                        province.setBlockFlag(false);
                    }else {
                        province.setBlockFlag(true);
                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = (Date) format.parse(cursor.getString(cursor.getColumnIndex("create_time")));
                    province.setCreateAt(new Timestamp(date.getTime()));
                    list.add(province);
                }while (cursor.moveToNext());
            }
            resultData.setData(list);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROE);
            resultData.setDescription(e.getMessage());

        }finally {
            return resultData;

        }
    }

    public ResultData queryCity(Map<String,Object> condition){
        ResultData resultData=new ResultData();
        List<City> list=new ArrayList<>();
        try {
            String table =  "city" ;
            String[] columns =null;//String[] columns = new  String[] { "CustomerName" ,  "SUM(OrderPrice)" };
            String selection =null;//String selection = "name=? and code=?" ;
            String[] selectionArgs=null;//String[] selectionArgs = new  String[]{ "zhejiang" ,"1"};
            String groupBy = null ;//String groupBy = "CustomerName" ;
            String having = null ;//String having = "SUM(OrderPrice)>500" ;
            String orderBy = null ;//String orderBy = "CustomerName" ;
            String limit=null;
            if (condition.containsKey("selection")){
                selection=(String) condition.get("selection");
            }
            if (condition.containsKey("selectionArgs")){
                selectionArgs=(String[]) condition.get("selectionArgs");
            }
            if (condition.containsKey("columns")){
                columns=(String[]) condition.get("columns");
            }
            if (condition.containsKey("groupBy")){
                groupBy=(String) condition.get("groupBy");
            }
            if (condition.containsKey("having")){
                having=(String) condition.get("having");
            }
            if (condition.containsKey("orderBy")){
                orderBy=(String) condition.get("orderBy");
            }
            if (condition.containsKey("limit")){
                limit=(String) condition.get("limit");
            }
            Cursor cursor=db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor.moveToFirst()){
                do {
                    City city=new City();
                    city.setCityId(cursor.getString(cursor.getColumnIndex("id")));
                    city.setCode(cursor.getString(cursor.getColumnIndex("code")));
                    city.setName(cursor.getString(cursor.getColumnIndex("name")));
                    if (cursor.getInt(cursor.getColumnIndex("block_flag"))==0){
                        city.setBlockFlag(false);
                    }else {
                        city.setBlockFlag(true);
                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = (Date) format.parse(cursor.getString(cursor.getColumnIndex("create_time")));
                    city.setCreateAt(new Timestamp(date.getTime()));
                    //关联查询province
                    condition.clear();
                    condition.put("selection","id=?");
                    condition.put("selectionArgs",new  String[]{cursor.getString(cursor.getColumnIndex("province_id"))});
                    ResultData queryData=queryProvince(condition);
                    if (!((List) queryData.getData()).isEmpty()){
                        Province province=((List<Province>)queryData.getData()).get(0);
                        city.setProvince(province);
                    }
                    list.add(city);
                }while (cursor.moveToNext());
            }
            resultData.setData(list);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROE);
            resultData.setDescription(e.getMessage());

        }finally {
            return resultData;

        }
    }

    public ResultData queryCounty(Map<String,Object> condition){
        ResultData resultData=new ResultData();
        List<County> list=new ArrayList<>();
        try {
            String table =  "county" ;
            String[] columns =null;//String[] columns = new  String[] { "CustomerName" ,  "SUM(OrderPrice)" };
            String selection =null;//String selection = "name=? and code=?" ;
            String[] selectionArgs=null;//String[] selectionArgs = new  String[]{ "zhejiang" ,"1"};
            String groupBy = null ;//String groupBy = "CustomerName" ;
            String having = null ;//String having = "SUM(OrderPrice)>500" ;
            String orderBy = null ;//String orderBy = "CustomerName" ;
            String limit=null;
            if (condition.containsKey("selection")){
                selection=(String) condition.get("selection");
            }
            if (condition.containsKey("selectionArgs")){
                selectionArgs=(String[]) condition.get("selectionArgs");
            }
            if (condition.containsKey("columns")){
                columns=(String[]) condition.get("columns");
            }
            if (condition.containsKey("groupBy")){
                groupBy=(String) condition.get("groupBy");
            }
            if (condition.containsKey("having")){
                having=(String) condition.get("having");
            }
            if (condition.containsKey("orderBy")){
                orderBy=(String) condition.get("orderBy");
            }
            if (condition.containsKey("limit")){
                limit=(String) condition.get("limit");
            }
            Cursor cursor=db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            if (cursor.moveToFirst()){
                do {
                    County county=new County();
                    county.setCountyId(cursor.getString(cursor.getColumnIndex("id")));
                    county.setCode(cursor.getString(cursor.getColumnIndex("code")));
                    county.setName(cursor.getString(cursor.getColumnIndex("name")));
                    if (cursor.getInt(cursor.getColumnIndex("block_flag"))==0){
                        county.setBlockFlag(false);
                    }else {
                        county.setBlockFlag(true);
                    }
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = (Date) format.parse(cursor.getString(cursor.getColumnIndex("create_time")));
                    county.setCreateAt(new Timestamp(date.getTime()));
                    //关联查询city
                    condition.clear();
                    condition.put("selection","id=?");
                    condition.put("selectionArgs",new  String[]{cursor.getString(cursor.getColumnIndex("city_id"))});
                    ResultData queryData=queryCity(condition);
                    if (!((List) queryData.getData()).isEmpty()){
                        City city=((List<City>)queryData.getData()).get(0);
                        county.setCity(city);
                    }
                    list.add(county);
                }while (cursor.moveToNext());
            }
            resultData.setData(list);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
            resultData.setResponseCode(ResponseCode.RESPONSE_ERROE);
            resultData.setDescription(e.getMessage());

        }finally {
            return resultData;

        }
    }
}
