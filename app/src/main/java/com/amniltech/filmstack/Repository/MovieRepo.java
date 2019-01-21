package com.amniltech.filmstack.Repository;

import android.app.Application;
import android.util.Log;

import com.amniltech.filmstack.RetrofitService.ApiInterface;
import com.amniltech.filmstack.RetrofitService.ApiServiceClient;
import com.amniltech.filmstack.RoomDatabase.Entity.GenreEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieTrailerEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.SimilarMoviesEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.TopRatedMovieEntity;
import com.amniltech.filmstack.RoomDatabase.MovieDao;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieDetailEntity;
import com.amniltech.filmstack.RoomDatabase.MovieDetailWithGenersAndProCompanies;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.PopularMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.ProductionCompanyEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.ShowingMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.UpComingMovieEntity;
import com.amniltech.filmstack.Util.InstanceProvider;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class MovieRepo {
    private Flowable<List<MovieEntity>> movies;
    private Flowable<MovieDetailWithGenersAndProCompanies> movieDetailEntity;
    private Flowable<List<MovieEntity>> similarMovies;

    //Movie Categories
    public static final String POPULAR_MOVIES = "PopularMovies";
    public static final String TOP_RATED_MOVIES = "TopRatedMovies";
    public static final String SHOWING_MOVIES = "ShowingMovies";
    public static final String UPCOMING_MOVIES = "UpcomingMovies";
    public static final String SIMILAR_MOVIES = "SimilarMovies";

    private MovieDao movieDao;
    private RemoteMovieRepo remoteMovieRepo;
    private static final String TAG = MovieRepo.class.getSimpleName();
    private ApiInterface apiInterface = ApiServiceClient.getClient().create(ApiInterface.class);

    //MovieRepo Constructor for access Popular movie lists
    public MovieRepo(Application application, String category) {
        movieDao = InstanceProvider.getInstanceOfMovieDao(application);
        remoteMovieRepo = new RemoteMovieRepo(application);

        switch (category) {
            case POPULAR_MOVIES:
                remoteMovieRepo.fetchPopularMovies();
                movies = movieDao.getAllPopularMovies();
                break;

            case TOP_RATED_MOVIES:
                remoteMovieRepo.fetchLatestMovies();
                movies = movieDao.getAllTopRatedMovies();
                break;

            case SHOWING_MOVIES:
                remoteMovieRepo.fetchShowingMovies();
                movies = movieDao.getAllShowingMovies();
                break;

            case UPCOMING_MOVIES:
                remoteMovieRepo.fetchUpcomingMovies();
                movies = movieDao.getAllUpcomingMovies();
                break;

        }
    }

    //MovieRepo Construtor for database access
    MovieRepo(Application application) {
        movieDao = InstanceProvider.getInstanceOfMovieDao(application);
    }

    //MovieRepo Constructor to access Movie detail by id
    public MovieRepo(Application application, Long movieId) {
        movieDao = InstanceProvider.getInstanceOfMovieDao(application);
        remoteMovieRepo = new RemoteMovieRepo(application);
        remoteMovieRepo.fetchMovieDetail(movieId);
        remoteMovieRepo.fetchSimilarMovies(movieId);
        remoteMovieRepo.fetchMovieTrailer(movieId);
        movieDetailEntity = movieDao.getMovieDetailById(movieId);
        similarMovies = movieDao.getAllSimilarMoviesById(movieId);
    }

    //Get methods to return value obtained in constructor from database
    public Flowable<List<MovieEntity>> getMovies() {
        return movies;
    }
    public Flowable<MovieDetailWithGenersAndProCompanies> getMovieDetailById() {
        return movieDetailEntity;
    }
    public Flowable<List<MovieEntity>> getSimilarMovies(){return similarMovies;}


    //Methods to store data into table in background thread
    void insertGenre(final List<GenreEntity> genreEntities){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                movieDao.insertGenres(genreEntities);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("MovieRepo:","Genre successfully inserted into database");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
    void insertProductionCompanies(final List<ProductionCompanyEntity> companyEntities){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                movieDao.insertProductionCompanies(companyEntities);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("MovieRepo:","Production Companies successfully inserted into database");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
    void insertMovieDetail(final MovieDetailEntity movieDetailEntity){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                movieDao.insertMovieDetailToDb(movieDetailEntity);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("MovieRepo:","Movie Detail successfully inserted into database");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
    void insertMoviesToRespectiveTable(final Object movieObject, final String category) {
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                switch (category) {
                    case POPULAR_MOVIES:
                        movieDao.insertPopularMovies((List<PopularMovieEntity>) movieObject);
                    case TOP_RATED_MOVIES:
                        movieDao.insertTopRatedMovies((List<TopRatedMovieEntity>) movieObject);
                    case SHOWING_MOVIES:
                        movieDao.insertShowingMovies((List<ShowingMovieEntity>) movieObject);
                    case UPCOMING_MOVIES:
                        movieDao.insertUpcomingMovies((List<UpComingMovieEntity>) movieObject);
                    case SIMILAR_MOVIES:
                        movieDao.insertSimilarMovies((List<SimilarMoviesEntity>) movieObject);
                        Log.d("MovieRepo:", "Successfully inserted similar movies");

                    default:
                        Log.d("Failed Insertion", "Failed to insert, provide valid movie category");

                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i("MovieRepo:","New Movies are successfully inserted into database");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
    void insertMovieTrailer(final MovieTrailerEntity movieTrailerEntity){
        Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                movieDao.insertMovieTrailer(movieTrailerEntity);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                    Log.d("MovieRepo:","Movie Trailer is added");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

}
