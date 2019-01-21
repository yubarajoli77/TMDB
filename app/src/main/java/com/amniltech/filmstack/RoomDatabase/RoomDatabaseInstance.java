package com.amniltech.filmstack.RoomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.amniltech.filmstack.RoomDatabase.Entity.GenreEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieDetailEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieTrailerEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.PopularMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.ProductionCompanyEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.ShowingMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.SimilarMoviesEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.TopRatedMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.UpComingMovieEntity;

@Database(entities = {PopularMovieEntity.class,TopRatedMovieEntity.class,ShowingMovieEntity.class,
        UpComingMovieEntity.class,SimilarMoviesEntity.class,MovieTrailerEntity.class,
        GenreEntity.class,MovieDetailEntity.class,ProductionCompanyEntity.class},version = 1)

public abstract class RoomDatabaseInstance extends RoomDatabase {
    public abstract MovieDao getMovieDao();
    private static volatile RoomDatabaseInstance roomDatabaseInstance;

    public static RoomDatabaseInstance getRoomDatabaseInstance(final Context context){
        if(roomDatabaseInstance==null){
            synchronized (RoomDatabaseInstance.class){
                if(roomDatabaseInstance==null){
                    roomDatabaseInstance=Room.databaseBuilder(context.getApplicationContext(),
                            RoomDatabaseInstance.class,"tmdb_movies").build();
                }
            }
        }
        return roomDatabaseInstance;
    }
}
