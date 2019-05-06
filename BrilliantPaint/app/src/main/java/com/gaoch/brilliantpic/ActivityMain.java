package com.gaoch.brilliantpic;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gaoch.brilliantpic.adapter.LocalDatabaseHelper;
import com.gaoch.brilliantpic.util.BlurSingle;
import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.HttpUtil;
import com.gaoch.brilliantpic.util.ImageUtil;
import com.gaoch.brilliantpic.util.Utility;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.gaoch.brilliantpic.util.ConstValue.LOCATIONGPS;


public class ActivityMain extends Activity {
    public DrawerLayout drawer;
    private Button btn_drawer;
    private NavigationView navigationView;
    public LinearLayout layout_bkg;
    private LinearLayout actionBarLayout;
    private LocalDatabaseHelper dbHelper;


    private final int requestPermissionsCode = 100;//权限请求码

    private final int msg_noInternet=1;
    private final int msg_getAllStyles=2;
    private final int msg_getAllMyPic=3;
    boolean hasOpen=false;
    private int primaryColor=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_main);
        initView();

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) actionBarLayout.getLayoutParams();
        lp.setMargins(0, Utility.getStatusBarHeight(getApplicationContext()), 0, 0);
        File bkgFile=new File(ConstValue.getBkgPath(getApplicationContext()));
        if(bkgFile.exists()&&bkgFile.isFile()){
            layout_bkg.setBackground(Drawable.createFromPath(ConstValue.getBkgPath(getApplicationContext())));
        }else{
            layout_bkg.setBackground(Utility.getCuteedBkg(getResources().getDrawable(R.drawable.bkg_2),this,getWindowManager()));
        }

        File bkgFile1=new File(ConstValue.getBkg_blurPath(getApplicationContext()));
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        if(bkgFile1.exists()&&bkgFile1.isFile()){
            BlurSingle.initBkgWithResieze(layout_bkg,Utility.LoadLocalBitmap(ConstValue.getBkg_blurPath(getApplicationContext())),width,height);
            primaryColor = ImageUtil.colorFromBitmap( BitmapFactory.decodeFile(ConstValue.getBkg_blurPath(getApplicationContext())));
        }


        dbHelper=new LocalDatabaseHelper(this,ConstValue.LocalDatabaseName,null,LocalDatabaseHelper.NEW_VERSION);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.close();
        tryGetStyles();
        requestPermissons();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeVarHead();
        if(hasOpen==false){
            hasOpen=true;
        }else{
            hasOpen=false;
            tryGetUserfile();
        }
    }

    private void initView(){
        //Utility.setFullScreen_origin(getWindow());


        actionBarLayout = findViewById(R.id.my_bar);
        layout_bkg=findViewById(R.id.bkg_layout);
        drawer = findViewById(R.id.drawer_layout);
        btn_drawer = findViewById(R.id.nav_button);
        btn_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDrawer();
            }
        });


        navigationView = findViewById(R.id.navigationview);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    FragmentManager manager=getFragmentManager();
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.replace(R.id.main_frame,new FragmentHome());
                    transaction.commit();

                } else if (id == R.id.nav_my) {
                    FragmentManager manager=getFragmentManager();
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.replace(R.id.main_frame,new FragmentMine());
                    transaction.commit();
                } else if (id == R.id.nav_view) {
                    FragmentManager manager=getFragmentManager();
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.replace(R.id.main_frame,new FragmentLook());
                    transaction.commit();
                }
                else if (id == R.id.nav_exit){
                    SharedPreferences.Editor editor = getSharedPreferences(ConstValue.sp,MODE_PRIVATE).edit();
                    editor.putString(ConstValue.spUsername,"");
                    editor.putString(ConstValue.spPassword,"");
                    editor.putLong(ConstValue.spAccount,-1);
                    editor.apply();
                    Intent intent = new Intent(ActivityMain.this,ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }
               else if (id == R.id.nav_about) {
                    FragmentManager manager=getFragmentManager();
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.replace(R.id.main_frame,new FragmentAbout());
                    transaction.commit();
                }else if(id==R.id.nav_setting){
                    FragmentManager manager=getFragmentManager();
                    FragmentTransaction transaction=manager.beginTransaction();
                    transaction.replace(R.id.main_frame,new FragmentTheme());
                    transaction.commit();
                }
                drawer.closeDrawers();
                return true;
            }
        });



        //将R.id.main_frame替换为FragmentMain
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.main_frame,new FragmentHome());
        transaction.commit();


    }

    public void showDrawer(){
        drawer.openDrawer(GravityCompat.START);
    }

    /**
     * 修改var_head的内容
     */
    public void changeVarHead() {
        SharedPreferences preferences = getSharedPreferences(ConstValue.sp,MODE_PRIVATE);
        Long account= preferences.getLong(ConstValue.spAccount,-1);
        String name= preferences.getString(ConstValue.spUsername,"用户名");
        String userpicname=preferences.getString(ConstValue.spUserPic,"");
        Log.e("GGG","account:"+account+" username:"+name);
        View headerView = navigationView.getHeaderView(0);
        TextView textview = (TextView) headerView.findViewById(R.id.nav_username);
        textview.setText(name);
        TextView textView1 = headerView.findViewById(R.id.nav_account);
        textView1.setText(account+"");
        CircleImageView userPic=headerView.findViewById(R.id.nav_userpic);

        if(!userpicname.equals("")){
            RequestOptions options = new RequestOptions().placeholder(R.drawable.user_pic).error(R.drawable.user_pic).centerCrop().dontAnimate();
            Glide.with(this).load(ConstValue.url_picUser(userpicname))
                    .apply(options)
                    .into(userPic);
        }


        Log.e("MainActivity",primaryColor+"");
        if(primaryColor!=0){
            LinearLayout layout = headerView.findViewById(R.id.nav_header);
            layout.setBackground(Utility.getCuteedBkg(ConstValue.getBkg_blurPath(this),this,130,200));
            int[] colors = new int[]{ primaryColor,getResources().getColor(R.color.uncheckedColor)};
            int[][] states = new int[][]{
                    new int[]{ android.R.attr.state_checked},
                    new int[]{-android.R.attr.state_checked}
            };
            ColorStateList csl = new ColorStateList(states,colors);
            navigationView.setItemIconTintList(csl);
            navigationView.setItemTextColor(csl);
        }

    }

    /**
     * 修改var_head的内容
     */
    public void updateUserPic(String userpic) {
        Log.e("GGG","更新navView");
        //SharedPreferences preferences = getSharedPreferences(ConstValue.sp,MODE_PRIVATE);
        View headerView = navigationView.getHeaderView(0);
        final CircleImageView userPic=headerView.findViewById(R.id.nav_userpic);
        //String userpicname=preferences.getString(ConstValue.spUserPic,"");
        if(userPic!=null&&(!userPic.equals(""))){
            RequestOptions options = new RequestOptions().placeholder(R.drawable.user_pic).error(R.drawable.user_pic).centerCrop().dontAnimate();
            Glide.with(this).load(ConstValue.url_picUser(userpic))
                    .apply(options)
                   .into(userPic);
        }


    }


    /**
     * 获取最新的style信息
     */
    public void tryGetStyles(){
        String address=ConstValue.url_StyleAll;
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
                System.out.println(responseText);
                LocalDatabaseHelper.proStyles(responseText,dbHelper.getWritableDatabase());
                Message msg = new Message();
                msg.what=msg_getAllStyles;
                handler.sendMessage(msg);
            }
        });
    }

    /**
     * 获取自己已经做过的全部图片信息
     */
    public void tryGetUserfile(){
        String address=ConstValue.url_userfiles(String.valueOf(getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getLong(ConstValue.spAccount,-1)));
        Log.e("GGG",address);
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
                System.out.println(responseText);
                if(!(responseText==null)&&!(responseText.length()<1)){
                    LocalDatabaseHelper.proUserfiles(responseText,dbHelper.getWritableDatabase());
                }


                Message msg = new Message();
                msg.what=msg_getAllMyPic;
                handler.sendMessage(msg);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstValue.noInternet:
                    Toast.makeText(ActivityMain.this, "无法获取最新的模板列表！", Toast.LENGTH_SHORT).show();
                    break;
                case msg_getAllStyles:
                    Log.e("GGG","成功获取最新的模板列表！");
                    //Toast.makeText(ActivityMain.this, "成功获取最新的模板列表！", Toast.LENGTH_SHORT).show();
                    tryGetUserfile();
                    break;
                case msg_getAllMyPic:
                    //Toast.makeText(ActivityMain.this, "获取！", Toast.LENGTH_SHORT).show();
                    Log.e("GGG","更新自己的图片");
                    break;

            }
        }
    };

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



}
