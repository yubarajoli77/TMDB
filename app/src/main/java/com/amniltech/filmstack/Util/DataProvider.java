package com.amniltech.filmstack.Util;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

public class DataProvider {
    public static int getCurrentOrientation(Activity activity){
        if(activity!=null)
            return activity.getResources().getConfiguration().orientation;
        else
            return -1;
    }

}
