package com.gaoch.test.myview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.gaoch.test.R;
import com.google.android.material.textfield.TextInputEditText;

public class DialogWrite extends DialogFragment {
        private ClickYes clickYes;
        private ClickNo clickNo;
        private TextInputEditText tv;
        String imgpaht;

        public void show(FragmentManager fragmentManager) {
            show(fragmentManager, "ViewDialogFragment");
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_comment, null);
            builder.setView(view);
            Button back = view.findViewById(R.id.dialog_write_no);
            Button share = view.findViewById(R.id.dialog_write_yes);
            tv=view.findViewById(R.id.dialog_write_ed);


            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickYes!=null){
                        clickYes.onClickYes(tv.getText().toString()+"");
                        Log.e("GGG",tv.getText().toString());
                    }
                    Log.e("GGG",tv.getText().toString());
                    dismiss();

                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(clickNo!=null){
                        clickNo.onClickNo();
                    }

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

        public void setClickListener(ClickYes clickYes, ClickNo clickNo){
            if(this.clickYes ==null){
                this.clickYes = clickYes;
            }
            if(this.clickNo ==null){
                this.clickNo = clickNo;
            }
        }


        @Override
        public void onDestroy() {
            super.onDestroy();
            clickNo =null;
            clickYes =null;
        }



    public interface ClickYes {
        void onClickYes(String data);
    }
    public interface ClickNo {
        void onClickNo();
    }
}
