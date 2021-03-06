package com.amniltech.filmstack.RoomDatabase.Entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "upcoming_movies")
public class UpComingMovieEntity {
    @PrimaryKey
    private Long movieId;

    @Embedded
    private MovieEntity movieEntity;

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public MovieEntity getMovieEntity() {
        return movieEntity;
    }

    public void setMovieEntity(MovieEntity movieEntity) {
        this.movieEntity = movieEntity;
    }
}
