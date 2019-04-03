package com.gaoch.brilliantpic;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gaoch.brilliantpic.myclass.BasicUserInfo;
import com.gaoch.brilliantpic.myview.CircleExp;
import com.gaoch.brilliantpic.util.Blur;
import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.HttpUtil;
import com.gaoch.brilliantpic.util.Utility;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ActivityUserInfo extends AppCompatActivity {

    private static final int msg_noInternet=111;
    private static final int msg_make=112;
    BasicUserInfo basicUserInfo;

    private TextView tv_exp,tv_username,tv_likes,tv_commentsnum,tv_make;
    private CircleImageView iv_userpic;
    private CircleExp circleExp;

    private ConstraintLayout layout_bkg,layout_2;
    private LinearLayout layout_1;
    private Blur.BlurLayout blurLayout1,blurLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_user_info);

        tv_commentsnum=findViewById(R.id.userinfo_tv_commentsnum);
        tv_likes=findViewById(R.id.userinfo_tv_likes);
        tv_username=findViewById(R.id.userinfo_tv_name);
        tv_exp=findViewById(R.id.userinfo_tv_exp);
        tv_make=findViewById(R.id.userinfo_tv_makes);

        iv_userpic=findViewById(R.id.userinfo_iv);
        circleExp=findViewById(R.id.userinfo_exp);
        layout_bkg=findViewById(R.id.userinfo_layout_bkg);
        layout_1=findViewById(R.id.userinfo_layout1);
        layout_2=findViewById(R.id.userinfo_layout2);



        File bkgFile=new File(ConstValue.getBkgPath(getApplicationContext()));

        if(bkgFile.exists()&&bkgFile.isFile()){
            layout_bkg.setBackground(Drawable.createFromPath(ConstValue.getBkgPath(getApplicationContext())));
        }else{
            layout_bkg.setBackground(getResources().getDrawable(R.drawable.bkg_2,null));
        }

        File bkgFile1=new File(ConstValue.getBkg_blurPath(getApplicationContext()));
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        if(bkgFile1.exists()&&bkgFile1.isFile()){
            Blur.initBkgWithResieze(layout_bkg,Utility.LoadLocalBitmap(ConstValue.getBkg_blurPath(getApplicationContext())),width,height);
        }



        blurLayout1=new Blur.BlurLayout(layout_1,layout_bkg);
        blurLayout2=new Blur.BlurLayout(layout_2,layout_bkg);





         basicUserInfo= (BasicUserInfo) getIntent().getSerializableExtra(ConstValue.bundle_user);
         if(basicUserInfo!=null){
             tryGetBasicInfo(basicUserInfo.getAccount());
             if(!basicUserInfo.getUserpic().equals("")){
                 RequestOptions options = new RequestOptions().placeholder(R.drawable.user_pic).error(R.drawable.user_pic).centerCrop().dontAnimate();
                 Glide.with(this).load(ConstValue.url_picUser(basicUserInfo.getUserpic()))
                         .apply(options)
                         .into(iv_userpic);
             }
             tv_username.setText(basicUserInfo.getUsername());
         }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Blur.destroy(layout_bkg);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case msg_make:
                    tv_commentsnum.setText(basicUserInfo.getCommentsnum()+"");
                    tv_likes.setText(basicUserInfo.getLikes()+"");
                    tv_make.setText(basicUserInfo.getPicList().size()+"");

                    int expAll= basicUserInfo.getExp();
                    int exp_index=expAll;
                    int level=0;
                    int thisLevelExp=1;
                    while (exp_index>thisLevelExp){
                        exp_index-=thisLevelExp;
                        level++;
                        thisLevelExp*=ConstValue.expLevel;
                    }

                    int angle=(int)((exp_index+0.0)/thisLevelExp*360);
                    circleExp.setAngle(angle+90);
                    tv_exp.setText("Lv."+level);
                    Log.e("GGG",angle+"");



                    break;
                case msg_noInternet:
                    Toast.makeText(ActivityUserInfo.this, "无网络!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    /**
     * 获取该用户的基本信息
     */
    public void tryGetBasicInfo(Long account){
        String address=ConstValue.url_BasicUserInfo(account);
        HttpUtil.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what=msg_noInternet;
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                Log.e("GGG",responseText);
                basicUserInfo= new Gson().fromJson(responseText,BasicUserInfo.class);
                    Message msg = new Message();
                    msg.what=msg_make;
                    handler.sendMessage(msg);
            }
        });
    }
}
