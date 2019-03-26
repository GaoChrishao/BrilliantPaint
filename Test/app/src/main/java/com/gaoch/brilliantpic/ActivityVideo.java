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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ActivityVideo extends AppCompatActivity {
    // 相册选择回传吗
    public final static int REQUEST_PICK_PICS = 12;
    public final static int REQUEST_PICK_MUSIC = 13;


    private Button btn_chosePics,btn_choseMusic;
    private ConstraintLayout layout_bkg;
    private ProgressDialog progressDialog;

    private List<String> picPathList=new ArrayList<>();
    private boolean hasFindMusic,hasFIndPics;
    private volatile String videoPath;



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
                        .setCount(9)//参数说明：最大可选数，默认1
                        .setPuzzleMenu(false)//参数说明：是否显示相机按钮,默认显示，传false即不显示
                        .start(REQUEST_PICK_PICS);
            }
        });

        btn_choseMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setData(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                intent.setType("audio/*");
                startActivityForResult(intent,REQUEST_PICK_MUSIC);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_PICK_PICS:
                if(RESULT_OK == resultCode){
                    Log.e("GGG","选择结束");
                    //返回图片地址集合：如果你只需要获取图片的地址，可以用这个
                    ArrayList<String> resultPaths = data.getStringArrayListExtra(EasyPhotos.RESULT_PATHS);
                    //返回图片地址集合时如果你需要知道用户选择图片时是否选择了原图选项，用如下方法获取
                    //boolean selectedOriginal = data.getBooleanExtra(EasyPhotos.RESULT_SELECTED_ORIGINAL, false);

                    picPathList.clear();
                    picPathList.addAll(resultPaths);
                    if(picPathList.size()>0){
                        TaskCopyPics taskCopyPics=new TaskCopyPics();
                        taskCopyPics.execute(picPathList);
                    }
                    btn_chosePics.setVisibility(View.GONE);


                }
                break;
            case REQUEST_PICK_MUSIC:
                if(resultCode==RESULT_OK){
                    Uri musicUri = data.getData();//相对路径
                    Log.e("GGG",musicUri.getPath());
//                    Cursor cursor = getContentResolver().query(musicUri, null, null, null, null);//用ContentProvider查找选中的音频
//                    cursor.moveToFirst();
                   // final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));//获取音频的绝对路径
                   // String audioPath= UriUtils.getPath(this,musicUri);
                  //  Log.e("GGG",audioPath);
                    hasFindMusic=true;

                    btn_choseMusic.setVisibility(View.GONE);
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





//        ////参数分别是图片集合路径,输出路径,输出视频的宽度，输出视频的高度，输出视频的帧率
//        MyEpEditor.pic2video(ConstValue.pic_video_tmp_Path, ConstValue.pic_video_Path+"a.mp4", ConstValue.pic_crop_maxWddth, ConstValue.pic_crop_maxHeight, 0.2f, new OnEditorListener() {
//            @Override
//            public void onSuccess() {
//                Log.e("GGG","完成");
//            }
//
//            @Override
//            public void onFailure() {
//                Log.e("GGG","失败");
//                //Toast.makeText(ActivityVideo.this, "失败!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onProgress(float progress) {
//                System.out.println("。");
//            }
//        });


    }

    class TaskImages2Video extends AsyncTask<Void,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(!hasFindMusic||!hasFIndPics)return false;
            File file = new File(ConstValue.pic_video_tmp_Path);
            SeekableByteChannel out = null;
            try {
                videoPath=ConstValue.pic_video_Path+System.currentTimeMillis()+".mp4";
                out = NIOUtils.writableFileChannel(videoPath);
                int fps=5;
                AndroidSequenceEncoder encoder = new AndroidSequenceEncoder(out, Rational.R(fps, 1));
                if(!file.exists()){
                    return false;
                }else{
                    List<File> files= Arrays.asList(file.listFiles());
                    List<Bitmap> bitmapList=new ArrayList<>();
                    int size=files.size();
                    if(files!=null&&files.size()>0){
                        for(int i=0;i<files.size();i++){
                            Bitmap bitmap=getBitmap(files.get(i).getAbsolutePath(), Color.WHITE);
                            for(int j=0;j<fps;j++){
                                encoder.encodeImage(bitmap);
                                publishProgress((int)((i*fps+j+1.0)/(size*fps)*100));
                            }

                        }
                    }

                }
                // Finalize the encoding, i.e. clear the buffers, write the header, etc.
                encoder.finish();
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
            if(progressDialog!=null){
                progressDialog.dismiss();
                progressDialog=null;
            }
            if(aBoolean==true){
                ViewDialogFragment viewDialogFragment = new ViewDialogFragment();
                viewDialogFragment.setClickListener(new ViewDialogFragment.ClickShare() {
                    @Override
                    public void onClickShare() {
                        //Toast.makeText(ActivityMake.this, "你点击了Share", Toast.LENGTH_SHORT).show();
                        Intent shareIntent = new Intent();
                        shareIntent.setAction(Intent.ACTION_SEND);
                        shareIntent.putExtra(Intent.EXTRA_STREAM, videoPath);
                        shareIntent.setType("video/*");
                        startActivity(Intent.createChooser(shareIntent, "分享到"));
                    }
                }, new ViewDialogFragment.ClickBack() {
                    @Override
                    public void onClickLike() {
                        //Toast.makeText(ActivityMake.this, "你点击了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                viewDialogFragment.setMessage(videoPath);
                viewDialogFragment.show(getFragmentManager());
            }else{
                Toast.makeText(ActivityVideo.this, "失败!", Toast.LENGTH_SHORT).show();
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



    class TaskCopyPics extends AsyncTask<List<String>,Void,Boolean>{


        @Override
        protected Boolean doInBackground(List<String>... lists) {

            File file=new File(ConstValue.pic_video_tmp_Path);
            Log.e("GGG",file.getAbsolutePath());
            if(!file.exists()){
                file.mkdirs();
            }else{
                //先删除之前保存下来的图片文件
                List<File> files= Arrays.asList(file.listFiles());
                if(files!=null&&files.size()>0){
                    for(int i=0;i<files.size();i++){
                        files.get(i).delete();
                    }
                }
            }


            List<String> stringList=lists[0];

            Log.e("GGG","开始复制文件:"+stringList.size()+","+stringList.get(0));

            for(int i=0;i<stringList.size();i++){
                File file1=new File(stringList.get(i));
                if(file1.exists()){
                    try {
                        InputStream inStream = new FileInputStream(file1);
                        FileOutputStream fs = new FileOutputStream(ConstValue.pic_video_tmp_Path+"pic"+i+".jpg");
                        byte[] buffer = new byte[1024];
                        int byteread;
                        long bytesum=0;
                        while ( (byteread = inStream.read(buffer)) != -1) {
                            bytesum += byteread; //字节数 文件大小
                            fs.write(buffer, 0, byteread);
                        }
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            Toast.makeText(ActivityVideo.this, "图片移动完毕!", Toast.LENGTH_SHORT).show();
            hasFIndPics=true;
            processVideo();
        }
    }

    private Bitmap getBitmap(String photoLink, @ColorInt int backgroundColor) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return scaleFillBackground(BitmapFactory.decodeFile(photoLink, options), ConstValue.pic_crop_maxWddth,ConstValue.pic_crop_maxHeight, backgroundColor);
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
