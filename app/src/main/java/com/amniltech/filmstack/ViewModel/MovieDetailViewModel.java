package com.amniltech.filmstack.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

import com.amniltech.filmstack.Repository.MovieRepo;
import com.amniltech.filmstack.RoomDatabase.MovieDetailWithGenersAndProCompanies;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieEntity;

import java.util.List;

import io.reactivex.Flowable;

public class MovieDetailViewModel extends AndroidViewModel {
    private Flowable<MovieDetailWithGenersAndProCompanies> movieDetailDto;
    private Flowable<List<MovieEntity>> similarMovies;

    MovieDetailViewModel(@NonNull Application application, Long movieId) {
        super(application);
        MovieRepo movieRepo = new MovieRepo(application,movieId);
        movieDetailDto = movieRepo.getMovieDetailById();
        similarMovies = movieRepo.getSimilarMovies();
    }

    public Flowable<MovieDetailWithGenersAndProCompanies> getMovieDetailById() {
        return movieDetailDto;
    }
    public Flowable<List<MovieEntity>> getSimilarMovies(){return similarMovies;}
}
