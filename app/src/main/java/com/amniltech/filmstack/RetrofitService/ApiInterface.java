package com.amniltech.filmstack.RetrofitService;


import com.amniltech.filmstack.Model.MovieDetailDto;
import com.amniltech.filmstack.Model.MovieResponseDto;
import com.amniltech.filmstack.Model.MovieVideoResponseDto;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("movie/popular")
    Call<MovieResponseDto> getPopularToday(@Query("api_key") String api_key,
                                           @Query("language") String language,
                                           @Query("page") int page,
                                           @Query("region") String region);

    @GET("movie/latest")
    Call<MovieResponseDto> getLatestMovies(@Query("api_key") String api_key,
                                           @Query("language") String language);

    @GET("movie/now_playing")
    Call<MovieResponseDto> getNowPlayingMovies(@Query("api_key") String api_key,
                                               @Query("language") String language,
                                               @Query("page") int page,
                                               @Query("region") String region);

    @GET("movie/upcoming")
    Call<MovieResponseDto> getUpcomingMovies(@Query("api_key") String api_key,
                                             @Query("language") String language,
                                             @Query("page") int page,
                                             @Query("region") String region);

    @GET("movie/top_rated")
    Call<MovieResponseDto> getTopRatedMovies(@Query("api_key") String api_key,
                                             @Query("language") String language,
                                             @Query("page") int page,
                                             @Query("region") String region);

    @GET("movie/{movie_id}/similar")
    Call<MovieResponseDto> getSimilarMovies(@Path("movie_id") Long movie_id,
                                               @Query("api_key") String api_key,
                                               @Query("language") String language,
                                               @Query("page") int page);

    @GET("movie/{movie_id}")
    Call<MovieDetailDto> getMovieDetail(@Path("movie_id") Long movie_id,
                                        @Query("api_key") String api_key,
                                        @Query("language") String language);
    @GET("movie/{movie_id}/videos")
    Call<MovieVideoResponseDto> getMovieTrailer(@Path("movie_id") Long movie_id,
                                                @Query("api_key") String api_key,
                                                @Query("language") String language);

    @GET("search/movie")
    Call<MovieResponseDto> searchMovies(@Query("query") String query,
                                            @Query("api_key") String api_key,
                                            @Query("language") String language,
                                            @Query("page") int page);

}
