package com.amniltech.filmstack.Util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amniltech.filmstack.R;

public class NetworkRequestErrorMapper {
    //Check the error, and  check if internet or wifi is available or not
    public static void checkErrorResponse(View layoutView, final Context context) {
        CheckInternet checkInternet = new CheckInternet();
        if(context!=null){
            if (!checkInternet.isNetworkAvailable(context)) {
                Snackbar snackbar = Snackbar
                        .make(layoutView, R.string.wifi_off_msg, Snackbar.LENGTH_LONG)
                        .setActionTextColor(context.getResources().getColor(R.color.colorWhite))
                        .setAction(R.string.goto_wifi_setting, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((Activity)context).startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        });
                snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                snackbar.show();

            } else if (!(checkInternet.isOnline())) {
                Toast.makeText(context, R.string.no_internet_available, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, R.string.something_worng_with_server, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
