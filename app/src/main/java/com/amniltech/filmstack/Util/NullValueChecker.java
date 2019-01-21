package com.amniltech.filmstack.Util;

import android.support.v7.app.ActionBar;

public class NullValueChecker {
    public static void setUpSupportActionBar(ActionBar supportActionBar,String title,Boolean showBackBtn){
        if(supportActionBar!=null){
            supportActionBar.setTitle(title);
            supportActionBar.setDisplayHomeAsUpEnabled(showBackBtn);
        }
    }
}
