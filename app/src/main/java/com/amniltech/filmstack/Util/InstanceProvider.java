package com.amniltech.filmstack.Util;

import android.app.Application;
import android.content.Context;

import com.amniltech.filmstack.RoomDatabase.MovieDao;
import com.amniltech.filmstack.RoomDatabase.RoomDatabaseInstance;

public class InstanceProvider {
    public static MovieDao getInstanceOfMovieDao(Application application){
        RoomDatabaseInstance databaseInstance = RoomDatabaseInstance.getRoomDatabaseInstance(application);
        return databaseInstance.getMovieDao();
    }
}
