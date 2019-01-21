package com.amniltech.filmstack.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.amniltech.filmstack.R;

public class ShowNoContentPopUp {
    public static void showMessage(Context mContext, String message) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);

        dialogBuilder.setMessage(message);
        dialogBuilder.setTitle("OOPS!!!");
        final AlertDialog alertDialog = dialogBuilder.create();
        if(alertDialog.getWindow()!=null){
            alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationUpBottom;
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

//        alertDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}
