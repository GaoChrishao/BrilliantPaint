package com.gaoch.brilliantpic.util;

import android.Manifest;
import android.content.Context;
import android.os.Environment;

/**
 * Created by GaoCh on 2019/2/20.
 */

public class ConstValue {
    public static final String LocalDatabaseName = "brillaintpaint.db";

    //service linnk
    //public static final String serverIp="http://brilliantpic.vipgz1.idcfengye.com/";
    public static final String serverIp="http://148.70.149.235:1195/";
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
    public static final String url_upload_userpic(String password,String account){
        return serverIp+"user/uploadUserPic?&password="+password+"&account="+account;
    }

    public static final String url_userfiles(String account){
        return serverIp+"pic/getPic?account="+account;
    }
    public static final String url_newestUserfiles(int id){
        return serverIp+"pic/getPic?id="+id;
    }
    public static final String url_picUser(String fileName){
        return serverIp+"images/user/"+fileName;
    }
    public static final String url_picAfter(String filename){
        return serverIp+"images/after/"+filename;
    }
    public static final String url_picPre(String filename){
        return serverIp+"images/pre/"+filename;
    }

    public static final String url_commentShare(Long account,String password,String content,Long fileid){
        return serverIp+"comment/share?account="+account+"&password="+password+"&content="+content+"&fileid="+fileid;
    }
    public static final String url_commentGet(Long fileid){
        return serverIp+"comment/get?fileid="+fileid;
    }
    public static final String url_commentNewestGet(Long fileid,Long commentid){
        return serverIp+"comment/get?fileid="+fileid+"&id="+commentid;
    }

    public static final String url_BasicUserInfo(Long account){
        return serverIp+"user/getBasic?account="+account;
    }

    public static final String url_sendIsLike(Long account,Long fileid,String password,boolean isLike){
        return serverIp+"like/shareLike?account="+account+"&password="+password+"&isLike="+isLike+"&fileid="+fileid;
    }
    public static final String url_getIsLike(Long account,Long fileid){
        return serverIp+"like/getIsLike?account="+account+"&fileid="+fileid;
    }




    public static final int max_length_username=16;
    public static final int max_length_password=16;
    public static final int min_length_password=6;
    public static final int min_length_username=1;



    public static final String key_glideUserpic="key_glide_user_pic";
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
    public static final String key_makeType="key_maketype";


    public static final String type_make_camera="camera";
    public static final String type_make_all="all";
    public static final String type_make_ink="ink";
    public static final String type_make_oil="oil";
    public static final String type_make_water="water";
    public static final String type_make_strange="strange";
    public static final String type_make_building="building";
    public static final String type_make_face="face";
    public static final String type_make_scene="scene";
    public static final String type_make_cartoon="cartoon";




    //SharedPreference
    public static final String sp="sp";
    public static final String hasDB="sp_hasDB";
    public static final String hasOpen="sp_hasOPEN";
    public static final String spUsername ="sp_username";
    public static final String spIsBlur="sp_idBlur";
    public static final String spBkgPath="sp_bkgPath";
    public static final String spAccount="sp_account";
    public static final String spPassword ="sp_password";
    public static final String spUserPic ="sp_userpic";
    public static final String sp_radius="sp_radius";
    public static final String spExp ="sp_exp";


    //经验相关
    public static final int expLevel=10;


    //线程等待时间
    public static final int thread_sleep=1000;  //ms

    //毛玻璃效果参数,可以动态修改
    public static int RoundCorner=50;
    public static int radius=10;
    public static int scaleFactor=26;


    //图片尺寸
    public static final int pic_size_postCard_x=800;
    public static final int pic_size_postCard_y=600;
    public static final int pic_crop_maxWddth=1000;
    public static final int pic_crop_maxHeight=750;
    public static final int pic_User_maxWddth=250;
    public static final int pic_User_maxHeight=250;
    public static final int pic_quality=95;


    //视频相关
    public static final int video_fps=5;    //视频的fps
    public static final int video_maxPictures=20; //最大可以选择图片数量
    public static final int video_width=1000;
    public static final int video_height=750;
    public static final int video_den=2;    //每张图片的显示时间
    public static final int video_den_1=3;


    //图片存储路径
    public static String picPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/BrilliantPaint/";
    //视频存储路径
    public static String pic_video_Path = picPath +"/video/";
    //暂时存储路径
    public static String pic_video_tmp_Path = picPath +"/video/tmp/";


    public static String getBkgPath(Context context){
        return context.getExternalFilesDir("bkg.jpg").getAbsolutePath();
    }
    public static String getBkg_blurPath(Context context){
        return context.getExternalFilesDir("blurbkg.jpg").getAbsolutePath();
    }





    //选色
    public static int colorRange=64;

    public static final String[] LOCATIONGPS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };



    public static final int serverPortUser=3001;
    public static final int noInternet=1111;













}
