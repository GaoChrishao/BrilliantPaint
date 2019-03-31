package com.gaoch.brilliantpic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gaoch.brilliantpic.myclass.GlideEngine;
import com.gaoch.brilliantpic.myview.ViewDialogFragment;
import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.Utility;
import com.huantansheng.easyphotos.EasyPhotos;

import org.jcodec.api.android.AndroidSequenceEncoder;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.io.SeekableByteChannel;
import org.jcodec.common.model.Rational;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import VideoHandle.EpEditor;
import VideoHandle.OnEditorListener;
import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ActivityVideo extends AppCompatActivity {
    // 相册选择回传吗
    public final static int REQUEST_PICK_PICS = 12;
    public final static int REQUEST_PICK_MUSIC = 13;

    private final int finishVideo=11;


    private Button btn_chosePics,btn_choseMusic;
    private ConstraintLayout layout_bkg;
    private ProgressDialog progressDialog;

    private List<String> picPathList=new ArrayList<>();
    private boolean hasFindMusic,hasFIndPics;
    private volatile String videoPath,musicpath;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_video);

        btn_chosePics=findViewById(R.id.video_btn_pics);
        btn_choseMusic=findViewById(R.id.video_btn_music);
        layout_bkg=findViewById(R.id.activity_video_layuot_bkg);

        //LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ll_bottom.getLayoutParams();
        //lp.setMargins(0, 0, 0, Utility.getNavigationHeight(this));

        File bkgFile=new File(ConstValue.getBkgPath(getApplicationContext()));

        if(bkgFile.exists()&&bkgFile.isFile()){
            layout_bkg.setBackground(Drawable.createFromPath(ConstValue.getBkg_blurPath(getApplicationContext())));
        }else{
            layout_bkg.setBackground(getResources().getDrawable(R.drawable.bkg_2,null));
        }


        setClick();



    }

    void setClick(){
        btn_chosePics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyPhotos.createAlbum(ActivityVideo.this, false, GlideEngine.getInstance())//参数说明：上下文，是否显示相机按钮，[配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
                        .setFileProviderAuthority("com.gaoch.brilliantpic.fileprovider")
                        .setCount(ConstValue.video_maxPictures)//参数说明：最大可选数，默认1
                        .setPuzzleMenu(false)//参数说明：是否显示相机按钮,默认显示，传false即不显示
                        .start(REQUEST_PICK_PICS);
            }
        });

        btn_choseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.setType("audio/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent,"choose music"),REQUEST_PICK_MUSIC);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_PICK_PICS:
                if(RESULT_OK == resultCode){
                    Log.d("GGG","选择结束");
                    //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
                     ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
                    //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                    //boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);

                    picPathList.clear();
                    picPathList.addAll(resultPaths);
                    btn_chosePics.setVisibility(View.GONE);
                    hasFIndPics=true;
                    processVideo();

                }
                break;
            case REQUEST_PICK_MUSIC:
                if(resultCode==RESULT_OK){
                    Uri musicUri = data.getData();//相对路径
                    musicpath=Utility.getPathFromUri(musicUri,this);
                    Log.d("GGG","音频地址:"+musicpath);

                    //判断音频格式
                    String s[]=musicpath.split("\\.");
                    String filetype=s[s.length-1];
                    if(!(filetype.equals("mp3")||filetype.equals("MP3"))){
                        Toast.makeText(this, "请选择mp3格式的音频文件!", Toast.LENGTH_SHORT).show();

                        return;
                    }
                    btn_choseMusic.setVisibility(View.GONE);
                    hasFindMusic=true;
                    processVideo();


                }
                break;
        }

    }





    void processVideo(){
        if(hasFindMusic==false||hasFIndPics==false)return;
        if(progressDialog ==null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            //progressDialog.setTitle("进度");
            progressDialog.setMessage("处理中");
            //progressDialog.setIcon(R.drawable.ic_launcher);
            //progressDialog 进度条的属性
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.setIndeterminate(false);//true时可以实时更新
            progressDialog.setCancelable(false);
            progressDialog.show();

   }

        TaskImages2Video taskImages2Video=new TaskImages2Video();
        taskImages2Video.execute();






    }

    class TaskImages2Video extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(!hasFindMusic||!hasFIndPics)return false;
            File file = new File(ConstValue.pic_video_tmp_Path);
            SeekableByteChannel out = null;
            try {
                if(!file.exists()){
                    file.mkdirs();
                }else{
                    if(picPathList!=null&&picPathList.size()>0){
                        Log.d("GGG","开始图片合成视频");
                        videoPath=ConstValue.pic_video_tmp_Path+"tmp.mp4";
                        out = NIOUtils.writableFileChannel(videoPath);
                        AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(ConstValue.video_fps, ConstValue.video_den));
                        int size=picPathList.size();
                        for(int i=0;i<size;i++){
                            Bitmap bitmap=getBitmap(picPathList.get(i), Color.BLACK);
                            for(int j=0;j<ConstValue.video_fps;j++){
                                encoder.encodeImage(bitmap);
                                publishProgress((int)((i*ConstValue.video_fps+j+1.0)/(size*ConstValue.video_fps)*100));
                            }

                        }
                        // Finalize the encoding, i.e. clear the buffers, write the header, etc.
                        encoder.finish();
                    }

                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                NIOUtils.closeQuietly(out);
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.setTitle("正在添加BGM...");


            if(aBoolean){
                File muFIle=new File(videoPath);
                if(muFIle.exists()){
                    Log.d("GGG","视频存在");
                }else{
                    Log.e("GGG","视频不存在!");
                }
                muFIle=new File(musicpath);
                if(muFIle.exists()){
                    Log.d("GGG","音乐存在");
                }else{
                    Log.e("GGG","音乐不存在");
                }
                //参数分别是视频路径，音频路径，输出路径,原始视频音量(1为100%,0.7为70%,以此类推),添加音频音量
                final String newVideoPath=ConstValue.pic_video_Path+System.currentTimeMillis()+".mp4";
                EpEditor.music(videoPath, musicpath, newVideoPath, 1.0f, 1.0f, new OnEditorListener() {
                    @Override
                    public void onSuccess() {
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                            progressDialog=null;
                        }

                        handler.sendEmptyMessage(finishVideo);
                        File preFile=new File(videoPath);
                        if(preFile.exists()){
                            preFile.delete();
                        }
                        videoPath=newVideoPath;
                        ViewDialogFragment viewDialogFragment = new ViewDialogFragment();
                        viewDialogFragment.setClickListener(new ViewDialogFragment.ClickShare() {
                            @Override
                            public void onClickShare() {
                                Intent shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.putExtra(Intent.EXTRA_STREAM, videoPath);
                                shareIntent.setType("video/*");
                                startActivity(shareIntent);
                            }
                        }, new ViewDialogFragment.ClickBack() {
                            @Override
                            public void onClickLike() {
                            }
                        });

                        viewDialogFragment.setMessage(newVideoPath);
                        viewDialogFragment.show(getFragmentManager());
                    }

                    @Override
                    public void onFailure() {

                    }

                    @Override
                    public void onProgress(float progress) {
                        //这里获取处理进度
                        progressDialog.setProgress((int)(progress*100));
                        Log.e("GGG","添加音频中...");
                    }
                });
            }else{

            }






        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(progressDialog!=null){
                Log.e("GGG","进度:"+values[0]);
                progressDialog.setProgress(values[0]);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }
    }


    Handler handler = new Handler(){


        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case finishVideo:
                    btn_choseMusic.setVisibility(View.VISIBLE);
                    btn_chosePics.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };



    private Bitmap getBitmap(String photoLink, @ColorInt int backgroundColor) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return scaleFillBackground(BitmapFactory.decodeFile(photoLink, options), ConstValue.video_width,ConstValue.video_height, backgroundColor);
    }


    /**
     * this method will create bitmap with required height and width by scaling it and empty area will be filled with background color
     *
     * @param imageToScale      bitmap
     * @param destinationWidth  width
     * @param destinationHeight height
     * @return scaled bitmap
     */
    public static Bitmap scaleFillBackground(Bitmap imageToScale, int destinationWidth,
                                             int destinationHeight, @ColorInt int backgroundColor) {
        if (destinationHeight > 0 && destinationWidth > 0 && imageToScale != null) {
            int width = imageToScale.getWidth();
            int height = imageToScale.getHeight();

            float widthRatio = (float) destinationWidth / (float) width;
            float heightRatio = (float) destinationHeight / (float) height;

            int finalWidth = (int) Math.floor(width * widthRatio);
            int finalHeight = (int) Math.floor(height * widthRatio);
            if (finalWidth > destinationWidth || finalHeight > destinationHeight) {
                finalWidth = (int) Math.floor(width * heightRatio);
                finalHeight = (int) Math.floor(height * heightRatio);
            }

            imageToScale = Bitmap.createScaledBitmap(imageToScale, finalWidth, finalHeight, true);

            Bitmap imageWithBackground = setBackground(destinationWidth, destinationHeight,
                    finalWidth, finalHeight,
                    backgroundColor, imageToScale);
            return imageWithBackground;
        } else {
            return imageToScale;
        }
    }

    /**
     * applies background to each image
     *
     * @param destinationWidth  destination width
     * @param destinationHeight destination height
     * @param finalWidth        final width from the previous calculation
     * @param finalHeight       final height from the previous calculation
     * @param backgroundColor   background color for bitmap
     * @param imageToScale      bitmap on which the background will apply
     * @return {@link Bitmap}
     */
    private static Bitmap setBackground(int destinationWidth, int destinationHeight,
                                        int finalWidth, int finalHeight, int backgroundColor,
                                        Bitmap imageToScale) {
        Bitmap scaledImage = Bitmap.createBitmap(destinationWidth, destinationHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledImage);

        //Draw background color
        Paint paint = new Paint();
        paint.setColor(backgroundColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        //Calculate the ratios and decide which part will have empty areas (width or height)
        float bitmapRatio = (float) finalWidth / (float) finalHeight;
        float destinationRatio = (float) destinationWidth / (float) destinationHeight;
        float left = bitmapRatio >= destinationRatio ? 0 : (float) (destinationWidth - finalWidth) / 2;
        float top = bitmapRatio < destinationRatio ? 0 : (float) (destinationHeight - finalHeight) / 2;
        canvas.drawBitmap(imageToScale, left, top, null);

        return scaledImage;
    }









}
