package com.amniltech.filmstack.ViewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.amniltech.filmstack.Repository.MovieRepo;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public class MovieViewModel extends AndroidViewModel {
    private Flowable<List<MovieEntity>> popularMovies;
    private Flowable<List<MovieEntity>> latestMovies;
    private Flowable<List<MovieEntity>> showingMovies;
    private Flowable<List<MovieEntity>> upcomingMovies;

    private MovieRepo movieRepo;

    public MovieViewModel(@NonNull Application application, String category) {
        super(application);
        switch (category){
            case MovieRepo.TOP_RATED_MOVIES:
                movieRepo = new MovieRepo(application,category);
                latestMovies = movieRepo.getMovies();

            case MovieRepo.POPULAR_MOVIES:
                movieRepo = new MovieRepo(application,category);
                popularMovies = movieRepo.getMovies();

            case MovieRepo.SHOWING_MOVIES:
                movieRepo = new MovieRepo(application,category);
                showingMovies = movieRepo.getMovies();

            case MovieRepo.UPCOMING_MOVIES:
                movieRepo = new MovieRepo(application,category);
                upcomingMovies = movieRepo.getMovies();

        }

    }


    public Flowable<List<MovieEntity>> getPopularMovies() {
        return popularMovies;
    }
    public Flowable<List<MovieEntity>> getLatestMovies() {
        return latestMovies;
    }
    public Flowable<List<MovieEntity>> getShowingMovies() {
        return showingMovies;
    }
    public Flowable<List<MovieEntity>> getUpcomingMovies() {
        return upcomingMovies;
    }
}
