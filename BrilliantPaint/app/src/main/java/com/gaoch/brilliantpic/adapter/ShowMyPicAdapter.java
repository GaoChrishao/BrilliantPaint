package com.gaoch.brilliantpic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gaoch.brilliantpic.R;
import com.gaoch.brilliantpic.myclass.Pic;
import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.Utility;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ShowMyPicAdapter extends RecyclerView.Adapter<ShowMyPicAdapter.ViewHolder> {
    private mOnItemClickListener onItemClickListener;
    private Context mcontext;
    private  LayoutInflater inflater;
    public List<Pic> picList;
    private Window window;
    private ConstraintLayout firstLayout;
    private RequestOptions options;

    public ShowMyPicAdapter(Context context, Window window, List<Pic> picList){
        inflater = LayoutInflater.from(context);
        this.mcontext=context;
        this.picList = picList;
        this.window=window;
        options = new RequestOptions().centerCrop();
        options.transforms(new CenterCrop(),new RoundedCorners(Utility.dp2px(context,25))).error(R.drawable.background_all_round).placeholder(R.drawable.background_all_round);
    }


    @NonNull
    @Override
    public ShowMyPicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View itemView = inflater.inflate(R.layout.item_showmypic,viewGroup,false);
        final ShowMyPicAdapter.ViewHolder viewHolder = new ShowMyPicAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder.layout.getTag()!=null){
//            if(((int)holder.layout.getTag())==0){
//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParams.setMargins(0,Utility.dp2px(mcontext,0),0,0);//4个参数按顺序分别是左上右下
//                holder.layout.setLayoutParams(layoutParams);
//            }
        }
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
        Glide.with(mcontext).load(ConstValue.url_picAfter(picList.get(i).getPicname())).apply(options).into(viewHolder.iv_pic);
        viewHolder.des.setText(picList.get(i).getStylename());


//        if(i==0){
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(0,Utility.dp2px(mcontext,110),0,0);//4个参数按顺序分别是左上右下
//            viewHolder.layout.setLayoutParams(layoutParams);
//            viewHolder.layout.setTag(i);
//        }

        Animation animation = AnimationUtils.loadAnimation(mcontext,R.anim.view_scale);
        viewHolder.itemView.startAnimation(animation);

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
        public TextView des;
        public ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.item_showmypic_layout);
            iv_pic=itemView.findViewById(R.id.item_showmypic_pic);
            des=itemView.findViewById(R.id.item_showmypic_des);
        }

    }

    public void setOnItemClickListener(mOnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public static interface mOnItemClickListener{
        public void onClick(ViewHolder viewHolder, int position);
    }

}
