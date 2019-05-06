package com.gaoch.brilliantpic;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.Utility;
import com.redbooth.WelcomeCoordinatorLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import static com.gaoch.brilliantpic.util.ConstValue.LOCATIONGPS;


public class ActivityStart extends Activity {
    private Button btn_start;
    private ValueAnimator backgroundAnimator;
    private WelcomeCoordinatorLayout coordinatorLayout;
    private LinearLayout layout;
    private Boolean animationReady=false;
    private final int requestPermissionsCode = 100;//权限请求码

    private ImageView iv_1,iv_2,iv_3,iv_4,iv_5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_start);



        initView();
        initializeBackgroundTransitions();
        initializeListeners();
        requestPermissons();



    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initView(){
        layout=findViewById(R.id.start_ll);
        coordinatorLayout = findViewById(R.id.coordinator);



        SharedPreferences preferences = getSharedPreferences(ConstValue.sp,MODE_PRIVATE);
        boolean hasOpen=preferences.getBoolean(ConstValue.hasOpen,false);
        String account=String.valueOf(preferences.getLong(ConstValue.spAccount,-1));
        if(hasOpen&&account.length()>2){
            layout.setBackground(Utility.getCuteedBkg(getResources().getDrawable(R.drawable.bkg_start),this,getWindowManager()));
            TaskIntentMain taskIntentMain=new TaskIntentMain();
            taskIntentMain.execute();

        }else if(hasOpen&account.length()<=2){
            layout.setBackground(Utility.getCuteedBkg(getResources().getDrawable(R.drawable.bkg_start),this,getWindowManager()));
            TaskIntentLogin taskIntentLogin=new TaskIntentLogin();
            taskIntentLogin.execute();

        }else{

            coordinatorLayout.setVisibility(View.VISIBLE);
            coordinatorLayout.addPage(R.layout.welcome1,R.layout.welcome2,R.layout.welcome3,R.layout.welcome4,R.layout.welcome5);
            btn_start=findViewById(R.id.as_btn_1);
            btn_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = getSharedPreferences(ConstValue.sp,MODE_PRIVATE).edit();
                    editor.putBoolean(ConstValue.hasOpen,true);
                    editor.apply();
                    Intent intent = new Intent(getApplicationContext(),ActivityLogin.class);
                    startActivity(intent);
                    //Log.e("GGG","跳转");
                    finish();
                }
            });
            iv_1=findViewById(R.id.welcome1_iv);
            iv_2=findViewById(R.id.welcome2_iv);
            iv_3=findViewById(R.id.welcome3_iv);
            iv_4=findViewById(R.id.welcome4_iv);
            iv_1.setImageDrawable(getResources().getDrawable(R.drawable.welcome1,null));
            iv_2.setImageDrawable(getResources().getDrawable(R.drawable.welcome2,null));
            iv_3.setImageDrawable(getResources().getDrawable(R.drawable.welcome4,null));
            iv_4.setImageDrawable(getResources().getDrawable(R.drawable.welcome3,null));
        }




    }

    private void initializeListeners() {
        coordinatorLayout.setOnPageScrollListener(new WelcomeCoordinatorLayout.OnPageScrollListener() {
            @Override
            public void onScrollPage(View v, float progress, float maximum) {
                if (!animationReady) {
                    animationReady = true;
                    backgroundAnimator.setDuration((long) maximum);
                }
                backgroundAnimator.setCurrentPlayTime((long) progress);
            }

            @Override
            public void onPageSelected(View v, int pageSelected) {

            }

        });
    }
    private void initializeBackgroundTransitions() {

        final Resources resources = getResources();
        final int colorPage1 = ResourcesCompat.getColor(resources, R.color.color1, getTheme());
        final int colorPage2 = ResourcesCompat.getColor(resources, R.color.color2, getTheme());
        final int colorPage3 = ResourcesCompat.getColor(resources, R.color.color3, getTheme());
        final int colorPage4 = ResourcesCompat.getColor(resources, R.color.color4, getTheme());
        final int colorPage5 = ResourcesCompat.getColor(resources, R.color.color5, getTheme());
        backgroundAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), colorPage1, colorPage2, colorPage3,colorPage4,colorPage5);
        backgroundAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                coordinatorLayout.setBackgroundColor((int) animation.getAnimatedValue());
            }

        });

    }



    class TaskIntentLogin extends AsyncTask<Void ,Integer ,Boolean  >{

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(ConstValue.thread_sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Intent intent = new Intent(getApplicationContext(),ActivityLogin.class);
            startActivity(intent);
            finish();
        }
    }

    class TaskIntentMain extends AsyncTask<Void ,Integer ,Boolean  >{

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Thread.sleep(ConstValue.thread_sleep);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Intent intent = new Intent(getApplicationContext(),ActivityMain.class);
            startActivity(intent);
            finish();
        }
    }


    /**
     * 检测权限是否开启
     */
    private void requestPermissons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> mPermissionList = new ArrayList<>();
            for (int i = 0; i < LOCATIONGPS.length; i++) {
                if (ContextCompat.checkSelfPermission(this, LOCATIONGPS[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(LOCATIONGPS[i]);//添加还未授予的权限
                }
            }
            //申请权限
            if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
                ActivityCompat.requestPermissions(this, LOCATIONGPS, requestPermissionsCode);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== requestPermissionsCode){
            if(grantResults[1]==PackageManager.PERMISSION_GRANTED){
                Log.e("GGG","授予读写权限成功");
            }else{
                Log.e("GGG","授予读写权限失败");
                Toast.makeText(this, "读写权限是为了图片读取，请手动给予", Toast.LENGTH_LONG).show();
            }
        }
    }

}
