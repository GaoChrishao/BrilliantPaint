package com.gaoch.test.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gaoch.test.R;
import com.gaoch.test.myclass.Pic;
import com.gaoch.test.util.ConstValue;
import com.gaoch.test.util.Utility;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShowPicAdapter extends RecyclerView.Adapter<ShowPicAdapter.ViewHolder> {
    private mOnItemClickListener onItemClickListener;
    private Context mcontext;
    private  LayoutInflater inflater;
    public List<Pic> picList;
    private RequestOptions options;


    public ShowPicAdapter(Context context, Window window, List<Pic> picList){
        inflater = LayoutInflater.from(context);
        this.mcontext=context;
        this.picList = picList;
        options = new RequestOptions();
        options.transforms(new CenterCrop(),new RoundedCorners(Utility.dp2px(context,25))).placeholder(R.drawable.background_allfround);
    }


    @NonNull
    @Override
    public ShowPicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View itemView = inflater.inflate(R.layout.item_showpic,viewGroup,false);
        final ShowPicAdapter.ViewHolder viewHolder = new ShowPicAdapter.ViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        if(onItemClickListener!=null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(viewHolder,i);
                }
            });
        }
        //防止Glide加载图片混乱
        Object tag = viewHolder.user_name.getTag();
        if(tag!=null&&(int)tag!=i){
            Glide.with(mcontext).clear(viewHolder.user_pic);
            Glide.with(mcontext).clear(viewHolder.iv_pic);
        }


        RequestOptions options1 = new RequestOptions().centerCrop().placeholder(R.drawable.user_pic).error(R.drawable.user_pic).dontAnimate();
        String userpic=picList.get(i).getUserpic();
        Log.e("GGG","!!!!!!!!!!!!!!!"+userpic);
        if(!userpic.equals("null")){
            Glide.with(mcontext).load(ConstValue.url_picUser(userpic)).apply(options1).into(viewHolder.user_pic);

        }

        Glide.with(mcontext).load(ConstValue.url_picAfter(picList.get(i).getPicname())).apply(options).into(viewHolder.iv_pic);
        viewHolder.des.setText(picList.get(i).getStylename());
        viewHolder.user_name.setText(picList.get(i).getUsername());
        viewHolder.itemView.setTag(i);
        Animation animation = AnimationUtils.loadAnimation(mcontext,R.anim.view_scale);
        viewHolder.itemView.startAnimation(animation);
        viewHolder.user_name.setTag(i);


    }

    @Override
    public int getItemCount() {
        if(picList !=null){
            return picList.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView iv_pic;
        public CircleImageView user_pic;
        public TextView user_name,des;
        public ConstraintLayout layout;
        public LinearLayout layout1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.item_showpic_layout);
            layout1=itemView.findViewById(R.id.item_showpic_needBlur);
            iv_pic=itemView.findViewById(R.id.item_showpic_pic);
            user_pic=itemView.findViewById(R.id.item_showpic_userPic);
            user_name=itemView.findViewById(R.id.item_showpic_userName);
            des=itemView.findViewById(R.id.item_showpic_des);
            //user_pic.bringToFront();  使该view处于最上层
        }
    }

    public void setOnItemClickListener(mOnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public static interface mOnItemClickListener{
        public void onClick(ViewHolder viewHolder, int position);
    }

}
