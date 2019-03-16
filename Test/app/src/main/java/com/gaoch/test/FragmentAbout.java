package com.gaoch.test;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.gaoch.test.util.Blur;
import com.gaoch.test.util.ConstValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class FragmentAbout extends Fragment {
    private int hasBlured_top1=0,hasBlured_top2=0;
    private LinearLayout layout_1,layout_2;
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
    }

}
