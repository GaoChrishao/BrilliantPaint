package com.gaoch.test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gaoch.test.R;
import com.gaoch.test.myclass.Style;
import com.gaoch.test.myview.RoundAngleImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChoseMakeAdapter extends RecyclerView.Adapter<ChoseMakeAdapter.ViewHolder> {
    private mOnItemClickListener onItemClickListener;
    private Context mcontext;
    private  LayoutInflater inflater;
    public List<Style> styleList;
    public List<Boolean>checkList;

    public ChoseMakeAdapter(Context context,List<Style>styleList){
        inflater = LayoutInflater.from(context);
        this.mcontext=context;
        this.styleList =styleList;
        checkList=new ArrayList<>();
        for(int i=0;i<styleList.size();i++){
            checkList.add(false);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @NonNull
    @Override
    public ChoseMakeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View itemView = inflater.inflate(R.layout.item_chosemake,viewGroup,false);
        final ChoseMakeAdapter.ViewHolder viewHolder = new ChoseMakeAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        if(onItemClickListener!=null){
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(viewHolder,i);
                }
            });
        }
        RequestOptions options = new RequestOptions().centerCrop();
        Glide.with(mcontext).load(styleList.get(i).getPicurl()).apply(options).into(viewHolder.imageView);
        //Log.e("GGG",styleList.get(i).getPicurl());
        if(checkList.get(i)){
            viewHolder.relativeLayout.setBackground(mcontext.getDrawable(R.drawable.background_yuanjiao_white));
            //放大动画
            Animation animation = AnimationUtils.loadAnimation(mcontext,R.anim.view_scale_larger);
            viewHolder.itemView.startAnimation(animation);
        }else{
            //Log.e("GGG","取消颜色:"+i);
            viewHolder.relativeLayout.setBackgroundColor(mcontext.getResources().getColor(R.color.colorUnChoseLines));
        }
        viewHolder.itemView.setTag(i);
        //Log.e("GGG","onBindViewHolder:"+i);
    }

    @Override
    public int getItemCount() {
        if(styleList !=null){
            return styleList.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public RelativeLayout relativeLayout;
        public RoundAngleImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_chosemake_iv);
            relativeLayout=itemView.findViewById(R.id.item_chosemake_rl);
        }
    }

    public void setOnItemClickListener(mOnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public static interface mOnItemClickListener{
        public void onClick(ViewHolder viewHolder,int position);
    }
}
