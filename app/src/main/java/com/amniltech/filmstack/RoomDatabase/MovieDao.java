package com.amniltech.filmstack.RoomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.amniltech.filmstack.RoomDatabase.Entity.GenreEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieDetailEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieTrailerEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.PopularMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.ProductionCompanyEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.ShowingMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.SimilarMoviesEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.TopRatedMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.UpComingMovieEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface MovieDao {
    //Dao methods for Popular movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPopularMovies(List<PopularMovieEntity> movieList);

    @Query("SELECT * FROM popular_movies")
    Flowable<List<MovieEntity>> getAllPopularMovies();

    //Dao methods for movie details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieDetailToDb(MovieDetailEntity movieDetailEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGenres(List<GenreEntity> genreEntities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProductionCompanies(List<ProductionCompanyEntity> productionCompanyEntities);

    @Query("SELECT * FROM movie_detail_table WHERE id=:id")
    Flowable<MovieDetailWithGenersAndProCompanies> getMovieDetailById(Long id);

    //Dao methods for Top Rated Movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopRatedMovies(List<TopRatedMovieEntity> movieList);

    @Query("SELECT * FROM top_rated_movies")
    Flowable<List<MovieEntity>> getAllTopRatedMovies();


    //Dao methods for Showing Movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertShowingMovies(List<ShowingMovieEntity> movieList);

    @Query("SELECT * FROM showing_movies")
    Flowable<List<MovieEntity>> getAllShowingMovies();


    //Dao methods for Upcoming Movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpcomingMovies(List<UpComingMovieEntity> movieList);

    @Query("SELECT * FROM upcoming_movies")
    Flowable<List<MovieEntity>> getAllUpcomingMovies();

    //Dao methods for Similar movies
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSimilarMovies(List<SimilarMoviesEntity> movieList);

    @Query("SELECT * FROM similar_movies WHERE parentMovieId=:movieId")
    Flowable<List<MovieEntity>> getAllSimilarMoviesById(Long movieId);

    //Dao methods for Movie Trailer
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMovieTrailer(MovieTrailerEntity movieList);

}
