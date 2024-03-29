package com.gaoch.brilliantpic;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.gaoch.brilliantpic.adapter.CommentsAdapter;
import com.gaoch.brilliantpic.myclass.BasicUserInfo;
import com.gaoch.brilliantpic.myclass.Comment;
import com.gaoch.brilliantpic.myclass.Like;
import com.gaoch.brilliantpic.myclass.Pic;
import com.gaoch.brilliantpic.myview.DialogWrite;
import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.HttpUtil;
import com.gaoch.brilliantpic.util.Utility;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ActivityDetail extends AppCompatActivity {
    private TextView tv_username,tv_detail,tv_likes,tv_commentsnum;
    private CircleImageView cv_user;
    private ImageView iv_pic,iv_like;
    private Pic pic;
    private LinearLayout layout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CommentsAdapter commentsAdapter;
    private List<Comment>commentList;
    private final int msg_noInternet=1111;
    private final int msg_nomore=1112;
    private final int msg_update=1113;
    private final int msg_make=1114;
    private final int msg_getIsLike=1115;
    private final int msg_sendIsLike=1116;
    private  boolean hasMore = true; // 是否还有
    private FloatingActionButton floatBtn;
    // 若是上拉加载更多的网络请求 则不需要删除数据
    private boolean isLoadingMore = false;
    // 最后一个条目位置
    private  int lastVisibleItem = -1;
    private  boolean loading=false;

    private volatile boolean hasGetIsLike=false;
    private volatile boolean isLike=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utility.setFullScreen_origin(this);
        setContentView(R.layout.activity_detail);



        tv_detail=findViewById(R.id.detail_showpic_des);
        tv_username=findViewById(R.id.detail_showpic_userName);
        tv_likes=findViewById(R.id.detail_showpic_tv_likes);
        tv_commentsnum=findViewById(R.id.detail_showpic_tv_commentsnum);
        iv_pic=findViewById(R.id.detail_showpic_pic);
        iv_like=findViewById(R.id.detail_showpic_iv_like);
        cv_user=findViewById(R.id.detail_showpic_userPic);
        layout = findViewById(R.id.detail_layout);
        swipeRefreshLayout=findViewById(R.id.detail_showpic_swipe);
        recyclerView=findViewById(R.id.detail_showpic_rv);
        floatBtn=findViewById(R.id.detail_showpic_floatBtn);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra(ConstValue.key_pic);
        if(bundle!=null){
            pic = (Pic) bundle.get(ConstValue.key_pic);
            if(pic!=null){
                tv_username.setText(pic.getUsername());
                tv_detail.setText(pic.getStylename());
                tv_likes.setText(pic.getLikes()+"");
                tv_commentsnum.setText(pic.getCommentsnum()+"");
                RequestOptions options1 = new RequestOptions().centerCrop();
                RequestOptions options2 = new RequestOptions().centerCrop().dontAnimate();
                //RequestOptions options = new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(Utility.dp2px(this,25))).error(R.drawable.background_all_round).placeholder(R.drawable.background_all_round);
                Glide.with(this).load(ConstValue.url_picAfter(pic.getPicname())).apply(options1).into(iv_pic);
                if(!pic.getUserpic().equals("null")){
                    Glide.with(this).load(ConstValue.url_picUser(pic.getUserpic())).apply(options2).into(cv_user);
                }

            }
        }


        commentList=new ArrayList<>();
        commentsAdapter=new CommentsAdapter(this,getWindow(),commentList);
        commentsAdapter.setOnItemClickListener(new CommentsAdapter.mOnItemClickListener() {
            @Override
            public void onClick(CommentsAdapter.ViewHolder viewHolder, int position) {
                BasicUserInfo basicUserInfo=new BasicUserInfo();
                basicUserInfo.setAccount(commentList.get(position).getAccount());
                basicUserInfo.setUserpic(commentList.get(position).getUserpic());
                basicUserInfo.setUsername(commentList.get(position).getUsername());
                Intent intent1=new Intent(ActivityDetail.this,ActivityUserInfo.class);



                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(ActivityDetail.this,

                        new Pair<View, String>(viewHolder.username,getResources().getString(R.string.s_userName)),
                        new Pair<View, String>(viewHolder.userpic,getResources().getString(R.string.s_userPic))

                ).toBundle();
                intent1.putExtra(ConstValue.bundle_user,basicUserInfo);
                startActivity(intent1,bundle);


            }
        });
        iv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BasicUserInfo basicUserInfo=new BasicUserInfo();
                basicUserInfo.setAccount(pic.getAccount());
                basicUserInfo.setUserpic(pic.getUserpic());
                basicUserInfo.setUsername(pic.getUsername());
                Intent intent1=new Intent(ActivityDetail.this,ActivityUserInfo.class);

                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(ActivityDetail.this,

                        new Pair<View, String>(tv_username,getResources().getString(R.string.s_userName)),
                        new Pair<View, String>(cv_user,getResources().getString(R.string.s_userPic))

                ).toBundle();
                intent1.putExtra(ConstValue.bundle_user,basicUserInfo);
                startActivity(intent1,bundle);
            }
        });


        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(commentsAdapter);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemViewCacheSize(0);//设置最大缓存数目
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(hasMore&&lastVisibleItem+1==commentList.size()&&commentList.get(lastVisibleItem).getId()>0){
                    tryGetComments(commentList.get(lastVisibleItem).getFileid(),commentList.get(lastVisibleItem).getId());
                }
                Log.e("GGG","---now:"+lastVisibleItem+"  max:"+commentList.size());
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(layoutManager.findLastVisibleItemPosition()>lastVisibleItem){
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                }

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogWrite dialogWrite = new DialogWrite();

                dialogWrite.setMessage("输入评价内容");
                dialogWrite.setClickListener(new DialogWrite.ClickYes() {
                    @Override
                    public void onClickYes(String data) {
                        if(data.length()<5){
                            Toast.makeText(ActivityDetail.this, "评论内容不能少于5个字", Toast.LENGTH_SHORT).show();
                        }else{
                            tryMakeComments(Long.valueOf(pic.getId()),data);
                        }
                    }
                },null);
                dialogWrite.show(getFragmentManager());
            }
        });

        iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasGetIsLike){
                    isLike=(!isLike);
                    Long likesnum=Long.valueOf(tv_likes.getText()+"");
                    if(isLike){
                        tv_likes.setText((likesnum+1)+"");
                    }else{
                        tv_likes.setText((likesnum-1)+"");
                    }
                    trySendIsLike(Long.valueOf(pic.getId()));
                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        tryGetComments(Long.valueOf(pic.getId()),null);
        tryGetIsLike(Long.valueOf(pic.getId()));
    }

    @Override
    public void onBackPressed() {
        RequestOptions options = new RequestOptions().transforms(new CenterCrop(),new RoundedCorners(Utility.dp2px(this,25))).error(R.drawable.background_all_round).placeholder(R.drawable.background_all_round);
        Glide.with(this).load(ConstValue.url_picAfter(pic.getPicname())).apply(options).into(iv_pic);
        layout.setBackground(getResources().getDrawable(R.drawable.background_halfround));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case msg_noInternet:
                    Toast.makeText(ActivityDetail.this, "无网络连接", Toast.LENGTH_SHORT).show();
                    break;
                case msg_update:
                    Log.e("GGG","更新完毕1");
                    Log.e("GGG",(lastVisibleItem+1)+" "+commentList.size());
                    if(commentList.size()==1){
                        commentsAdapter.notifyDataSetChanged();
                    }else{
                        commentsAdapter.notifyItemRangeChanged(lastVisibleItem+1,commentList.size()-1);
                    }
                    tv_commentsnum.setText(commentList.size()+"");

                    swipeRefreshLayout.setRefreshing(false);
                    if(FragmentLook.picList!=null){
                        for(int i=0;i<FragmentLook.picList.size();i++){
                            Pic pic1=FragmentLook.picList.get(i);
                            if(pic1.getId()==pic.getId()){
                                pic1.setCommentsnum(Long.valueOf(commentList.size()));
                            }
                        }
                    }


                    Log.e("GGG","更新完毕2");
                    //Toast.makeText(getApplicationContext(), "获取评论成功！", Toast.LENGTH_SHORT).show();
                    break;
                case msg_nomore:
                    swipeRefreshLayout.setRefreshing(false);
                    //Toast.makeText(getApplicationContext(), "无评论!", Toast.LENGTH_SHORT).show();
                    break;
                case msg_make:
                    commentsAdapter.notifyItemRangeChanged(0,commentList.size()-1);
                    swipeRefreshLayout.setRefreshing(false);
                    tv_commentsnum.setText(commentList.size()+"");
                    if(getApplicationContext()!=null){
                        Toast.makeText(getApplicationContext(), "发表评论成功！", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case msg_getIsLike:
                    Log.e("GGG","获取是否喜欢:"+isLike);
                    if(isLike){
                        iv_like.setImageDrawable(getDrawable(R.drawable.ic_like));
                    }else{
                        iv_like.setImageDrawable(getDrawable(R.drawable.ic_like_un));
                    }
                    break;
                case msg_sendIsLike:
                    Log.e("GGG","发送是否喜欢:"+isLike);
                    if(isLike){
                        iv_like.setImageDrawable(getDrawable(R.drawable.ic_like));
                    }else{
                        iv_like.setImageDrawable(getDrawable(R.drawable.ic_like_un));
                    }

                    if(FragmentLook.picList!=null){
                        for(int i=0;i<FragmentLook.picList.size();i++){
                            Pic pic1=FragmentLook.picList.get(i);
                            if(pic1.getId()==pic.getId()){
                                if(isLike){
                                    pic1.setLikes(pic1.getLikes()+1);
                                }else{
                                    pic1.setLikes(pic1.getLikes()-1);
                                }
                            }
                        }
                    }

                    break;
            }
        }
    };

    /**
     * 获取自己已经做过的全部图片信息
     */
    public void tryGetComments(Long fileid,Long commentid){
        if(fileid==0){
            //Toast.makeText(this, "无数据!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(loading==true)return;
        loading=true;
        //swipeRefreshLayout.setRefreshing(true);
        String address;
        if(commentid!=null){
            address=ConstValue.url_commentNewestGet(fileid,commentid);
        }else{
            address=ConstValue.url_commentGet(fileid);
        }
        Log.e("GGG",address);
        HttpUtil.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what=msg_noInternet;
                loading=false;
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                JSONArray jsonArray = null;
                Log.e("GGG",responseText);
                try {
                    jsonArray = new JSONArray(responseText);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                        int id=jsonObject.getInt("id");
                        if(id==0){
                            hasMore=false;
                            handler.sendEmptyMessage(msg_nomore);
                            return;
                        }else{
                            long commentid=jsonObject.getLong("id");
                            long fileid=jsonObject.getLong("fileid");
                            long account=jsonObject.getLong("account");
                            String username=jsonObject.getString("username");
                            String userpic=jsonObject.getString("userpic");
                            String content=jsonObject.getString("content");
                            long time=jsonObject.getLong("time");
                            Comment comment=new Comment();
                            comment.setId(commentid);
                            comment.setAccount(account);
                            comment.setContent(content);
                            comment.setFileid(fileid);
                            comment.setUsername(username);
                            comment.setTime(time);
                            comment.setUserpic(userpic);
                            commentList.add(comment);
                        }
                    }
                    Message msg = new Message();
                    msg.what=msg_update;
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                    loading=false;
                }


            }
        });
    }


    /**
     * 获取自己已经做过的全部图片信息
     */
    public void tryMakeComments(Long fileid,String content){
        if(fileid==0){
            //Toast.makeText(this, "无数据!", Toast.LENGTH_SHORT).show();
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        SharedPreferences preferences=getSharedPreferences(ConstValue.sp,MODE_PRIVATE);
        Long  account=preferences.getLong(ConstValue.spAccount,-1l);
        String pwd=preferences.getString(ConstValue.spPassword,"null");
        String address=ConstValue.url_commentShare(account,pwd,content,fileid);
        HttpUtil.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what=msg_noInternet;
                loading=false;
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                JSONArray jsonArray = null;
                Log.e("GGG",responseText);
                if(responseText.length()<=2){
                    Toast.makeText(ActivityDetail.this, "非法评论!", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    commentList.clear();
                    lastVisibleItem=-1;
                    hasMore=true;
                    jsonArray = new JSONArray(responseText);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = (JSONObject) jsonArray.getJSONObject(i);
                        int id=jsonObject.getInt("id");
                        if(id==0){
                            hasMore=false;
                            handler.sendEmptyMessage(msg_nomore);
                            return;
                        }else{
                            long commentid=jsonObject.getLong("id");
                            long fileid=jsonObject.getLong("fileid");
                            long account=jsonObject.getLong("account");
                            String username=jsonObject.getString("username");
                            String userpic=jsonObject.getString("userpic");
                            String content=jsonObject.getString("content");
                            long time=jsonObject.getLong("time");
                            Comment comment=new Comment();
                            comment.setId(commentid);
                            comment.setAccount(account);
                            comment.setContent(content);
                            comment.setFileid(fileid);
                            comment.setUsername(username);
                            comment.setTime(time);
                            comment.setUserpic(userpic);
                            commentList.add(comment);
                        }
                    }
                    Message msg = new Message();
                    msg.what=msg_make;
                    handler.sendMessage(msg);

                } catch (JSONException e) {
                    e.printStackTrace();
                    loading=false;
                }


            }
        });
    }


    /**
     * 获取是否喜欢
     */
    public void tryGetIsLike(Long fileid){
        SharedPreferences preferences=getSharedPreferences(ConstValue.sp,MODE_PRIVATE);
        Long  account=preferences.getLong(ConstValue.spAccount,-1l);
        String pwd=preferences.getString(ConstValue.spPassword,"null");
        String address=ConstValue.url_getIsLike(account,fileid);
        HttpUtil.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what=msg_noInternet;
                loading=false;
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();

                Log.e("GGG",responseText);

                try {
                    Like like= new Gson().fromJson(responseText,Like.class);
                    isLike=like.getIsLike();
                    hasGetIsLike=true;
                    Message msg = new Message();
                    msg.what=msg_getIsLike;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


    /**
     * 获取是否喜欢
     */
    public void trySendIsLike(Long fileid){
        SharedPreferences preferences=getSharedPreferences(ConstValue.sp,MODE_PRIVATE);
        Long  account=preferences.getLong(ConstValue.spAccount,-1l);
        String pwd=preferences.getString(ConstValue.spPassword,"null");
        String address=ConstValue.url_sendIsLike(account,fileid,pwd,isLike);
        HttpUtil.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what=msg_noInternet;
                loading=false;
                handler.sendMessage(msg);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();

                Log.e("GGG",responseText);

                try {
                    Like like= new Gson().fromJson(responseText,Like.class);
                    isLike=like.getIsLike();
                    hasGetIsLike=true;
                    Message msg = new Message();
                    msg.what=msg_sendIsLike;
                    handler.sendMessage(msg);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }


}
