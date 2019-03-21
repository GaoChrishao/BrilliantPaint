package com.gaoch.brilliantpic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gaoch.brilliantpic.R;
import com.gaoch.brilliantpic.myclass.Comment;
import com.gaoch.brilliantpic.util.ConstValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private mOnItemClickListener onItemClickListener;
    private Context mcontext;
    private  LayoutInflater inflater;
    public List<Comment> commentList;
    private Window window;
    private ConstraintLayout firstLayout;
    private RequestOptions options;

    public CommentsAdapter(Context context, Window window, List<Comment> commentList){
        inflater = LayoutInflater.from(context);
        this.mcontext=context;
        this.commentList = commentList;
        this.window=window;
        options = new RequestOptions().centerCrop().placeholder(R.drawable.user_pic).error(R.drawable.user_pic).dontAnimate();
    }


    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        View itemView = inflater.inflate(R.layout.item_comment,viewGroup,false);
        final CommentsAdapter.ViewHolder viewHolder = new CommentsAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        if(holder.username.getTag()!=null){

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

        Object tag=  viewHolder.username.getTag();
        if(tag!=null&&(int)tag!=i){
            Glide.with(mcontext).clear(viewHolder.userpic);
        }



        String userpic=commentList.get(i).getUserpic();
        if(userpic!=null&&userpic.length()>4){
            Glide.with(mcontext).load(ConstValue.url_picUser(userpic)).apply(options).into(viewHolder.userpic);
        }

        viewHolder.content.setText(commentList.get(i).getContent());
        viewHolder.username.setText(commentList.get(i).getUsername());
        Long time=commentList.get(i).getTime();


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年:MM月dd日:HH时");
        Date date = new Date(time);
        viewHolder.time.setText(formatter.format(date));

        viewHolder.username.setTag(i);


//        if(i==0){
//            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(0,Utility.dp2px(mcontext,110),0,0);//4个参数按顺序分别是左上右下
//            viewHolder.layout.setLayoutParams(layoutParams);
//            viewHolder.layout.setTag(i);
//        }
//
//        Animation animation = AnimationUtils.loadAnimation(mcontext,R.anim.view_scale);
//        viewHolder.itemView.startAnimation(animation);

    }

    @Override
    public int getItemCount() {
        if(commentList !=null){
            return commentList.size();
        }else{
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView userpic;
        public TextView content,username,time;
        public ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userpic =itemView.findViewById(R.id.comment_userpic);
            time=itemView.findViewById(R.id.comment_time);
            username=itemView.findViewById(R.id.comment_username);
            content=itemView.findViewById(R.id.comment_content);
        }
    }

    public void setOnItemClickListener(mOnItemClickListener onItemClickListener){
        this.onItemClickListener=onItemClickListener;
    }

    public static interface mOnItemClickListener{
        public void onClick(ViewHolder viewHolder, int position);
    }

}
