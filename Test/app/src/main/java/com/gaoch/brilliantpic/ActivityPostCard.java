package com.gaoch.brilliantpic;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.ImageUtil;
import com.gaoch.brilliantpic.util.Utility;
import com.yinglan.shadowimageview.ShadowImageView;

import java.io.File;

import androidx.annotation.Nullable;

public class ActivityPostCard extends Activity {
    private Drawable pic_show;
    private Bitmap pic,pic_after;
    private boolean hasPic=false;
    private ImageView iv_add;
    private ShadowImageView iv_pic;
    private RelativeLayout rl;
    private Button btn1;
    private EditText et1;
    private static final int requestCode_chosePic=1;
    private static final int requestCode_cropPic=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_post_card);
        iv_add=findViewById(R.id.activity_postcard_iv_add);
        iv_pic=findViewById(R.id.activity_postcard_iv);
        rl=findViewById(R.id.activity_postcard_rl);
        et1=findViewById(R.id.activity_postcard_et1);
        btn1=findViewById(R.id.activity_postcard_btn1);
        setClickEvent();
    }

    void setClickEvent(){
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePic();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasPic&&pic!=null){
                    String s=et1.getText()+"";
                    pic_after = BitmapFactory.decodeResource(getResources(), R.drawable.water_icon);
                    pic_after = ImageUtil.createWaterMaskRightBottom(getApplicationContext(), pic, pic_after, 0, 0);
                    pic_after = ImageUtil.drawTextToCenter(getApplicationContext(), pic_after, s, 32, Color.WHITE);
                    pic_show = Utility.ReSizePic(pic_after,(int)(rl.getMeasuredHeight()),(int)(rl.getMeasuredWidth()),getApplicationContext());
                    iv_pic.setImageDrawable(pic_show);
                }
            }
        });
        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePic();
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case requestCode_chosePic:
                if(resultCode==RESULT_OK){
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
                    pic_show = Utility.ReSizePic(pic,(int)(rl.getMeasuredHeight()),(int)(rl.getMeasuredWidth()),this);
                    hasPic=true;
                    iv_add.setVisibility(View.GONE);
                    iv_pic.setVisibility(View.VISIBLE);
                    iv_pic.setImageDrawable(pic_show);
                    File file = new File(path);
                    if(file.exists()){
                        file.delete();
                    }
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

    //保存图片到文件
    class MyTask extends AsyncTask<Drawable,Void, Boolean> {
        @Override
        protected Boolean doInBackground(Drawable... drawables) {
            return Utility.saveDrawableToFile(drawables[0],"tmp.jpg");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(ActivityPostCard.this, "保存成功", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(ActivityPostCard.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
