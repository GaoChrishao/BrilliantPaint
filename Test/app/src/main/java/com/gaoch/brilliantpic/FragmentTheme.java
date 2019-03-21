package com.gaoch.brilliantpic;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gaoch.brilliantpic.myclass.User;
import com.gaoch.brilliantpic.util.Blur;
import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.Utility;

import java.io.File;

import static android.app.Activity.RESULT_OK;


/**
 * Created by GaoCh on 2018/7/24.
 */

public class FragmentTheme extends Fragment {
    private LinearLayout layout1, layout2, layout3;
    private Button btn_choseBkg, btn_cancelBkg, btn_choseUserPic;
    private final int requestCode_chosebkg = 1111;
    private final int requestCode_processbkg = 1112;
    private final int requestCode_choseUserPic = 1113;
    private final int requestCode_processUser = 1114;
    private ProgressDialog progressDialog;
    private Blur.BlurLayout blurLayout1,blurLayout2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        initView(view);
        return view;
    }

    public void initView(View view) {
        layout1 = view.findViewById(R.id.fragment_theme_layout_1);
        layout2 = view.findViewById(R.id.fragment_theme_layout_2);
        btn_cancelBkg = view.findViewById(R.id.theme_cancelBG);
        btn_choseBkg = view.findViewById(R.id.theme_chooseBG);
        btn_choseUserPic = view.findViewById(R.id.theme_chooseUserpic);


    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_choseBkg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_choose = new Intent(Intent.ACTION_PICK);//Intent.ACTION_GET_CONTENT和是获得最近使用过的图片。
                intent_choose.setType("image/*");//应该是指定数据类型是图片。
                startActivityForResult(intent_choose, requestCode_chosebkg);
            }
        });
        btn_choseUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_choose = new Intent(Intent.ACTION_PICK);//Intent.ACTION_GET_CONTENT和是获得最近使用过的图片。
                intent_choose.setType("image/*");//应该是指定数据类型是图片。
                startActivityForResult(intent_choose, requestCode_choseUserPic);
            }
        });

        btn_cancelBkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File bkgFile=new File(ConstValue.getBkgPath(getContext()));
                File bkgFile2=new File(ConstValue.getBkg_blurPath(getContext()));
                if(bkgFile.exists()&&bkgFile.isFile()){
                   bkgFile.delete();
                }
                if(bkgFile2.exists()&&bkgFile2.isFile()){
                    bkgFile2.delete();
                }
                Toast.makeText(getContext(), "重启生效", Toast.LENGTH_SHORT).show();
            }
        });
         setBlur();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case requestCode_chosebkg:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();//图片的相对路径
                    Cursor cursor = getContext().getContentResolver().query(imageUri, null, null, null, null);//用ContentProvider查找选中的图片
                    cursor.moveToFirst();
                    final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//获取图片的绝对路径


                    Intent intent = new Intent(getContext(), ActivityCrop.class);
                    intent.putExtra(ConstValue.key_imageUrl, path);

                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;

                    intent.putExtra(ConstValue.key_imageCropX, (int) width);
                    intent.putExtra(ConstValue.key_imageCropY, (int) height);
                    intent.putExtra(ConstValue.key_crop, ConstValue.CROP_BKG);
                    startActivityForResult(intent, requestCode_processbkg);

                    Log.e("GGG", "选择图片");
                } else {
                    Log.e("GGG", "取消选择图片");
                }
                break;
            case requestCode_choseUserPic:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = data.getData();//图片的相对路径
                    Cursor cursor = getContext().getContentResolver().query(imageUri, null, null, null, null);//用ContentProvider查找选中的图片
                    cursor.moveToFirst();
                    final String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//获取图片的绝对路径
                    Intent intent = new Intent(getContext(), ActivityCrop.class);
                    intent.putExtra(ConstValue.key_imageUrl, path);

                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);


                    intent.putExtra(ConstValue.key_imageCropX, ConstValue.pic_User_maxWddth);
                    intent.putExtra(ConstValue.key_imageCropY, ConstValue.pic_User_maxHeight);
                    intent.putExtra(ConstValue.key_crop, ConstValue.CROP_USER);
                    startActivityForResult(intent, requestCode_processUser);

                    Log.e("GGG", "选择用户头像");
                } else {
                    Log.e("GGG", "取消选择用户头像");
                }
                break;
            case requestCode_processbkg:
                if (resultCode == RESULT_OK) {
                    ((ActivityMain) getActivity()).layout_bkg.setBackground(new BitmapDrawable(getContext().getResources(),
                            Utility.LoadLocalBitmap(ConstValue.getBkgPath(getContext()))));
                    DisplayMetrics metrics =new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;
                    Blur.initBkgWithResieze(((ActivityMain)getActivity()).layout_bkg,Utility.LoadLocalBitmap(ConstValue.getBkg_blurPath(getContext())),width,height);
                    blurLayout1.reSetPositions();
                    blurLayout2.reSetPositions();
                } else {
                    Toast.makeText(getContext(), "未选择图片", Toast.LENGTH_SHORT).show();
                }
                break;
            case requestCode_processUser:
                if (resultCode == RESULT_OK) {
                    new TaskUPloadUserPic().execute();
                    if(progressDialog==null){
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("上传中");
                        progressDialog.setCanceledOnTouchOutside(false);
                    }
                } else {
                    Toast.makeText(getContext(), "未选择图片", Toast.LENGTH_SHORT).show();
                }
                break;

        }


    }


    public void setBlur(){
        final LinearLayout bkg_layout=((ActivityMain)getActivity()).layout_bkg;
        blurLayout1=new Blur.BlurLayout(layout1,bkg_layout);
        blurLayout2=new Blur.BlurLayout(layout2,bkg_layout);

    }

    //上传剪裁的用户头像
    class TaskUPloadUserPic extends AsyncTask<Void,Void, User> {
        @Override
        protected User doInBackground(Void... voids) {
            Utility.LoadLocalBitmap(ConstValue.filePath+"tmp.jpg");
            SharedPreferences sharedPreferences =getActivity().getSharedPreferences(ConstValue.sp, Context.MODE_PRIVATE);
            Long account=sharedPreferences.getLong(ConstValue.spAccount,-1);
            String password=sharedPreferences.getString(ConstValue.spPassword,"");
            User user;
            if(account==-1){
                Toast.makeText(getContext(), "未登入!", Toast.LENGTH_SHORT).show();
                user=new User();
                user.setAccount(-1l);
                return user;
            }
            try {
                user=Utility.uploadUserPicFile(
                        getContext(),
                        ConstValue.url_upload_userpic(password,String.valueOf(account)),
                        ConstValue.filePath+"tmp.jpg"
                );
            } catch (Exception e) {
                e.printStackTrace();
                user=new User();
                user.setAccount(-1l);
            }
            return user;

        }

        @Override
        protected void onPostExecute(User user) {
            super.onPostExecute(user);
            Log.e("GGG",user.getAccount()+"");
            if(user.getAccount()!=-1l){
                //上传成功
                Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(ConstValue.sp,Context.MODE_PRIVATE).edit();
                editor.putString(ConstValue.spUserPic,user.getUserpic());
                editor.apply();
                ((ActivityMain)getActivity()).updateUserPic();
            }else{
                Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
            }
            if(progressDialog!=null){
                progressDialog.dismiss();
                progressDialog=null;
            }
        }


    }

}
