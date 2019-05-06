package com.gaoch.brilliantpic;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gaoch.brilliantpic.util.Blur;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class FragmentAbout extends Fragment {
    private LinearLayout layout_1,layout_2;
    private Blur.BlurLayout blurLayout1,blurLayout2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_about,container,false);
        layout_1=view.findViewById(R.id.about_li_1);
        layout_2=view.findViewById(R.id.about_li_2);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        layout_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Uri uri = Uri.parse("www.baidu.com");
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                startActivity(intent);
            }
        });


        setBlur();
    }


    public void setBlur(){
        final LinearLayout bkg_layout=((ActivityMain)getActivity()).layout_bkg;
        blurLayout1=new Blur.BlurLayout(layout_1,bkg_layout);
        blurLayout2=new Blur.BlurLayout(layout_2,bkg_layout);
    }

}
