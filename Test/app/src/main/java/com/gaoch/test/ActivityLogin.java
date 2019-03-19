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
import android.widget.TextView;
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


public class ActivityLogin extends Activity implements View.OnClickListener {

    private Button btn_login;
    private TextView tv_signup;
    private EditText edit_account;
    private EditText edit_password;
    private ProgressDialog progressDialog;

    private final int msg_login=1;
    private final int msg_signup=2;
    private final int msg_close=3;

    private boolean decideSignUp=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_login);
        initView();


    }

    private void initView(){
        btn_login=findViewById(R.id.login_btn_login);
        tv_signup =findViewById(R.id.login_tv_signin);
        edit_account =findViewById(R.id.editText_account);
        edit_password=findViewById(R.id.editText_password);
        btn_login.setOnClickListener(this);
        tv_signup.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences(ConstValue.sp,MODE_PRIVATE);
        String account= String.valueOf(preferences.getLong(ConstValue.spAccount,-1));
        String pwd=preferences.getString(ConstValue.spPassword,"");
        if(!account.equals("-1")){
            edit_account.setText(account);
            edit_password.setText(pwd);
        }

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.login_btn_login:
                String account= edit_account.getText().toString()+"";
                String password=edit_password.getText().toString()+"";
                //此处执行登入操作
                if(account.length()<=0||password.length()<=0){
                    Toast.makeText(this, "请填写完整账户和密码!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!Utility.isInteger(account)){
                    Toast.makeText(this, "请输入合法账号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressDialog();
                tryLogin(account,password);
                break;
            case R.id.login_tv_signin:
                Intent intent=new Intent(this,ActivitySignin.class);
                startActivity(intent);
                finish();
                break;
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstValue.noInternet:
                    closeProgressDialog();
                    Toast.makeText(ActivityLogin.this,"网络连接失败！",Toast.LENGTH_SHORT).show();
                    break;
                case msg_login:
                    closeProgressDialog();
                    Bundle bundle = msg.getData();
                    User user= (User) bundle.getSerializable(ConstValue.bundle_user);
                    if(user.getId()==0){
                        Toast.makeText(ActivityLogin.this, "登入失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(ActivityLogin.this, "欢迎回来："+user.getUsername(), Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getSharedPreferences(ConstValue.sp,MODE_PRIVATE).edit();
                    editor.putString(ConstValue.spUsername,user.getUsername());
                    editor.putString(ConstValue.spPassword,user.getPassword());
                    editor.putLong(ConstValue.spAccount,user.getAccount());
                    editor.putInt(ConstValue.spExp,user.getExp());
                    editor.putString(ConstValue.spUserPic,user.getUserpic());
                    editor.apply();
                    startActivity(new Intent(ActivityLogin.this,ActivityMain.class));
                    closeProgressDialog();
                    finish();
                    break;
                case msg_close:
                    Toast.makeText(ActivityLogin.this, "服务器暂时关闭", Toast.LENGTH_SHORT).show();
                    closeProgressDialog();
                        break;
            }
        }
    };




    public void tryLogin(String account,String pwd){
        String address=ConstValue.url_login(account,pwd);
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
                User user=null;
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
                    msg.what=msg_login;
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
            progressDialog.setMessage("请稍后...");
            //progressDialog.setCanceledOnTouchOutside(false);
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
