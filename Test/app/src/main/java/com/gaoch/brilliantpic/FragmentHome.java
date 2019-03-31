package com.gaoch.brilliantpic;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gaoch.brilliantpic.myclass.User;
import com.gaoch.brilliantpic.myview.CircleExp;
import com.gaoch.brilliantpic.util.Blur;
import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.HttpUtil;
import com.google.gson.Gson;
import com.stx.xhb.xbanner.XBanner;

import java.io.File;
import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class FragmentHome extends Fragment {
    private LinearLayout layout_1,layout_2,layout_3,layout_4;
    private XBanner mXBanner;
    private List<String>imageUrls;
    private List<String>titles;
    private Button btn_strange, btn_oil,btn_ink,btn_building,btn_cartoon,btn_water,btn_face,btn_scene
            ,btn_camera,btn_postcard,btn_video;
    private TextView tv_5;
    private View.OnTouchListener onTouchListener;
    private CircleImageView circleImageView;
    private CircleExp exp;
    private TextView tv_name,tv_exp;
    private final int msg_login=1;
    private final int msg_signup=2;
    private final int msg_close=3;
    private final int msg_noAccount=4;

    // 拍照回传码
    public final static int REQUEST_CAMERA = 11;



    private Blur.BlurLayout blurLayout1,blurLayout2,blurLayout3,blurLayout4;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_home,container,false);

        circleImageView=view.findViewById(R.id.fragment_main_user_pic);
        exp=view.findViewById(R.id.fragment_main_exp);
        tv_name=view.findViewById(R.id.fragment_main_tv_name);
        tv_exp=view.findViewById(R.id.fragment_main_tv_exp);

        layout_1=view.findViewById(R.id.main_layout1);
        layout_2=view.findViewById(R.id.main_layout2);
        layout_3=view.findViewById(R.id.main_layout3);
        layout_4=view.findViewById(R.id.main_layout4);

        btn_strange =view.findViewById(R.id.fragment_main_btn_strange);
        btn_oil =view.findViewById(R.id.fragment_main_btn_oil);
        btn_building=view.findViewById(R.id.fragment_main_btn_building);
        btn_cartoon=view.findViewById(R.id.fragment_main_btn_catroon);
        btn_water =view.findViewById(R.id.fragment_main_btn_water);
        btn_ink =view.findViewById(R.id.fragment_main_btn_ink);
        btn_face=view.findViewById(R.id.fragment_main_btn_face);
        btn_scene=view.findViewById(R.id.fragment_main_btn_scene);
        btn_camera=view.findViewById(R.id.fragment_main_btn_camera);
        btn_video=view.findViewById(R.id.fragment_main_btn_video);
        btn_postcard=view.findViewById(R.id.fragment_main_btn_postcard);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_name.setText(getActivity().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getString(ConstValue.spUsername,"妙笔生画"));
        int expAll= getActivity().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getInt(ConstValue.spExp,0);
        int exp_index=expAll;
        int level=0;
        int thisLevelExp=1;
        while (exp_index>thisLevelExp){
            exp_index-=thisLevelExp;
            level++;
            thisLevelExp*=ConstValue.expLevel;
        }

        int angle=(int)((exp_index+0.0)/thisLevelExp*360);
        exp.setAngle(angle+90);
        tv_exp.setText("Lv."+level);

        Log.e("GGG","all:"+expAll+" expIndex:"+exp_index+" thisLEvelNEdd:"+thisLevelExp+"  engle:"+angle);



        String userpicname=getActivity().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getString(ConstValue.spUserPic,"");
        RequestOptions options = new RequestOptions().placeholder(R.drawable.user_pic).error(R.drawable.user_pic).centerCrop().dontAnimate();
        if(!userpicname.equals("")){
            Glide.with(getContext()).load(ConstValue.url_picUser(userpicname))
                    .apply(options)
                    .into(circleImageView);
        }
        setBlur();

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();

            }
        });

        btn_postcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "还未完成此功能", Toast.LENGTH_SHORT).show();
            }
        });


        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ActivityVideo.class);
                startActivity(intent);
            }
        });



        btn_strange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ActivityMake.class);
                intent.putExtra(ConstValue.key_makeType,ConstValue.type_make_strange);
                startActivity(intent);
            }
        });

        btn_scene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ActivityMake.class);
                intent.putExtra(ConstValue.key_makeType,ConstValue.type_make_scene);
                startActivity(intent);
            }
        });
        btn_face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ActivityMake.class);
                intent.putExtra(ConstValue.key_makeType,ConstValue.type_make_face);
                startActivity(intent);
            }
        });
        btn_cartoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ActivityMake.class);
                intent.putExtra(ConstValue.key_makeType,ConstValue.type_make_cartoon);
                startActivity(intent);
            }
        });
        btn_building.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ActivityMake.class);
                intent.putExtra(ConstValue.key_makeType,ConstValue.type_make_building);
                startActivity(intent);
            }
        });
        btn_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ActivityMake.class);
                intent.putExtra(ConstValue.key_makeType,ConstValue.type_make_water);
                startActivity(intent);
            }
        });
        btn_oil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ActivityMake.class);
                intent.putExtra(ConstValue.key_makeType,ConstValue.type_make_oil);
                startActivity(intent);
            }
        });
        btn_ink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ActivityMake.class);
                intent.putExtra(ConstValue.key_makeType,ConstValue.type_make_ink);
                startActivity(intent);
            }
        });



    }

    @Override
    public void onResume() {
        super.onResume();
        tryLogin();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mXBanner.stopAutoPlay();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA:
                if(resultCode==RESULT_OK){
                    Toast.makeText(getContext(), "拍照成功！", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(),ActivityMake.class);
                    intent.putExtra(ConstValue.key_makeType,ConstValue.type_make_camera);
                    startActivity(intent);

                }else{
                    Toast.makeText(getContext(), "拍照失败", Toast.LENGTH_SHORT).show();

            }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ConstValue.noInternet:
                    if(getContext()!=null){
                        Toast.makeText(getContext(),"网络连接失败！",Toast.LENGTH_SHORT).show();
                    }

                    break;
                case msg_login:
                    Bundle bundle = msg.getData();
                    User user= (User) bundle.getSerializable(ConstValue.bundle_user);
                    if(user.getId()==0){
                        if(getContext()!=null){
                            Toast.makeText(getContext(),"登入失败,账号或密码出错!",Toast.LENGTH_SHORT).show();
                        }
                        getActivity().startActivity(new Intent(getActivity(),ActivityLogin.class));
                        getActivity().finish();
                        return;
                    }
                    //Toast.makeText(getContext(), "欢迎回来："+user.getUsername(), Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = getContext().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).edit();
                    editor.putString(ConstValue.spUsername,user.getUsername());
                    editor.putString(ConstValue.spPassword,user.getPassword());
                    editor.putLong(ConstValue.spAccount,user.getAccount());
                    editor.putInt(ConstValue.spExp,user.getExp());
                    editor.putString(ConstValue.spUserPic,user.getUserpic());
                    editor.apply();
                    tv_name.setText(user.getUsername());

                    int expAll= user.getExp();
                    int exp_index=expAll;
                    int level=0;
                    int thisLevelExp=1;
                    while (exp_index>thisLevelExp){
                        exp_index-=thisLevelExp;
                        level++;
                        thisLevelExp*=ConstValue.expLevel;
                    }
                    exp.setAngle((int)((exp_index+0.0)/thisLevelExp*360)+90);
                    tv_exp.setText("Lv."+level);



                    break;
                case msg_close:
                    if(getContext()!=null){
                        Toast.makeText(getContext(),"服务器暂时关闭",Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };

    public void tryLogin(){
        long account=getContext().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getLong(ConstValue.spAccount,-1);
        String pwd=getContext().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getString(ConstValue.spPassword,"0");
        String address=ConstValue.url_login(String.valueOf(account),pwd);
        Log.e("GGG",address);
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


    private void takePhoto(){
        // android 7.0系统解决拍照的问题
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

//
//        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(ConstValue.getTmpPicPath(getContext()))));
//        startActivityForResult(intentToTakePhoto, REQUEST_CAMERA);



        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {

            File file = new File(ConstValue.picPath, "tmp.jpg");
            String mCurrentPhotoPath = file.getAbsolutePath();
            Log.e("GGG","path:"+mCurrentPhotoPath);

            Uri fileUri;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                fileUri = FileProvider.getUriForFile(getContext(), "com.gaoch.brilliantpic.fileprovider", file);
            }else{
                fileUri=Uri.fromFile(file);
            }
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        }





    }



    public void setBlur(){
        final LinearLayout bkg_layout=((ActivityMain)getActivity()).layout_bkg;
        blurLayout1=new Blur.BlurLayout(layout_1,bkg_layout);
        blurLayout2=new Blur.BlurLayout(layout_2,bkg_layout);
        blurLayout3=new Blur.BlurLayout(layout_3,bkg_layout);
        blurLayout4=new Blur.BlurLayout(layout_4,bkg_layout);
    }
}
