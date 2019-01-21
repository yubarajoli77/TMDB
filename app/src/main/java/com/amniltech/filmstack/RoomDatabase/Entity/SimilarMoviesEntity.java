package com.amniltech.filmstack.RoomDatabase.Entity;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "similar_movies")
public class SimilarMoviesEntity {
    @PrimaryKey
    private Long similarMovieId;

    private Long parentMovieId;

    @Embedded
    private MovieEntity movieEntity;

    public Long getSimilarMovieId() {
        return similarMovieId;
    }

    public void setSimilarMovieId(Long similarMovieId) {
        this.similarMovieId = similarMovieId;
    }

    public Long getParentMovieId() {
        return parentMovieId;
    }

    public void setParentMovieId(Long parentMovieId) {
        this.parentMovieId = parentMovieId;
    }

    public MovieEntity getMovieEntity() {
        return movieEntity;
    }

    public void setMovieEntity(MovieEntity movieEntity) {
        this.movieEntity = movieEntity;
    }
}
