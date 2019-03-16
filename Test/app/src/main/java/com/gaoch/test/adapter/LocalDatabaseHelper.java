package com.gaoch.test.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gaoch.test.myclass.Pic;
import com.gaoch.test.myclass.Style;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocalDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    public static int INITIAL_VERSION = 1 ; // 初始版本号
    public static int NEW_VERSION = 2 ;       // 最新的版本号 ,增加styles表
    public static String table_name="styles";
    public static final String CREATE_STYLES ="create table styles("
            +"id integer primary key autoincrement,"
            +"type varchar,"
            + "name varchar,"
            +"modelname varchar,"
            + "picurl varchar)";
    public static final String CREATE_USERFILES ="create table userfiles("
            +"id integer primary key autoincrement,"
            +"account bigint,"
            + "stylename varchar,"
            + "username varchar,"
            +"picname varchar,"
            + "time timestamp)";


    public LocalDatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STYLES);
        db.execSQL(CREATE_USERFILES);
        Log.e("GGG","本地数据库创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //判断用户当前安装的本是不是1.0版本
        if(oldVersion == INITIAL_VERSION &&newVersion==2){
            db.execSQL(CREATE_USERFILES);
            oldVersion++;
        }
        Log.e("GGG","数据库升级成功");
        //Toast.makeText(context, "数据库升级", Toast.LENGTH_SHORT).show();
    }

    public static boolean has_Style(SQLiteDatabase db,String id){
        Cursor cursor = db.rawQuery("select * from styles where id="+id, null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        return false;
    }

    public static boolean has_UserPic(SQLiteDatabase db,String id){
        Cursor cursor = db.rawQuery("select * from userfiles where id="+id, null);
        if (cursor != null && cursor.getCount() > 0) {
            return true;
        }
        cursor.close();
        return false;
    }
    public static void proStyles(String responseText,SQLiteDatabase db){
        try {
            //db.delete("styles","id=?",new String[]{"*"});
            JSONArray jsonArray = new JSONArray(responseText);
            for(int i=0;i<jsonArray.length();i++){
                ContentValues value = new ContentValues();
                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                int id=jsonObject.getInt("id");
                if(LocalDatabaseHelper.has_Style(db,id+"")){
                    //执行更新数据库操作
                    value.put("type",jsonObject.getString("type"));
                    value.put("name",jsonObject.getString("name"));
                    value.put("modelname",jsonObject.getString("modelname"));
                    value.put("picurl",jsonObject.getString("picurl"));
                    db.update("styles",value,"id=?",new String[]{id+""});
                }else{
                    //执行写入数据库操作
                    value.put("id",jsonObject.getInt("id"));
                    value.put("type",jsonObject.getString("type"));
                    value.put("name",jsonObject.getString("name"));
                    value.put("modelname",jsonObject.getString("modelname"));
                    value.put("picurl",jsonObject.getString("picurl"));
                    db.insert("styles",null,value);
                }

            }
            db.close();
        } catch (JSONException e) {
            db.close();
            e.printStackTrace();
        }
    }

    public static List<Style>getStyles(String needtype,SQLiteDatabase db){
        List<Style>styleList=new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from styles ", null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String name=cursor.getString(cursor.getColumnIndex("name"));
                String type=cursor.getString(cursor.getColumnIndex("type"));
                String picurl=cursor.getString(cursor.getColumnIndex("picurl"));
                String modelname=cursor.getString(cursor.getColumnIndex("modelname"));
                Style style=new Style();
                style.setId(id);
                style.setModelname(modelname);
                style.setName(name);
                style.setPicurl(picurl);
                style.setType(type);
                styleList.add(style);

            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return styleList;
    }

    public static List<Pic>getUserPic(String needaccount,SQLiteDatabase db){
        List<Pic>picList=new ArrayList<>();

        Cursor cursor = db.rawQuery("select * from userfiles where account="+needaccount, null);
        if(cursor.moveToFirst()){
            do{
                int id=cursor.getInt(cursor.getColumnIndex("id"));
                String account=cursor.getString(cursor.getColumnIndex("account"));
                String username=cursor.getString(cursor.getColumnIndex("username"));
                String stylename=cursor.getString(cursor.getColumnIndex("stylename"));
                String picname=cursor.getString(cursor.getColumnIndex("picname"));
                Long time=cursor.getLong(cursor.getColumnIndex("time"));
                Pic pic = new Pic();
                pic.setId(id);
                pic.setAccount(Long.valueOf(account));
                pic.setPicname(picname);
                pic.setStylename(stylename);
                pic.setUsername(username);
                pic.setTime(time);
                picList.add(pic);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return picList;
    }

    public static void proUserfiles(String responseText,SQLiteDatabase db){
        try {
            JSONArray jsonArray = new JSONArray(responseText);
            for(int i=0;i<jsonArray.length();i++){
                ContentValues value = new ContentValues();
                JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                int id=jsonObject.getInt("id");
                if(LocalDatabaseHelper.has_UserPic(db,id+"")){
                    //执行更新数据库操作
                    value.put("account",jsonObject.getLong("account"));
                    value.put("username",jsonObject.getString("username"));
                    value.put("stylename",jsonObject.getString("stylename"));
                    value.put("picname",jsonObject.getString("picname"));
                    value.put("time",jsonObject.getString("time"));
                    db.update("userfiles",value,"id=?",new String[]{id+""});
                }else{
                    //执行写入数据库操作
                    value.put("id",id);
                    value.put("account",jsonObject.getLong("account"));
                    value.put("username",jsonObject.getString("username"));
                    value.put("stylename",jsonObject.getString("stylename"));
                    value.put("picname",jsonObject.getString("picname"));
                    value.put("time",jsonObject.getString("time"));
                    db.insert("userfiles",null,value);
                }

            }
            db.close();
        } catch (JSONException e) {
            db.close();
            e.printStackTrace();
        }
    }



}
