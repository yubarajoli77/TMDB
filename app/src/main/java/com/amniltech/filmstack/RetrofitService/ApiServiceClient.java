package com.amniltech.filmstack.RetrofitService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceClient {
    private static Retrofit mRetrofit;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String POSTER_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w185/";
    public static final String BACKDROP_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";

    public static final String apiKey = "daba08773ced66a094625802579ebbc9";

    public static Retrofit getClient() {
        if (mRetrofit == null) {
            HttpLoggingInterceptor intercepter = new HttpLoggingInterceptor();
            intercepter.setLevel(HttpLoggingInterceptor.Level.BODY);
            HeaderInterceptor headerInterceptor = new HeaderInterceptor();
            OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(intercepter).build();

            Gson gson = new GsonBuilder().setLenient().create();

            mRetrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return mRetrofit;
    }


    private static class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request.Builder requestBuilder = originalRequest.newBuilder()
                    .addHeader("api_key", apiKey);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        }
    }
}
