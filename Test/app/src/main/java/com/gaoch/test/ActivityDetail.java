package com.gaoch.test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gaoch.test.myclass.Pic;
import com.gaoch.test.util.ConstValue;
import com.gaoch.test.util.Utility;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityDetail extends AppCompatActivity {
    private TextView tv_username,tv_detail;
    private CircleImageView cv_user;
    private ImageView iv_pic;
    private Pic pic;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_detail);



        tv_detail=findViewById(R.id.detail_showpic_des);
        tv_username=findViewById(R.id.detail_showpic_userName);
        iv_pic=findViewById(R.id.detail_showpic_pic);
        cv_user=findViewById(R.id.detail_showpic_userPic);
        layout = findViewById(R.id.detail_layout);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConstValue.key_pic);
        if(bundle!=null){
            pic = (Pic) bundle.get(ConstValue.key_pic);
            if(pic!=null){
                tv_username.setText(pic.getUsername());
                tv_detail.setText(pic.getStylename());
                RequestOptions options1 = new RequestOptions().centerCrop().dontAnimate();
                RequestOptions options2 = new RequestOptions().centerCrop().dontAnimate();
                RequestOptions options = new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(Utility.dp2px(this,25))).error(R.drawable.background_allfround).placeholder(R.drawable.background_allfround);
                Glide.with(this).load(ConstValue.url_picAfter(pic.getPicname())).apply(options1).into(iv_pic);
                //Glide.with(this).load(pic.userpic).apply(options2).into(cv_user);
            }
        }
    }

    @Override
    public void onBackPressed() {
        RequestOptions options = new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(Utility.dp2px(this,25))).error(R.drawable.background_allfround).placeholder(R.drawable.background_allfround);
        Glide.with(this).load(ConstValue.url_picAfter(pic.getPicname())).apply(options).into(iv_pic);
        layout.setBackground(getResources().getDrawable(R.drawable.background_halfround));
        super.onBackPressed();
    }
}
