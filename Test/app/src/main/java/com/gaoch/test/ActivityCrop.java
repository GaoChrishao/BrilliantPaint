package com.gaoch.test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.gaoch.test.util.Blur;
import com.gaoch.test.util.ConstValue;
import com.gaoch.test.util.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ActivityCrop extends Activity {
    private FloatingActionButton btn;
    private CropImageView cropImageView;
    private volatile String fileName;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_crop);
        cropImageView =findViewById(R.id.crop_CropImageView);
        btn= findViewById(R.id.crop_button);
        String imgPath=getIntent().getStringExtra(ConstValue.key_imageUrl);
        if(imgPath!=null){
            Log.e("GGG",imgPath);
           cropImageView.setImageBitmap(Utility.LoadLocalBitmap(imgPath));
           //cropImageView.setImageUriAsync(Uri.parse(imgPath));
           //cropImageView.setImage
            //new TaskLoadImg().execute(imgPath);

        }
        int x=getIntent().getIntExtra(ConstValue.key_imageCropX,0);
        int y=getIntent().getIntExtra(ConstValue.key_imageCropY,0);

        if(x!=0&&y!=0){
            cropImageView.setAspectRatio(x,y);
        }else{
            cropImageView.setAspectRatio(3,4);
        }
        cropImageView.setFixedAspectRatio(true);

        Intent intent=getIntent();
        type=intent.getStringExtra(ConstValue.key_crop);
        if(type==null)type="";


        // 当触摸时候才显示网格线

       btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取裁剪成的图片
                Bitmap croppedImage = cropImageView.getCroppedImage();
                fileName=System.currentTimeMillis()+"";
                switch (type){
                    case ConstValue.CROP_BKG:
                        new TaskBkg().execute(croppedImage);
                        break;
                    case ConstValue.CROP_USER:
                        new TaskUser().execute(croppedImage);
                        break;
                        default:
                            new ActivityCrop.MyTask().execute(croppedImage);
                            break;

                }

            }
        });

    }


    //保存剪裁的图片到文件
    class MyTask extends AsyncTask<Bitmap,Void, String> {
        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            int prePicWidth=bitmaps[0].getWidth();
            int prePicHeight=bitmaps[0].getHeight();
            if(prePicHeight>ConstValue.pic_crop_maxHeight){
                double scaleFactor=(ConstValue.pic_crop_maxHeight+0.0)/prePicHeight;
                Bitmap bkg_scaled= Bitmap.createScaledBitmap(bitmaps[0],(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
                return Utility.saveBitmapToFile(bkg_scaled,ConstValue.pic_quality,"tmp","jpg");
            }else{
                return Utility.saveBitmapToFile(bitmaps[0],ConstValue.pic_quality,"tmp","jpg");
            }


        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            if(path!=null){
                //Toast.makeText(ActivityCrop.this, "保存成功", Toast.LENGTH_SHORT).show();
                Log.e("GGG","图片剪裁成功:"+path);
                Intent intent = new Intent();
                intent.putExtra(ConstValue.key_imageUrl,path);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(ActivityCrop.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //保存背景图片到文件
    class TaskBkg extends AsyncTask<Bitmap,Void, String> {
        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            DisplayMetrics metrics =new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            Bitmap originBItmap= ((BitmapDrawable)Utility.ReSizePic(bitmaps[0],(int)height,(int)width,getApplicationContext())).getBitmap();
            Utility.saveBitmapToFileInData(originBItmap,ConstValue.getBkgPath(getApplicationContext()));
            //精确缩放到指定大小
            Bitmap bkg_origin= Bitmap.createScaledBitmap(originBItmap,(int)(width/ConstValue.scaleFactor),(int)(height/ConstValue.scaleFactor), true);
            bkg_origin.setConfig(Bitmap.Config.ARGB_8888);
            return Utility.saveBitmapToFileInData(Blur.doBlur(bkg_origin,ConstValue.radius,true),ConstValue.getBkg_blurPath(getApplicationContext()));

        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            if(path!=null){
                //Toast.makeText(ActivityCrop.this, "保存成功", Toast.LENGTH_SHORT).show();
                Log.e("GGG","图片剪裁成功:"+path);
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(ActivityCrop.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }


    //保存剪裁的用户头像到文件
    class TaskUser extends AsyncTask<Bitmap,Void, String> {
        @Override
        protected String doInBackground(Bitmap... bitmaps) {
            int prePicWidth=bitmaps[0].getWidth();
            int prePicHeight=bitmaps[0].getHeight();
            if(prePicHeight>ConstValue.pic_crop_maxHeight){
                double scaleFactor=(ConstValue.pic_User_maxHeight+0.0)/prePicHeight;
                Bitmap bkg_scaled= Bitmap.createScaledBitmap(bitmaps[0],(int)(prePicWidth*scaleFactor),(int)(prePicHeight*scaleFactor), true);
                return Utility.saveBitmapToFile(bkg_scaled,ConstValue.pic_quality,"tmp","jpg");
            }else{
                return Utility.saveBitmapToFile(bitmaps[0],ConstValue.pic_quality,"tmp","jpg");
            }


        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            if(path!=null){
                //Toast.makeText(ActivityCrop.this, "保存成功", Toast.LENGTH_SHORT).show();
                Log.e("GGG","图片剪裁成功:"+path);
                Intent intent = new Intent();
                intent.putExtra(ConstValue.key_imageUrl,path);
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(ActivityCrop.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }






    class TaskLoadImg extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            return Utility.LoadLocalBitmap(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            cropImageView.setImageBitmap(bitmap);
        }
    }
}
