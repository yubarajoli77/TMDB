package com.amniltech.filmstack.Repository;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.amniltech.filmstack.Model.MovieDetailDto;
import com.amniltech.filmstack.Model.MovieResponseDto;
import com.amniltech.filmstack.Model.MovieVideoResponseDto;
import com.amniltech.filmstack.Model.Result;
import com.amniltech.filmstack.RetrofitService.ApiInterface;
import com.amniltech.filmstack.RetrofitService.ApiServiceClient;
import com.amniltech.filmstack.RoomDatabase.Entity.GenreEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieTrailerEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.ShowingMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.SimilarMoviesEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.TopRatedMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieDetailEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.PopularMovieEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.ProductionCompanyEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.UpComingMovieEntity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteMovieRepo {

    private MovieRepo movieRepo;
    private static final String TAG = MovieRepo.class.getSimpleName();
    private ApiInterface apiInterface = ApiServiceClient.getClient().create(ApiInterface.class);

    RemoteMovieRepo(Application application) {
        movieRepo = new MovieRepo(application);
    }

    void fetchMovieDetail(final Long movieId) {
        Call<MovieDetailDto> call = apiInterface.getMovieDetail(movieId, ApiServiceClient.apiKey, "en-US");
        call.enqueue(new Callback<MovieDetailDto>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetailDto> call, @NonNull Response<MovieDetailDto> response) {
                if (response.isSuccessful()) {
                    Log.d("OnResponse::", "Called");
                    MovieDetailDto movieDetailDto = response.body();
                    MovieDetailEntity movieDetailEntity = new MovieDetailEntity();

                    if(movieDetailDto!=null){
                        movieDetailEntity.setAdult(movieDetailDto.getAdult());
                        movieDetailEntity.setBackdropPath(movieDetailDto.getBackdropPath());
                        movieDetailEntity.setPosterPath(movieDetailDto.getPosterPath());
                        movieDetailEntity.setBudget(movieDetailDto.getBudget());

                        List<GenreEntity> genreEntities = new ArrayList<>();
                        for (MovieDetailDto.Genre genre : movieDetailDto.getGenres()) {
                            GenreEntity genreEntity = new GenreEntity();
                            genreEntity.setId(genre.getId());
                            genreEntity.setMovieId(movieDetailDto.getId());
                            genreEntity.setName(genre.getName());
                            genreEntities.add(genreEntity);
                        }

                        movieDetailEntity.setHomepage(movieDetailDto.getHomepage());
                        movieDetailEntity.setId(movieDetailDto.getId());
                        movieDetailEntity.setReleaseDate(movieDetailDto.getReleaseDate());
                        movieDetailEntity.setImdbId(movieDetailDto.getImdbId());
                        movieDetailEntity.setOriginalLanguage(movieDetailDto.getImdbId());
                        movieDetailEntity.setTitle(movieDetailDto.getTitle());
                        movieDetailEntity.setOriginalTitle(movieDetailDto.getOriginalTitle());
                        movieDetailEntity.setOverview(movieDetailDto.getOverview());
                        movieDetailEntity.setPopularity(movieDetailDto.getPopularity());
                        movieDetailEntity.setRevenue(movieDetailDto.getRevenue());
                        movieDetailEntity.setRuntime(movieDetailDto.getRuntime());
                        movieDetailEntity.setVoteAverage(movieDetailDto.getVoteAverage());
                        movieDetailEntity.setVoteCount(movieDetailDto.getVoteCount());

                        List<ProductionCompanyEntity> productionCompanyEntityList = new ArrayList<>();
                        for (MovieDetailDto.ProductionCompany productionCompany : movieDetailDto.getProductionCompanies()) {
                            ProductionCompanyEntity companyEntity = new ProductionCompanyEntity();
                            companyEntity.setId(productionCompany.getId());
                            companyEntity.setMovieId(movieDetailDto.getId());
                            companyEntity.setOriginCountry(productionCompany.getOriginCountry());
                            companyEntity.setName(productionCompany.getName());
                            companyEntity.setLogoPath(productionCompany.getLogoPath());
                            productionCompanyEntityList.add(companyEntity);
                        }

                        movieRepo.insertMovieDetail(movieDetailEntity);
                        movieRepo.insertGenre(genreEntities);
                        movieRepo.insertProductionCompanies(productionCompanyEntityList);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetailDto> call, @NonNull Throwable t) {
                Log.d("RemoMovRepo:", "Server Failed");

            }
        });

    }

    void fetchSimilarMovies(final Long movieId) {
        Call<MovieResponseDto> call = apiInterface.getSimilarMovies(movieId, ApiServiceClient.apiKey, "en-US", 1);
        call.enqueue(new Callback<MovieResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponseDto> call, @NonNull Response<MovieResponseDto> response) {
                Log.i(TAG, "Response Code " + response.code());

                if (response.isSuccessful()) {
                    MovieResponseDto movieResponseDto = response.body();
                    List<SimilarMoviesEntity> similarMoviesEntities = new ArrayList<>();

                    if(movieResponseDto!=null && !movieResponseDto.getResults().isEmpty()){
                        for (Result movie : movieResponseDto.getResults()) {
                            SimilarMoviesEntity similarMoviesEntity = new SimilarMoviesEntity();
                            MovieEntity movieEntity = new MovieEntity();
                            movieEntity.setId(movie.getId());
                            movieEntity.setAdult(movie.getAdult());
                            movieEntity.setBackdropPath(movie.getBackdropPath());
                            movieEntity.setOriginalLanguage(movie.getOriginalLanguage());
                            movieEntity.setOriginalTitle(movie.getOriginalTitle());
                            movieEntity.setOverview(movie.getOverview());
                            movieEntity.setPopularity(movie.getPopularity());
                            movieEntity.setReleaseDate(movie.getReleaseDate());
                            movieEntity.setTitle(movie.getTitle());
                            movieEntity.setVideo(movie.getVideo());
                            movieEntity.setVoteAverage(movie.getVoteAverage());
                            movieEntity.setVoteCount(movie.getVoteCount());
                            movieEntity.setPosterPath(movie.getPosterPath());
                            similarMoviesEntity.setSimilarMovieId(movie.getId());
                            similarMoviesEntity.setParentMovieId(movieId);
                            similarMoviesEntity.setMovieEntity(movieEntity);
                            similarMoviesEntities.add(similarMoviesEntity);
                        }

                    }
                    movieRepo.insertMoviesToRespectiveTable(similarMoviesEntities, MovieRepo.SIMILAR_MOVIES);
                    Log.i(TAG, "Response is successful");
                } else {
                    Log.d(TAG, "Response is not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponseDto> call, @NonNull Throwable t) {
                Log.d(TAG, "Failed to hit server");
                t.printStackTrace();

            }
        });
    }

    void fetchPopularMovies() {
        Call<MovieResponseDto> call = apiInterface.getPopularToday(ApiServiceClient.apiKey, "en-US", 1, " ");
        call.enqueue(new Callback<MovieResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponseDto> call, @NonNull Response<MovieResponseDto> response) {
                Log.i(TAG, "Response Code " + response.code());

                if (response.isSuccessful()) {
                    MovieResponseDto movieResponseDto = response.body();
                    List<PopularMovieEntity> popularMovieEntities = new ArrayList<>();

                    if(movieResponseDto!=null && !movieResponseDto.getResults().isEmpty()){
                        for (Result movie : movieResponseDto.getResults()) {
                            PopularMovieEntity popularMovieEntity = new PopularMovieEntity();
                            MovieEntity movieEntity = new MovieEntity();
                            movieEntity.setId(movie.getId());
                            movieEntity.setAdult(movie.getAdult());
                            movieEntity.setBackdropPath(movie.getBackdropPath());
                            movieEntity.setOriginalLanguage(movie.getOriginalLanguage());
                            movieEntity.setOriginalTitle(movie.getOriginalTitle());
                            movieEntity.setOverview(movie.getOverview());
                            movieEntity.setPopularity(movie.getPopularity());
                            movieEntity.setReleaseDate(movie.getReleaseDate());
                            movieEntity.setTitle(movie.getTitle());
                            movieEntity.setVideo(movie.getVideo());
                            movieEntity.setVoteAverage(movie.getVoteAverage());
                            movieEntity.setVoteCount(movie.getVoteCount());
                            movieEntity.setPosterPath(movie.getPosterPath());
                            popularMovieEntity.setMovieId(movie.getId());
                            popularMovieEntity.setMovieEntity(movieEntity);
                            popularMovieEntities.add(popularMovieEntity);
                        }
                    }


                    movieRepo.insertMoviesToRespectiveTable(popularMovieEntities, MovieRepo.POPULAR_MOVIES);
                    Log.i(TAG, "Response is successful");
                } else {
                    Log.d(TAG, "Response is not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponseDto> call, @NonNull Throwable t) {
                Log.d(TAG, "Failed to hit server");
                t.printStackTrace();

            }
        });
    }

    void fetchLatestMovies() {
        Call<MovieResponseDto> call = apiInterface.getTopRatedMovies(ApiServiceClient.apiKey, "en-US", 1, "");
        call.enqueue(new Callback<MovieResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponseDto> call, @NonNull Response<MovieResponseDto> response) {
                Log.i(TAG, "Response Code " + response.code());

                if (response.isSuccessful()) {
                    MovieResponseDto movieResponseDto = response.body();
                    List<TopRatedMovieEntity> latestMovieEntities = new ArrayList<>();

                    if(movieResponseDto!=null && !movieResponseDto.getResults().isEmpty()){
                        for (Result movie : movieResponseDto.getResults()) {
                            TopRatedMovieEntity topRatedMovieEntity = new TopRatedMovieEntity();
                            MovieEntity movieEntity = new MovieEntity();
                            movieEntity.setId(movie.getId());
                            movieEntity.setAdult(movie.getAdult());
                            movieEntity.setBackdropPath(movie.getBackdropPath());
                            movieEntity.setOriginalLanguage(movie.getOriginalLanguage());
                            movieEntity.setOriginalTitle(movie.getOriginalTitle());
                            movieEntity.setOverview(movie.getOverview());
                            movieEntity.setPopularity(movie.getPopularity());
                            movieEntity.setReleaseDate(movie.getReleaseDate());
                            movieEntity.setTitle(movie.getTitle());
                            movieEntity.setVideo(movie.getVideo());
                            movieEntity.setVoteAverage(movie.getVoteAverage());
                            movieEntity.setVoteCount(movie.getVoteCount());
                            movieEntity.setPosterPath(movie.getPosterPath());
                            topRatedMovieEntity.setMovieId(movie.getId());
                            topRatedMovieEntity.setMovieEntity(movieEntity);
                            latestMovieEntities.add(topRatedMovieEntity);
                        }

                    }

                    movieRepo.insertMoviesToRespectiveTable(latestMovieEntities, MovieRepo.TOP_RATED_MOVIES);
                    Log.i(TAG, "Response is successful");
                } else {
                    Log.d(TAG, "Response is not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponseDto> call,@NonNull Throwable t) {
                Log.d(TAG, "Failed to hit server");
                t.printStackTrace();

            }
        });
    }

    void fetchShowingMovies() {
        Call<MovieResponseDto> call = apiInterface.getNowPlayingMovies(ApiServiceClient.apiKey, "en-US", 1, "");
        call.enqueue(new Callback<MovieResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponseDto> call, @NonNull Response<MovieResponseDto> response) {
                Log.i(TAG, "Response Code " + response.code());

                if (response.isSuccessful()) {
                    MovieResponseDto movieResponseDto = response.body();
                    List<ShowingMovieEntity> showingMovieEntities = new ArrayList<>();

                    if(movieResponseDto!=null && !movieResponseDto.getResults().isEmpty()){
                        for (Result movie : movieResponseDto.getResults()) {
                            ShowingMovieEntity showingMovieEntity = new ShowingMovieEntity();
                            MovieEntity movieEntity = new MovieEntity();
                            movieEntity.setId(movie.getId());
                            movieEntity.setAdult(movie.getAdult());
                            movieEntity.setBackdropPath(movie.getBackdropPath());
                            movieEntity.setOriginalLanguage(movie.getOriginalLanguage());
                            movieEntity.setOriginalTitle(movie.getOriginalTitle());
                            movieEntity.setOverview(movie.getOverview());
                            movieEntity.setPopularity(movie.getPopularity());
                            movieEntity.setReleaseDate(movie.getReleaseDate());
                            movieEntity.setTitle(movie.getTitle());
                            movieEntity.setVideo(movie.getVideo());
                            movieEntity.setVoteAverage(movie.getVoteAverage());
                            movieEntity.setVoteCount(movie.getVoteCount());
                            movieEntity.setPosterPath(movie.getPosterPath());
                            showingMovieEntity.setMovieId(movie.getId());
                            showingMovieEntity.setMovieEntity(movieEntity);
                            showingMovieEntities.add(showingMovieEntity);
                        }

                    }
                    movieRepo.insertMoviesToRespectiveTable(showingMovieEntities, MovieRepo.SHOWING_MOVIES);
                    Log.i(TAG, "Response is successful");
                } else {
                    Log.d(TAG, "Response is not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponseDto> call,@NonNull Throwable t) {
                Log.d(TAG, "Failed to hit server");
                t.printStackTrace();

            }
        });
    }

    void fetchUpcomingMovies() {
        Call<MovieResponseDto> call = apiInterface.getUpcomingMovies(ApiServiceClient.apiKey, "en-US", 1, "");
        call.enqueue(new Callback<MovieResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponseDto> call, @NonNull Response<MovieResponseDto> response) {
                Log.i(TAG, "Response Code " + response.code());

                if (response.isSuccessful()) {
                    MovieResponseDto movieResponseDto = response.body();
                    List<UpComingMovieEntity> upComingMovieEntities = new ArrayList<>();

                    if (movieResponseDto != null && !movieResponseDto.getResults().isEmpty()) {
                        for (Result movie : movieResponseDto.getResults()) {
                            UpComingMovieEntity upComingMovieEntity = new UpComingMovieEntity();
                            MovieEntity movieEntity = new MovieEntity();
                            movieEntity.setId(movie.getId());
                            movieEntity.setAdult(movie.getAdult());
                            movieEntity.setBackdropPath(movie.getBackdropPath());
                            movieEntity.setOriginalLanguage(movie.getOriginalLanguage());
                            movieEntity.setOriginalTitle(movie.getOriginalTitle());
                            movieEntity.setOverview(movie.getOverview());
                            movieEntity.setPopularity(movie.getPopularity());
                            movieEntity.setReleaseDate(movie.getReleaseDate());
                            movieEntity.setTitle(movie.getTitle());
                            movieEntity.setVideo(movie.getVideo());
                            movieEntity.setVoteAverage(movie.getVoteAverage());
                            movieEntity.setVoteCount(movie.getVoteCount());
                            movieEntity.setPosterPath(movie.getPosterPath());
                            upComingMovieEntity.setMovieId(movie.getId());
                            upComingMovieEntity.setMovieEntity(movieEntity);
                            upComingMovieEntities.add(upComingMovieEntity);
                        }
                    }
                    movieRepo.insertMoviesToRespectiveTable(upComingMovieEntities, MovieRepo.UPCOMING_MOVIES);
                    Log.i(TAG, "Response is successful");
                } else {
                    Log.d(TAG, "Response is not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponseDto> call,@NonNull Throwable t) {
                Log.d(TAG, "Failed to hit server");
                t.printStackTrace();

            }
        });
    }

    void fetchMovieTrailer(Long movieId) {
        Call<MovieVideoResponseDto> call = apiInterface.getMovieTrailer(movieId, ApiServiceClient.apiKey, "en-US");
        call.enqueue(new Callback<MovieVideoResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<MovieVideoResponseDto> call, @NonNull Response<MovieVideoResponseDto> response) {
                Log.i(TAG, "Response Code " + response.code());

                if (response.isSuccessful()) {
                    MovieVideoResponseDto movieVideoResponseDto = response.body();
                    if (movieVideoResponseDto != null && !movieVideoResponseDto.getResults().isEmpty()) {
                        MovieTrailerEntity movieTrailerEntity = new MovieTrailerEntity();
                        movieTrailerEntity.setId(movieVideoResponseDto.getResults().get(0).getId());
                        movieTrailerEntity.setKey(movieVideoResponseDto.getResults().get(0).getKey());
                        movieTrailerEntity.setMovieId(movieVideoResponseDto.getId());
                        movieRepo.insertMovieTrailer(movieTrailerEntity);
                        Log.i(TAG, "Movie Trailer Response is successful");
                    }

                } else {
                    Log.d(TAG, " Movie Trailer Response is not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieVideoResponseDto> call,@NonNull Throwable t) {
                Log.d(TAG, "Failed to hit server");
                t.printStackTrace();

            }
        });
    }

}
