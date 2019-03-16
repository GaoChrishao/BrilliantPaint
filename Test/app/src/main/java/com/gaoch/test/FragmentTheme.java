package com.gaoch.test;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gaoch.test.util.Blur;
import com.gaoch.test.util.ConstValue;
import com.gaoch.test.util.Utility;

import static android.app.Activity.RESULT_OK;


/**
 * Created by GaoCh on 2018/7/24.
 */

public class FragmentTheme extends Fragment {
    private LinearLayout layout1, layout2, layout3;
    private int hasBlured_top1 = 0, hasBlured_top2 = 0, hasBlured_top3 = 0;
    private Button btn_choseBkg, btn_cancelBkg, btn_choseUserPic;
    private final int requestCode_chosebkg = 1111;
    private final int requestCode_processbkg = 1112;


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
            case requestCode_processbkg:
                if (resultCode == RESULT_OK) {
                    ((ActivityMain) getActivity()).layout_bkg.setBackground(new BitmapDrawable(getContext().getResources(),
                            Utility.LoadLocalBitmap(ConstValue.getBkgPath(getContext()))));
                    DisplayMetrics metrics =new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
                    int width = metrics.widthPixels;
                    int height = metrics.heightPixels;
                    Blur.initBkg(Utility.LoadLocalBitmap(ConstValue.getBkg_blurPath(getContext())),width,height);
                } else {
                    Toast.makeText(getContext(), "未选择图片", Toast.LENGTH_SHORT).show();
                }
                break;

        }


    }


    public void setBlur(){
        final LinearLayout bkg_layout=((ActivityMain)getActivity()).layout_bkg;
        layout1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(hasBlured_top1!=layout1.getTop()){
                    Blur.blur(bkg_layout,layout1,ConstValue.radius,ConstValue.scaleFactor,ConstValue.RoundCorner);
                    hasBlured_top1=layout1.getTop();
                }

                return true;
            }
        });
        layout2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if(hasBlured_top2!=layout2.getTop()){
                    Blur.blur(bkg_layout,layout2,ConstValue.radius,ConstValue.scaleFactor,ConstValue.RoundCorner);
                    hasBlured_top2=layout2.getTop();
                }

                return true;
            }
        });

    }

}
