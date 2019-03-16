package com.gaoch.test.util;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

/**
 * Created by GaoCh on 2019/2/20.
 */

public class ConstValue {
    public static final String LocalDatabaseName = "brillaintpaint.db";

    //service linnk
    public static final String serverIp="http://brilliantpic.vipgz1.idcfengye.com/";
    public static final String url_StyleAll =serverIp+"style/all";
    public static final String url_StyleSearch(String type){
        return serverIp+"style/search?type="+type;
    }
    public static final String url_login(String account,String password){
        return serverIp+"user/login?account="+account+"&password="+password;
    }
    public static final String url_signup(String username,String password){
        return serverIp+"user/signup?username="+username+"&password="+password;
    }
    public static final String url_update_username(String newUsername,String password,String account){
        return serverIp+"user/signup?username="+newUsername+"&password="+password+"&account="+account;
    }
    public static final String url_userfiles(String account){
        return serverIp+"pic/getPic?account="+account;
    }
    public static final String url_newestUserfiles(int id){
        return serverIp+"pic/getPic?id="+id;
    }
    public static final String url_picAfter(String filename){
        return serverIp+"images/"+filename;
    }


    public static final int max_length_username=16;
    public static final int max_length_password=16;
    public static final int min_length_password=6;
    public static final int min_length_username=1;



    public static final String key_image="key_image";
    public static final String key_pic="key_pic";
    public static final String key_imageUrl="key_imageUrl";
    public static final String key_imageCropX="key_imageCropX";
    public static final String key_imageCropY="key_imageCropY";
    public static final String key_fileMessage="key_fileMessage";
    public static final String bundle_user="bundle_user";
    public static final String key_crop="key_croptype";
    public static final String CROP_BKG="bkg";
    public static final String CROP_USER="user";



    //SharedPreference
    public static final String sp="sp";
    public static final String hasDB="sp_hasDB";
    public static final String hasOpen="sp_hasOPEN";
    public static final String spUsername ="sp_username";
    public static final String spIsBlur="sp_idBlur";
    public static final String spBkgPath="sp_bkgPath";
    public static final String spAccount="sp_account";
    public static final String spPassword ="sp_password";
    public static final String sp_radius="sp_radius";
    public static final String spExp ="sp_exp";


    //毛玻璃效果参数,可以动态修改
    public static int RoundCorner=50;
    public static int radius=10;
    public static int scaleFactor=26;


    //图片尺寸
    public static final int pic_size_postCard_x=800;
    public static final int pic_size_postCard_y=600;

    //图片存储路径
    public static String filePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/BrilliantPaint/";
    public static String getBkgPath(Context context){
        return context.getExternalFilesDir("bky.jpg").getAbsolutePath();
    }
    public static String getBkg_blurPath(Context context){
        return context.getExternalFilesDir("blurbky.jpg").getAbsolutePath();
    }




    //选色
    public static int colorRange=64;

    public static final String[] LOCATIONGPS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    public static final int serverPortUser=3001;
    public static final int noInternet=1111;













}
