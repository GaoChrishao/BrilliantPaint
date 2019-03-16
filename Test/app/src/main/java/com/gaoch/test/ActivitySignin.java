package com.gaoch.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gaoch.test.myclass.User;
import com.gaoch.test.util.ConstValue;
import com.gaoch.test.util.HttpUtil;
import com.gaoch.test.util.Utility;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ActivitySignin extends Activity implements View.OnClickListener {

    private Button btn_signup;
    private EditText edit_username;
    private EditText edit_password,edit_password_re;
    private ProgressDialog progressDialog;

    private final int msg_login=1;
    private final int msg_signup=2;
    private final int msg_close=3;

    private boolean decideSignUp=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_signin);
        initView();


    }

    private void initView(){
        btn_signup=findViewById(R.id.signin_btn_signin);
        edit_username =findViewById(R.id.signin_username);
        edit_password=findViewById(R.id.signin_password);
        edit_password_re=findViewById(R.id.signin_password_re);
        btn_signup.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.signin_btn_signin:
                String username= edit_username.getText().toString()+"";
                String password_1=edit_password.getText().toString()+"";
                String password_2=edit_password.getText().toString()+"";
                if(username.length()>ConstValue.max_length_username||username.length()<ConstValue.min_length_username){
                    Toast.makeText(this, "用户名长度必须不大于"+ConstValue.max_length_username+"位，不小于"+ConstValue.min_length_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password_1.equals(password_2)){
                    Toast.makeText(this, "两次输入密码必须相同", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password_1.length()>ConstValue.max_length_password||password_1.length()<ConstValue.min_length_password){
                    Toast.makeText(this, "密码长度必须不大于"+ConstValue.max_length_password+"位，不小于"+ConstValue.min_length_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                trySignup(username,password_1);
                showProgressDialog();
                break;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstValue.noInternet:
                    closeProgressDialog();
                    Toast.makeText(ActivitySignin.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
                    break;
                case msg_signup:
                    Bundle bundle1 = msg.getData();
                    User user1= (User) bundle1.getSerializable(ConstValue.bundle_user);
                    if(user1.getId()==0){
                        Toast.makeText(ActivitySignin.this, "注册失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ActivitySignin.this, "注册成功，您的账号为:"+user1.getAccount(), Toast.LENGTH_LONG).show();
                    SharedPreferences.Editor editor1 = getSharedPreferences(ConstValue.sp,MODE_PRIVATE).edit();
                    editor1.putString(ConstValue.spUsername,user1.getUsername());
                    editor1.putString(ConstValue.spPassword,user1.getPassword());
                    editor1.putString(ConstValue.spAccount,user1.getAccount()+"");
                    editor1.apply();
                    closeProgressDialog();
                    startActivity(new Intent(ActivitySignin.this,ActivityMain.class));
                    finish();
                    break;
                case msg_close:
                    Toast.makeText(ActivitySignin.this, "服务器暂时关闭", Toast.LENGTH_SHORT).show();
                    closeProgressDialog();
                        break;
            }
        }
    };




    public void trySignup(String username,String pwd){
        String address=ConstValue.url_signup(username,pwd);
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what=ConstValue.noInternet;
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                System.out.println(responseText);
                User user =null;
                try{
                    user = new Gson().fromJson(responseText,User.class);
                }catch (Exception e){
                    e.printStackTrace();
                    handler.sendEmptyMessage(msg_close);
                }
                if(user!=null){
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(ConstValue.bundle_user,user);
                    msg.setData(bundle);
                    msg.what=msg_signup;
                    handler.sendMessage(msg);
                }

            }
        });
    }





    /**
     * 显示进度对话框
     */
    private void showProgressDialog(){
        if(progressDialog==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("注册中...");
        }
        progressDialog.show();
    }


    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }



}
