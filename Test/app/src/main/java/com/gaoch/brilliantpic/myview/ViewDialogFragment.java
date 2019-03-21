package com.gaoch.brilliantpic.myview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gaoch.brilliantpic.R;

public class ViewDialogFragment extends DialogFragment {
        private ClickShare clickShare;
        private ClickBack clickBack;
        private TextView tv;
        String imgpaht;

        public void show(FragmentManager fragmentManager) {
            show(fragmentManager, "ViewDialogFragment");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_sharepic, null);
            builder.setView(view);
            Button back = view.findViewById(R.id.dialog_sharepic_btn_back);
            Button share = view.findViewById(R.id.dialog_sharepic_btn_share);
            tv=view.findViewById(R.id.dialog_sharepic_tv_path);
            tv.setText("保存路径:"+imgpaht);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickShare.onClickShare();
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickBack.onClickLike();
                    dismiss();
                }
            });
            return builder.create();
        }

        public void setMessage(String msg){
            imgpaht=msg;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

        public void setClickListener(ClickShare clickShare, ClickBack clickBack){
            if(this.clickShare==null){
                this.clickShare=clickShare;
            }
            if(this.clickBack ==null){
                this.clickBack = clickBack;
            }
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            clickBack =null;
            clickShare=null;
        }



    public interface ClickShare {
        void onClickShare();
    }
    public interface ClickBack {
        void onClickLike();
    }
}
