package com.gaoch.brilliantpic.util;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.gaoch.brilliantpic.adapter.LocalDatabaseHelper;
import com.gaoch.brilliantpic.myclass.FileMessage;
import com.gaoch.brilliantpic.myclass.User;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

import static com.gaoch.brilliantpic.util.ConstValue.picPath;

/**
 * Created by GaoCh on 2018/7/23.
 */

public class Utility {
    public static int dp2px(Context context,float dpValue){
        float scale=context.getResources().getDisplayMetrics().density;
        return (int)(dpValue*scale+0.5f);
    }


    public static int sp2px(Context context,float spValue){
        float fontScale=context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue*fontScale+0.5f);
    }

    public static boolean isDbEmpty(SQLiteDatabase db){
        Cursor cursor = db.query(LocalDatabaseHelper.table_name, null, null, null, null, null, null);
        if(cursor.moveToNext()){
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    /**
     * 按照最大范围来放大图片
     * @param path
     * @return
     */
    public static Drawable ReSizePic(String path,int maxHeight,int maxWidth,Context context){
        BitmapDrawable bd=(BitmapDrawable)Drawable.createFromPath(path);
        Bitmap bkg1=bd.getBitmap();
        float prePicWidth=bkg1.getWidth();
        float prePicHeight=bkg1.getHeight();
        double displayScale=(maxWidth+0.0)/maxHeight;
        double prePicScale=prePicWidth/prePicHeight;
        Log.e("GGG","显示区域:"+maxHeight+","+maxWidth);
        Log.e("GGG","图片尺寸:"+prePicHeight+","+prePicWidth);
        Log.e("GGG","显示区域比例:"+displayScale);
        Log.e("GGG","图片比例:"+prePicScale);
        if(prePicScale>displayScale){
            //图片宽度过大,将宽度放大到maxWidth
            float scaleFactor=maxWidth/prePicWidth;
            //按照高度缩放
            Log.e("GGG","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            return new BitmapDrawable(context.getResources(),bkg_scaled);


        }else{
            //背景图片长度过大，将长度放大到maxHeight
            float scaleFactor=maxHeight/prePicHeight;
            //按照宽度缩放
            Log.e("GGG","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            return new BitmapDrawable(context.getResources(),bkg_scaled);
        }
    }
    /**
     * 按照最大范围来放大图片
     * @param bitmap
     * @return
     */
    public static Drawable ReSizePic(Bitmap bitmap,int maxHeight,int maxWidth,Context context){
        Bitmap bkg1=bitmap;
        float prePicWidth=bkg1.getWidth();
        float prePicHeight=bkg1.getHeight();
        double displayScale=(maxWidth+0.0)/maxHeight;
        double prePicScale=prePicWidth/prePicHeight;
        Log.e("GGG","显示区域:"+maxHeight+","+maxWidth);
        Log.e("GGG","图片尺寸:"+prePicHeight+","+prePicWidth);
        Log.e("GGG","显示区域比例:"+displayScale);
        Log.e("GGG","图片比例:"+prePicScale);
        if(prePicScale>displayScale){
            //图片宽度过大,将宽度放大到maxWidth
            float scaleFactor=maxWidth/prePicWidth;
            //按照高度缩放
            Log.e("GGG","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            return new BitmapDrawable(context.getResources(),bkg_scaled);


        }else{
            //背景图片长度过大，将长度放大到maxHeight
            float scaleFactor=maxHeight/prePicHeight;
            //按照宽度缩放
            Log.e("GGG","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            return new BitmapDrawable(context.getResources(),bkg_scaled);
        }
    }

    /**
     * 按照最大范围来放大图片
     * @param drawable
     * @return
     */
    public static Drawable ReSizePic(Drawable drawable,int maxHeight,int maxWidth,Context context){
        Bitmap bkg1=((BitmapDrawable)drawable).getBitmap();
        float prePicWidth=bkg1.getWidth();
        float prePicHeight=bkg1.getHeight();
        double displayScale=(maxWidth+0.0)/maxHeight;
        double prePicScale=prePicWidth/prePicHeight;
        Log.e("GGG","显示区域:"+maxHeight+","+maxWidth);
        Log.e("GGG","图片尺寸:"+prePicHeight+","+prePicWidth);
        Log.e("GGG","显示区域比例:"+displayScale);
        Log.e("GGG","图片比例:"+prePicScale);
        if(prePicScale>displayScale){
            //图片宽度过大,将宽度放大到maxWidth
            float scaleFactor=maxWidth/prePicWidth;
            //按照高度缩放
            Log.e("GGG","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            return new BitmapDrawable(context.getResources(),bkg_scaled);


        }else{
            //背景图片长度过大，将长度放大到maxHeight
            float scaleFactor=maxHeight/prePicHeight;
            //按照宽度缩放
            Log.e("GGG","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            return new BitmapDrawable(context.getResources(),bkg_scaled);
        }
    }

    /**
     * 按照屏幕尺寸剪裁图片
     * @param path
     * @return
     */
    public static Drawable getCuteedBkg(String path, Context context, WindowManager windowManager){
        BitmapDrawable bd=(BitmapDrawable)Drawable.createFromPath(path);
        Bitmap bkg1=bd.getBitmap();

        DisplayMetrics metrics =new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(metrics);

        float width = metrics.widthPixels;
        float height = metrics.heightPixels;
        Log.e("SimWeather","屏幕尺寸:"+width+","+height);

        float prePicWidth=bkg1.getWidth();
        float prePicHeight=bkg1.getHeight();
        Log.e("SimWeather","原始图片尺寸:"+prePicWidth+","+prePicHeight);
        float displayScale=width/height;
        float prePicScale=prePicWidth/prePicHeight;
        Log.e("SimWeather","屏幕宽高比例:"+displayScale+",原始图片宽高比例:"+prePicScale);
        if(displayScale<prePicScale){

            //背景图片宽度过大,按照高度缩放
            float scaleFactor=height/prePicHeight;
            //按照高度缩放
            Log.e("SimWeather","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            float pic_width=bkg_scaled.getWidth();
            float pic_height=bkg_scaled.getHeight();
            Bitmap bkg_nedded=Bitmap.createBitmap(bkg_scaled,(((int)pic_width-(int)width)/2),0,(int)width,(int)height);
            return new BitmapDrawable(context.getResources(),bkg_nedded);


        }else{
            //背景图片宽度过小
            float scaleFactor=width/prePicWidth;
            //按照宽度缩放
            Log.e("SimWeather","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            float pic_width=bkg_scaled.getWidth();
            float pic_height=bkg_scaled.getHeight();
            Bitmap bkg_nedded=Bitmap.createBitmap(bkg_scaled,0,((int)pic_height-(int)height)/2,(int)width,(int)height);
            return new BitmapDrawable(context.getResources(),bkg_nedded);
        }
    }
    /**
     * 按照屏幕尺寸剪裁图片
     * @param drawable
     * @return
     */
    public static Drawable getCuteedBkg(Drawable drawable, Context context, WindowManager windowManager){
        BitmapDrawable bd1=(BitmapDrawable)drawable;
        Bitmap bkg1=bd1.getBitmap();

        DisplayMetrics metrics =new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(metrics);

        float width = metrics.widthPixels;
        float height = metrics.heightPixels;
        Log.e("SimWeather","屏幕尺寸:"+width+","+height);

        float prePicWidth=bkg1.getWidth();
        float prePicHeight=bkg1.getHeight();
        Log.e("SimWeather","原始图片尺寸:"+prePicWidth+","+prePicHeight);
        float displayScale=width/height;
        float prePicScale=prePicWidth/prePicHeight;
        Log.e("SimWeather","屏幕宽高比例:"+displayScale+",原始图片宽高比例:"+prePicScale);
        if(displayScale<prePicScale){

            //背景图片宽度过大,按照高度缩放
            float scaleFactor=height/prePicHeight;
            //按照高度缩放
            Log.e("SimWeather","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            float pic_width=bkg_scaled.getWidth();
            float pic_height=bkg_scaled.getHeight();
            Bitmap bkg_nedded=Bitmap.createBitmap(bkg_scaled,(((int)pic_width-(int)width)/2),0,(int)width,(int)height);
            Log.e("SimWeather","剪裁后的图片:"+bkg_nedded.getWidth()+","+bkg_nedded.getHeight());
            return new BitmapDrawable(context.getResources(),bkg_nedded);


        }else{
            //背景图片宽度过小
            float scaleFactor=width/prePicWidth;
            //按照宽度缩放
            Log.e("SimWeather","缩放后的图片:"+(int)(prePicWidth*scaleFactor)+","+(int)(prePicHeight*scaleFactor));
            Bitmap bkg_scaled= Bitmap.createScaledBitmap(bkg1,(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
            float pic_width=bkg_scaled.getWidth();
            float pic_height=bkg_scaled.getHeight();
            Bitmap bkg_nedded=Bitmap.createBitmap(bkg_scaled,0,((int)pic_height-(int)height)/2,(int)width,(int)height);
            Log.e("SimWeather","剪裁后的图片:"+bkg_nedded.getWidth()+","+bkg_nedded.getHeight());
            return new BitmapDrawable(context.getResources(),bkg_nedded);
        }
    }

    public static Bitmap LoadLocalBitmap(String path){
        BitmapDrawable bd=(BitmapDrawable)Drawable.createFromPath(path);
       return bd.getBitmap();
    }



    public static boolean saveDrawableToFile(Drawable drawable,String filename){
        File file = new File(ConstValue.picPath);
        if(!file.exists()){
            file.mkdirs();
            Log.e("GGG","创建文件夹");
        }
        File imageFile=new File(ConstValue.picPath +filename);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        if (drawable == null) {
            Log.e("GGG","图片不存在");
            return false;
        }
        Bitmap bitmap= ((BitmapDrawable)drawable).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        try {
            fos.flush();
            fos.close();
            Log.e("GGG","图片保存在："+ imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public static String saveBitmapToFile(Bitmap bitmap,int quality,String fileName,String fileType){
        File file = new File(picPath);
        if(!file.exists()){
            file.mkdirs();
            Log.e("GGG","创建文件夹");
        }
        File imageFile=new File(picPath +fileName+"."+fileType);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        if (bitmap == null) {
            Log.e("GGG","图片不存在");
            return null;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
        try {
            fos.flush();
            fos.close();
            Log.e("GGG","图片保存在："+ imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageFile.getAbsolutePath();
    }

    public static String saveBitmapToFileInData(Bitmap bitmap,String filePath){
        File imageFile=new File(filePath);
        if(imageFile.exists()){
            imageFile.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        if (bitmap == null) {
            Log.e("GGG","图片不存在");
            return null;
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        try {
            fos.flush();
            fos.close();
            Log.e("GGG","图片保存在："+ imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return imageFile.getAbsolutePath();
    }


    public static void setFullScreen_origin(Activity mAc){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = mAc.getWindow();
            mAc.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
                //在P下允许全屏app使用刘海屏
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
                window.setAttributes(lp);
            }
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }
//    public static void hideStatusBar(Activity mAc){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = mAc.getWindow();
//            mAc.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.P){
//                //在P下允许全屏app使用刘海屏
//                WindowManager.LayoutParams lp = window.getAttributes();
//                lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
//                window.setAttributes(lp);
//            }
//            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
//    }



    /**
     * 上传文件到服务器
     * @param handler
     * @param servicePath     上传服务器地址
     * @param localFilePath       本地文件的绝对路径
     * @return  接受到的文件在本地的绝对路径
     */
    public static FileMessage uploadLogFile(Handler handler, String servicePath, String localFilePath)throws Exception{
        URL url = new URL(servicePath);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        // 允许Input、Output，不使用Cache
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        con.setConnectTimeout(100000);
        con.setReadTimeout(100000);
        // 设置传送的method=POST
        con.setRequestMethod("POST");
        //在一次TCP连接中可以持续发送多份数据而不会断开连接
        con.setRequestProperty("Connection", "Keep-Alive");
        //设置编码
        con.setRequestProperty("Charset", "UTF-8");
        //text/plain能上传纯文本文件的编码格式
        con.setRequestProperty("Content-Type", "image/jpeg");
        // con.setRequestProperty("Content-Type", "text/plain");

        // 设置DataOutputStream
        DataOutputStream ds = new DataOutputStream(con.getOutputStream());

        // 取得文件的FileInputStream

        FileInputStream fStream = new FileInputStream(localFilePath);
        // 设置每次写入1024bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int length = -1;
        // 从文件读取数据至缓冲区
        int sum=0;
        while ((length = fStream.read(buffer)) != -1) {
            // 将资料写入DataOutputStream中
            ds.write(buffer, 0, length);
            sum+=length;
        }
        Log.e("GGG","上传的图片大小为:"+(sum/1024)+"KB");

        ds.flush();
        fStream.close();
        ds.close();



        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();

        in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
        String str = null;
        while ((str = in.readLine()) != null) {
            sb.append(str);
        }

        Log.e("GGG",sb.toString());
        FileMessage fileMessage = new Gson().fromJson(sb.toString(),FileMessage.class);

        return fileMessage;
    }


    /**
     * 上传文件到服务器
     * @param context
     * @param servicePath     上传服务器地址
     * @param localFilePath       本地文件的绝对路径
     * @return  接受到的文件在本地的绝对路径
     */
    public static User uploadUserPicFile(Context context, String servicePath, String localFilePath)throws Exception{
        URL url = new URL(servicePath);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();

        // 允许Input、Output，不使用Cache
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        con.setConnectTimeout(100000);
        con.setReadTimeout(100000);
        // 设置传送的method=POST
        con.setRequestMethod("POST");
        //在一次TCP连接中可以持续发送多份数据而不会断开连接
        con.setRequestProperty("Connection", "Keep-Alive");
        //设置编码
        con.setRequestProperty("Charset", "UTF-8");
        //text/plain能上传纯文本文件的编码格式
        con.setRequestProperty("Content-Type", "image/jpeg");
        // con.setRequestProperty("Content-Type", "text/plain");

        // 设置DataOutputStream
        DataOutputStream ds = new DataOutputStream(con.getOutputStream());

        // 取得文件的FileInputStream

        FileInputStream fStream = new FileInputStream(localFilePath);
        // 设置每次写入1024bytes
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int length = -1;
        // 从文件读取数据至缓冲区
        int sum=0;
        while ((length = fStream.read(buffer)) != -1) {
            // 将资料写入DataOutputStream中
            ds.write(buffer, 0, length);
            sum+=length;
        }
        Log.e("GGG","上传的图片大小为:"+(sum/8)/1024+"KB");

        ds.flush();
        fStream.close();
        ds.close();
        Log.e("GGG","文件上传成功！上传文件为：" + localFilePath);
        //String picPath= downloadFile(new DataInputStream(con.getInputStream()),new File(oldFilePath).getName());
        //Log.e("GGG","文件下载成功！下载文件为：" + picPath);

        BufferedReader in = null;
        StringBuilder sb = new StringBuilder();

        in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
        String str = null;
        while ((str = in.readLine()) != null) {
            sb.append(str);
        }

        Log.e("GGG",sb.toString());
//        JSONObject jsonObject = null;
//        jsonObject = new JSONObject(responseText);
        User user = new Gson().fromJson(sb.toString(),User.class);

        return user;
    }


    //判断字符串是否为整数
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }



    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId =context.getResources().getIdentifier("status_bar_height","dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        Log.e("GGG","状态栏的高度:"+result);
        return result;
    }
    /**
     * 获取导航栏高度
     * @param context
     * @return
     */
    public static int getNavigationHeight(Context context) {
        int result = 0;
        int resourceId=0;
        int rid = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
        if (rid!=0){
            resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            return context.getResources().getDimensionPixelSize(resourceId);
        }else
            return 0;
    }

    public static String getFileName(String url){
        String s[]=url.split("/");
        if(s.length<=1){
            s=url.split("\\\\");
        }
        return s[s.length-1];

    }


    /**
     * 从本地选择媒体文件，转化为路径
     * @param uri
     * @param context
     * @return
     */
    public static String getPathFromUri(Uri uri,Context context){
        if (Build.VERSION.SDK_INT >= 29){
            String s=uri.getPath();
            Log.e("GGG",s);
        }
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];
        Uri contentUri = null;
        if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }
        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{split[1]};
        return getDataColumn(context, contentUri, selection, selectionArgs);
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }






}
