package com.gaoch.brilliantpic;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
        exp.setAngle(180);
        tv_name.setText(getActivity().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getString(ConstValue.spUsername,"妙笔生画"));
        int exp= getActivity().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getInt(ConstValue.spExp,0);
        int exp_1=exp;
        int level=0;
        while (exp_1>0){
            exp_1=exp_1/10;
            level++;
        }
        tv_exp.setText("Lv."+level);
        String userpicname=getActivity().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getString(ConstValue.spUserPic,"");
        RequestOptions options = new RequestOptions().placeholder(R.drawable.user_pic).error(R.drawable.user_pic).centerCrop().dontAnimate();
        if(!userpicname.equals("")){
            Glide.with(getContext()).load(ConstValue.url_picUser(userpicname))
                    .apply(options)
                    .into(circleImageView);
        }
        setBlur();


//        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.font1);
//        tv_5.setTypeface(typeface);
//
//        titles=new ArrayList<>();
//        imageUrls=new ArrayList<>();
//        imageUrls.add("https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=199579668,662169770&fm=26&gp=0.jpg");
//        titles.add("这是第1张图片");
//        imageUrls.add("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2340194228,2284173225&fm=26&gp=0.jpg");
//        titles.add("这是第2张图片");
//        imageUrls.add("https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=582402591,1773722533&fm=26&gp=0.jpg");
//        titles.add("这是第3张图片");
//        imageUrls.add("https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=307943251,2816769785&fm=26&gp=0.jpg");
//        titles.add("这是第4张图片");
//        //添加轮播图片数据（图片数据不局限于网络图片、本地资源文件、View 都可以）,刷新数据也是调用该方法
//        mXBanner.setData(R.layout.item_banner,imageUrls,null);
//        mXBanner.loadImage(new XBanner.XBannerAdapter() {
//            @Override
//            public void loadBanner(XBanner banner, Object model, final View view, int position) {
//                String url = imageUrls.get(position);
//                RequestOptions options = new RequestOptions();
//                options.transforms(new CenterCrop(),new RoundedCorners(Utility.dp2px(getContext(),25))).error(R.drawable.background_allfround).placeholder(R.drawable.background_allfround);
//                Glide.with(getContext()).load(url).apply(options).into((ImageView) view.findViewById(R.id.banner_iv));
//
//            }
//        });
//
//
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "还未完成此功能", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getContext(), "还未完成此功能", Toast.LENGTH_SHORT).show();
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
//        layout_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),ActivityPostCard.class);
//                startActivity(intent);
//            }
//        });
//        layout_3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),ActivityMake.class);
//                startActivity(intent);
//            }
//        });
//        layout_4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),ActivityMake.class);
//                startActivity(intent);
//            }
//        });
//
//        onTouchListener = new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                int action = event.getActionMasked();
//                /* Raise view on ACTION_DOWN and lower it on ACTION_UP. */
//                switch (action) {
//                    case MotionEvent.ACTION_DOWN:
//                        v.setTranslationZ(Utility.dp2px(getContext(),-3));
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        v.setTranslationZ(Utility.dp2px(getContext(),0));
//                        break;
//                    default:
//                        return false;
//                }
//                return false;
//            }
//        };
//
//        layout_1.setOnTouchListener(onTouchListener);
//        layout_2.setOnTouchListener(onTouchListener);
//        layout_3.setOnTouchListener(onTouchListener);
//        layout_4.setOnTouchListener(onTouchListener);



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
            case 0:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();//图片的相对路径
                    Cursor cursor = getContext().getContentResolver().query(imageUri, null, null, null, null);//用ContentProvider查找选中的图片
                    cursor.moveToFirst();
                    final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//获取图片的绝对路径
                    Log.d("GGG", path);
                    //FrameLayout layout = findViewById(R.id.main_fragment);
                    //layout.setBackground(Drawable.createFromPath(path));
                    cursor.close();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 按照最大范围来放大图片
     * @param bitmap
     * @return
     */
    public  Drawable ReSizePic(Bitmap bitmap, int maxHeight, int maxWidth, Context context){
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
                    int exp= user.getExp();
                    int level=0;
                    while (exp>0){
                        exp=exp/10;
                        level++;
                    }
                    tv_exp.setText("Lv."+level);
//                    ((ActivityMain)getActivity()).changeVarHead();
//                    RequestOptions options = new RequestOptions().placeholder(R.drawable.user_pic).error(R.drawable.user_pic).centerCrop();
//                    String userpicname=getActivity().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getString(ConstValue.spUsername,"");
//                    if(!userpicname.equals("")){
//                        Glide.with(getContext()).load(ConstValue.url_picUser(userpicname))
//                                .apply(options)
//                                .listener(new RequestListener<Drawable>() {
//                                    @Override
//                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//
//                                        return false;
//                                    }
//                                    @Override
//                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                        return false;
//                                    }
//                                }).into(circleImageView);
//                    }

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


    public void setBlur(){
        final LinearLayout bkg_layout=((ActivityMain)getActivity()).layout_bkg;
        blurLayout1=new Blur.BlurLayout(layout_1,bkg_layout);
        blurLayout2=new Blur.BlurLayout(layout_2,bkg_layout);
        blurLayout3=new Blur.BlurLayout(layout_3,bkg_layout);
        blurLayout4=new Blur.BlurLayout(layout_4,bkg_layout);
    }
}
