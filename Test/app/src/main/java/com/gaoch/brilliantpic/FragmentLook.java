package com.gaoch.brilliantpic;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gaoch.brilliantpic.adapter.ShowPicAdapter;
import com.gaoch.brilliantpic.myclass.Pic;
import com.gaoch.brilliantpic.util.ConstValue;
import com.gaoch.brilliantpic.util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FragmentLook extends Fragment {
    private RecyclerView recyclerView;
    private ShowPicAdapter adapter;
    public static List<Pic> picList;
    private ConstraintLayout layout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final int msg_noInternet=0;
    private final int msg_update=1;
    private final int msg_nomore=2;

    private  boolean hasMore = true; // 是否还有
    // 若是上拉加载更多的网络请求 则不需要删除数据
    private boolean isLoadingMore = false;
    // 最后一个条目位置
    private  int lastVisibleItem = -1;
    private  boolean loading=false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_look,container,false);
        recyclerView=view.findViewById(R.id.look_rv);
        swipeRefreshLayout=view.findViewById(R.id.look_sr);
        layout=view.findViewById(R.id.look_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picList=new ArrayList<>();

        adapter=new ShowPicAdapter(getContext(),getActivity().getWindow(), picList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(0);//设置最大缓存数目

        adapter.setOnItemClickListener(new ShowPicAdapter.mOnItemClickListener() {
            @Override
            public void onClick(ShowPicAdapter.ViewHolder viewHolder, int position) {
                Intent intent = new Intent(getContext(),ActivityDetail.class);
                Pic pic = picList.get(position);
                Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        new Pair<View, String>(viewHolder.des,getResources().getString(R.string.s_des)),
                        new Pair<View, String>(viewHolder.iv_pic,getResources().getString(R.string.s_pic)),
                        new Pair<View, String>(viewHolder.user_name,getResources().getString(R.string.s_userName)),
                        new Pair<View, String>(viewHolder.user_pic,getResources().getString(R.string.s_userPic))
                        ,new Pair<View, String>(viewHolder.layout1,getResources().getString(R.string.s_layout1))
                        ).toBundle();
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable(ConstValue.key_pic,pic);
                intent.putExtra(ConstValue.key_pic,bundle1);
                startActivity(intent,bundle);


            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(hasMore&&lastVisibleItem+1==picList.size()&&picList.get(lastVisibleItem).getId()>0){
                    tryGetUserfile(picList.get(lastVisibleItem).getId());
                }
                Log.e("GGG","now:"+lastVisibleItem+"  max:"+picList.size());
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
        tryGetUserfile(-1);


    }
    /**
     * 获取自己已经做过的全部图片信息
     */
    public void tryGetUserfile(int id){
        if(id==0){
            Toast.makeText(getContext(), "无数据!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(loading==true)return;
        loading=true;
        Log.e("GGG","---------------------"+id);
        swipeRefreshLayout.setRefreshing(true);
        String address=ConstValue.url_newestUserfiles(id);
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
                        }else{
                            long account=jsonObject.getLong("account");
                            String username=jsonObject.getString("username");
                            String stylename=jsonObject.getString("stylename");
                            String picname=jsonObject.getString("picname");
                            String userpic=jsonObject.getString("userpic");
                            long time=jsonObject.getLong("time");
                            long likes=jsonObject.getLong("likes");
                            long commentsnum=jsonObject.getLong("commentsnum");
                            Pic pic=new Pic();
                            pic.setId(id);
                            pic.setUsername(username);
                            pic.setStylename(stylename);
                            pic.setPicname(picname);
                            pic.setAccount(account);
                            pic.setTime(time);
                            pic.setUserpic(userpic);
                            pic.setCommentsnum(commentsnum);
                            pic.setLikes(likes);

                            picList.add(pic);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loading=false;
                Message msg = new Message();
                msg.what=msg_update;
                handler.sendMessage(msg);
            }
        });
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case msg_noInternet:
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "请检查网络连接！", Toast.LENGTH_SHORT).show();
                    break;
                case msg_update:
                    adapter.notifyItemRangeChanged(lastVisibleItem+1,picList.size());
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "获取数据成功！", Toast.LENGTH_SHORT).show();
                    break;
                case msg_nomore:
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "无数据!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



}
