package com.gaoch.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gaoch.test.adapter.ChoseMakeAdapter;
import com.gaoch.test.adapter.LocalDatabaseHelper;
import com.gaoch.test.myclass.FileMessage;
import com.gaoch.test.myclass.Style;
import com.gaoch.test.myview.ShadowImageView;
import com.gaoch.test.myview.ViewDialogFragment;
import com.gaoch.test.util.Blur;
import com.gaoch.test.util.ConstValue;
import com.gaoch.test.util.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ActivityMake extends Activity {
    public  volatile String localpicname;
    private Drawable pic_show;  //缩放后显示的图片
    private Drawable pic_after;
    private Bitmap pic;  //原图
    private boolean hasPic=false;
    private ImageView iv_add;
    private ShadowImageView iv_pic;
    private LinearLayout ll_bottom,layout_bkg; //选择风格种类的lineaLayout,与选择
    private RecyclerView recyclerView;
    private Button btn_yes,btn_no;
    private ChoseMakeAdapter choseMakeAdapter;
    private LinearLayoutManager layoutManager;
    private int prePosition=-1,nowPosition=-1;
    private static final int requestCode_chosePic=001;
    private static final int requestCode_cropPic=003;
    private static final int msg_finishUploadPic=002;
    private static final int msg_wrong=003;
    private static final int msg_wait=004;
    private static final int msg_toolarge=005;

    private ProgressDialog progressDialog;
    private LocalDatabaseHelper dbHelper;
    private List<Style>styleList;
    private List<Boolean> checkList;
    private String makeType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);


        setContentView(R.layout.activity_make);
        iv_add=findViewById(R.id.activity_make_iv_add);
        iv_pic=findViewById(R.id.activity_make_iv);
        recyclerView=findViewById(R.id.activity_make_rv);
        btn_yes=findViewById(R.id.activity_make_btn_yes);
        btn_no=findViewById(R.id.activity_make_btn_no);
        ll_bottom=findViewById(R.id.activity_make_ll_buttom);
        layout_bkg=findViewById(R.id.activity_make_layuot_bkg);





        iv_pic.setVisibility(View.GONE);
        ll_bottom.setVisibility(View.GONE);
        makeType=getIntent().getStringExtra(ConstValue.key_makeType);
        if(makeType==null||makeType.equals("")){
            makeType=ConstValue.type_make_strange;
        }
        initData();
        setClickEvent();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_bottom.getLayoutParams();
        lp.setMargins(0, 0, 0, Utility.getNavigationHeight(this));
        File bkgFile=new File(ConstValue.getBkgPath(getApplicationContext()));
        if(bkgFile.exists()&&bkgFile.isFile()){
            layout_bkg.setBackground(Drawable.createFromPath(ConstValue.getBkg_blurPath(getApplicationContext())));
        }else{
            layout_bkg.setBackground(new BitmapDrawable(getResources(), Blur.bkg));
        }
    }

    void setClickEvent(){
        choseMakeAdapter = new ChoseMakeAdapter(getApplicationContext(),styleList);
        choseMakeAdapter.setOnItemClickListener(new ChoseMakeAdapter.mOnItemClickListener() {
            @Override
            public void onClick(ChoseMakeAdapter.ViewHolder viewHolder, int position) {
                if(choseMakeAdapter.checkList.get(position)){
                    //之前选中了
                    nowPosition=-1;
                    viewHolder.relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorUnChoseLines));
                    choseMakeAdapter.checkList.set(position,false);
                    //放小动画
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.view_scale_larger_r);
                    viewHolder.itemView.startAnimation(animation);
                  //  Log.e("GGG","取消颜色:"+position);
                }else{
                    //之前未选中
                    choseMakeAdapter.checkList.set(position,true);
                    viewHolder.relativeLayout.setBackground(getResources().getDrawable(R.drawable.background_yuanjiao_white));

                    //放大动画
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.view_scale_larger);
                    viewHolder.itemView.startAnimation(animation);

                 //   Log.e("GGG","选中:"+position);
                    if(nowPosition!=-1){
                        //将之前选过的去除选中状态
                        if(nowPosition>=layoutManager.findFirstVisibleItemPosition()&&nowPosition<=layoutManager.findLastVisibleItemPosition()){
                            ChoseMakeAdapter.ViewHolder holder= (ChoseMakeAdapter.ViewHolder) recyclerView.findViewHolderForAdapterPosition(nowPosition);
                            holder.relativeLayout.setBackgroundColor(getResources().getColor(R.color.colorUnChoseLines));
                            //放小动画
                            Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.view_scale_larger_r);
                            holder.itemView.startAnimation(animation1);
                           // Log.e("GGG","取消颜色:"+nowPosition);
                        }else{
                            choseMakeAdapter.notifyItemChanged(nowPosition);
                        }
                        choseMakeAdapter.checkList.set(nowPosition,false);
                    }
                    nowPosition=position;

                }
                //Log.e("GGG","当前状态:"+choseMakeAdapter.checkList.get(0)+" "+choseMakeAdapter.checkList.get(1)+" "+choseMakeAdapter.checkList.get(2)+" "+choseMakeAdapter.checkList.get(3)+" "+choseMakeAdapter.checkList.get(4));

            }
        });
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(choseMakeAdapter);
        recyclerView.setItemViewCacheSize(0);


        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("GGG","点击添加");
                choosePic();
            }
        });
        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("GGG","图片");
                choosePic();
            }
        });

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更具选择的图片执行上传操作
                if(progressDialog==null){
                    progressDialog = new ProgressDialog(ActivityMake.this);
                    progressDialog.setMessage("上传中");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
                Log.e("GGG","显示");
                progressDialog.show();
                //Utility.showProgressDialog(progressDialog,ActivityMake.this,"上传中......");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            File file=new File(ConstValue.filePath+"tmp.jpg");
                            if(!file.exists()){
                                Utility.saveBitmapToFile(pic,ConstValue.pic_quality,"tmp","jpg");
                            }
                            SharedPreferences sp = getSharedPreferences(ConstValue.sp,MODE_PRIVATE);
                            FileMessage fileMessage=Utility.uploadLogFile(
                                    getApplicationContext(),
                                    ConstValue.serverIp+"uploadFile?useraccount="+sp.getLong(ConstValue.spAccount,-1)+"&modelname="+styleList.get(nowPosition).getModelname(),
                                    ConstValue.filePath+"tmp.jpg"
                                     );
                            if(file.exists())file.delete();
                            switch (fileMessage.getStatus()){
                                case FileMessage.status_wait:
                                    handler.sendEmptyMessage(msg_wait);
                                    break;
                                case FileMessage.status_ok:
                                    Message msg = new Message();
                                    msg.what=msg_finishUploadPic;
                                    Bundle bundle =new Bundle();
                                    bundle.putSerializable(ConstValue.key_fileMessage,fileMessage);
                                    msg.setData(bundle);
                                    handler.sendMessage(msg);
                                    break;
                                case FileMessage.status_toolarge:
                                    handler.sendEmptyMessage(msg_toolarge);
                                    break;
                                case FileMessage.status_wrong:
                                    handler.sendEmptyMessage(msg_wrong);
                                    break;
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                            handler.sendEmptyMessage(msg_wrong);
                        }

                    }
                }).start();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nowPosition!=-1){
                    choseMakeAdapter.checkList.set(nowPosition,false);
                    choseMakeAdapter.notifyItemChanged(nowPosition);
                    nowPosition=-1;
                }else if(nowPosition==-1){
                    hasPic=false;
                    ll_bottom.setVisibility(View.GONE);
                    iv_pic.setVisibility(View.GONE);
                    iv_add.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    void initData(){
        styleList=new ArrayList<>();
        checkList=new ArrayList<>();
        dbHelper=new LocalDatabaseHelper(this,ConstValue.LocalDatabaseName,null,LocalDatabaseHelper.NEW_VERSION);
        styleList=LocalDatabaseHelper.getStyles(makeType,dbHelper.getReadableDatabase());
        for(int i=0;i<styleList.size();i++){
            checkList.add(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!hasPic){
            iv_pic.setVisibility(View.INVISIBLE);
            iv_add.setVisibility(View.VISIBLE);
        }else{
            iv_add.setVisibility(View.GONE);
            iv_pic.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case requestCode_chosePic:
                if(resultCode==RESULT_OK){
                    iv_pic.setVisibility(View.VISIBLE);
                    Uri imageUri = data.getData();//图片的相对路径
                    Cursor cursor = getContentResolver().query(imageUri, null, null, null, null);//用ContentProvider查找选中的图片
                    cursor.moveToFirst();
                    final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//获取图片的绝对路径



                    Intent intent = new Intent(this, ActivityCrop.class);
                    intent.putExtra(ConstValue.key_imageUrl,path);
                    intent.putExtra(ConstValue.key_imageCropX,ConstValue.pic_size_postCard_x);
                    intent.putExtra(ConstValue.key_imageCropY,ConstValue.pic_size_postCard_y);
                    startActivityForResult(intent,requestCode_cropPic);
                    Log.e("GGG","选择图片");
                }else{
                    Log.e("GGG","取消选择图片");
                }
                break;

            case requestCode_cropPic:
                if(resultCode==ActivityCrop.RESULT_OK){
                    String path=data.getStringExtra(ConstValue.key_imageUrl);
                    Log.e("GGG",path);
                    BitmapDrawable bd=(BitmapDrawable)Drawable.createFromPath(path);
                    pic=bd.getBitmap();
                    pic_show = Utility.ReSizePic(pic,(int)(iv_pic.getMeasuredHeight()),(int)(iv_pic.getMeasuredWidth()),this);
                    hasPic=true;
                    iv_add.setVisibility(View.GONE);
                    iv_pic.setVisibility(View.VISIBLE);
                    iv_pic.setImageDrawable(pic_show);
                    ll_bottom.setVisibility(View.VISIBLE);
                }
                break;

        }
    }



    /**
     * 选择图片
     */
    public void choosePic() {
        Intent intent_choose = new Intent(Intent.ACTION_PICK);//Intent.ACTION_GET_CONTENT和是获得最近使用过的图片。
        intent_choose.setType("image/*");//应该是指定数据类型是图片。
        startActivityForResult(intent_choose, requestCode_chosePic);
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case msg_finishUploadPic:
                    final FileMessage fileMessage = (FileMessage) msg.getData().getSerializable(ConstValue.key_fileMessage);
                    Log.e("GGG",fileMessage.getFileList().get(0).getUrlpath());
                    localpicname=Utility.getFileName(fileMessage.getFileList().get(0).getUrlpath());
                    if(progressDialog!=null){
                        progressDialog.setMessage("处理成功，正在下载...");
                    }
                    RequestOptions options1 = new RequestOptions().centerCrop().dontAnimate();
                    Glide.with(getApplicationContext()).load(fileMessage.getFileList().get(0).getUrlpath()).apply(options1).into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            pic=((BitmapDrawable)resource).getBitmap();
                            pic_show = Utility.ReSizePic(pic,iv_pic.getMeasuredHeight(),iv_pic.getMeasuredWidth(),getApplicationContext());
                            iv_pic.setImageDrawable(pic_show);
                            new MyTask().execute(resource);
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            if(progressDialog!=null){progressDialog.dismiss();progressDialog=null;}
                            Toast.makeText(ActivityMake.this, "下载失败!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                case msg_wrong:
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                        progressDialog=null;
                    }

                    //Utility.closeProgressDialog(progressDialog);
                    Toast.makeText(ActivityMake.this, "出错！", Toast.LENGTH_SHORT).show();
                    break;
                case msg_wait:
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                        progressDialog=null;
                    }
                    //Utility.closeProgressDialog(progressDialog);
                    Toast.makeText(ActivityMake.this, "请求人数太多，请稍后再试!", Toast.LENGTH_SHORT).show();
                    break;
                case msg_toolarge:
                    if(progressDialog!=null){
                        progressDialog.dismiss();
                        progressDialog=null;
                    }
                    //Utility.closeProgressDialog(progressDialog);
                    Toast.makeText(ActivityMake.this, "上传图片过大！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };





    //保存图片到文件
    class MyTask extends AsyncTask<Drawable,Void, Boolean>{
        @Override
        protected Boolean doInBackground(Drawable... drawables) {
            Log.e("GGG","保存下来的文件名为:"+localpicname);
            return Utility.saveDrawableToFile(drawables[0],localpicname);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progressDialog!=null){progressDialog.dismiss();progressDialog=null;}
            ViewDialogFragment viewDialogFragment = new ViewDialogFragment();
            viewDialogFragment.setClickListener(new ViewDialogFragment.ClickShare() {
                @Override
                public void onClickShare() {
                    //Toast.makeText(ActivityMake.this, "你点击了Share", Toast.LENGTH_SHORT).show();
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, ConstValue.filePath+localpicname);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, "分享到"));
                }
            }, new ViewDialogFragment.ClickBack() {
                @Override
                public void onClickLike() {
                    //Toast.makeText(ActivityMake.this, "你点击了取消", Toast.LENGTH_SHORT).show();
                }
            });
            viewDialogFragment.setMessage(ConstValue.filePath+localpicname);
            viewDialogFragment.show(getFragmentManager());
        }
    }

}
