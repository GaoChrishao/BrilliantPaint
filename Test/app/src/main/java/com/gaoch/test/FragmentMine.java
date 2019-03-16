package com.gaoch.test;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaoch.test.adapter.LocalDatabaseHelper;
import com.gaoch.test.adapter.ShowMyPicAdapter;
import com.gaoch.test.myclass.Pic;
import com.gaoch.test.util.ConstValue;

import java.util.List;

public class FragmentMine extends Fragment {
    private RecyclerView recyclerView;
    private ShowMyPicAdapter adapter;
    private List<Pic> picList;
    private ConstraintLayout layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_mine,container,false);
        recyclerView=view.findViewById(R.id.mine_rv);
        layout=view.findViewById(R.id.mine_layout);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        picList = LocalDatabaseHelper.getUserPic(getActivity().getSharedPreferences(ConstValue.sp, Context.MODE_PRIVATE).getString(ConstValue.spAccount,""),
                new LocalDatabaseHelper(getContext(),ConstValue.LocalDatabaseName,null,LocalDatabaseHelper.NEW_VERSION).getReadableDatabase()
                );



        adapter=new ShowMyPicAdapter(getContext(),getActivity().getWindow(), picList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemViewCacheSize(0);//设置最大缓存数目



    }
}
