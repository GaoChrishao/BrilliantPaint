package com.gaoch.test;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoch.test.myview.CircleExp;
import com.gaoch.test.util.Blur;
import com.gaoch.test.util.ConstValue;
import com.stx.xhb.xbanner.XBanner;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class FragmentHome extends Fragment {
    private LinearLayout layout_1,layout_2,layout_3,layout_4;
    private XBanner mXBanner;
    private List<String>imageUrls;
    private List<String>titles;
    private Button btn_1,btn_2;
    private TextView tv_5;
    private View.OnTouchListener onTouchListener;
    private CircleImageView circleImageView;
    private CircleExp exp;
    private TextView tv_name,tv_exp;
    private int hasBlured_top1=0,hasBlured_top2=0,hasBlured_top3=0,hasBlured_top4=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_make,container,false);
        layout_1=view.findViewById(R.id.main_layout1);
        layout_2=view.findViewById(R.id.main_layout2);
        layout_3=view.findViewById(R.id.main_layout3);
        btn_1=view.findViewById(R.id.fragment_main_btn_1);
        btn_2=view.findViewById(R.id.fragment_main_btn_2);
        circleImageView=view.findViewById(R.id.fragment_main_user_pic);
        exp=view.findViewById(R.id.fragment_main_exp);
        tv_name=view.findViewById(R.id.fragment_main_tv_name);
        tv_exp=view.findViewById(R.id.fragment_main_tv_exp);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        exp.setAngle(180);
        tv_name.setText(getActivity().getSharedPreferences(ConstValue.sp,MODE_PRIVATE).getString(ConstValue.spUsername,"妙笔生画"));
        int exp= getActivity().getSharedPreferences(ConstValue.spAccount,MODE_PRIVATE).getInt(ConstValue.spExp,999999);
        int exp_1=exp;
        int level=0;
        while (exp_1>0){
            exp_1=exp_1/10;
            level++;
        }
        tv_exp.setText("Lv."+level);
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
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(),ActivityMake.class);
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
        //mXBanner.startAutoPlay();
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


    public void setBlur(){
        final LinearLayout bkg_layout=((ActivityMain)getActivity()).layout_bkg;
        layout_1.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int position[]=new int[2];
                layout_1.getLocationInWindow(position);
                if(hasBlured_top1!=position[1]){
                    Blur.blur(bkg_layout,layout_1,ConstValue.radius,ConstValue.scaleFactor,ConstValue.RoundCorner);
                    hasBlured_top1=position[1];
                    Log.e("GGG",position[0]+" "+position[1]);
                }

                return true;
            }
        });
        layout_2.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int position[]=new int[2];
                layout_2.getLocationInWindow(position);
                if(hasBlured_top2!=position[1]){
                    Blur.blur(bkg_layout,layout_2,ConstValue.radius,ConstValue.scaleFactor,ConstValue.RoundCorner);
                    hasBlured_top2=position[1];
                    Log.e("GGG","--------");
                }


                return true;
            }
        });
        layout_3.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                int position[]=new int[2];
                layout_3.getLocationInWindow(position);
                if(hasBlured_top3!=position[1]){
                    Blur.blur(bkg_layout,layout_3,ConstValue.radius,ConstValue.scaleFactor,ConstValue.RoundCorner);
                    hasBlured_top3=position[1];
                }
                //Log.e("GGG",layout_3.getTop()+"");

                return true;
            }
        });

    }
}
