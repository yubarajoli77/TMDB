package com.amniltech.filmstack.RoomDatabase;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.amniltech.filmstack.RoomDatabase.Entity.GenreEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieDetailEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieTrailerEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.ProductionCompanyEntity;

import java.util.List;

public class MovieDetailWithGenersAndProCompanies {
    @Embedded
    public MovieDetailEntity movieDetailEntity;

    @Relation(parentColumn = "id",entityColumn = "movieId")
    private List<GenreEntity> genres = null;

    @Relation(parentColumn = "id",entityColumn = "movieId")
    private List<ProductionCompanyEntity> productionCompanies = null;

    @Relation(parentColumn = "id",entityColumn = "movieId")
    private List<MovieTrailerEntity> movieTrailerEntities;

    public List<MovieTrailerEntity> getMovieTrailerEntities() {
        return movieTrailerEntities;
    }

    public void setMovieTrailerEntities(List<MovieTrailerEntity> movieTrailerEntities) {
        this.movieTrailerEntities = movieTrailerEntities;
    }

    public MovieDetailEntity getMovieDetailEntity() {
        return movieDetailEntity;
    }


    public List<GenreEntity> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreEntity> genres) {
        this.genres = genres;
    }

    public List<ProductionCompanyEntity> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompanyEntity> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public void setMovieDetailEntity(MovieDetailEntity movieDetailEntity) {
        this.movieDetailEntity = movieDetailEntity;
    }

}
