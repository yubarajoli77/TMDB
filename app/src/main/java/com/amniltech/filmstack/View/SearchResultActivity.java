package com.amniltech.filmstack.View;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.amniltech.filmstack.Model.MovieResponseDto;
import com.amniltech.filmstack.Model.Result;
import com.amniltech.filmstack.R;
import com.amniltech.filmstack.RetrofitService.ApiInterface;
import com.amniltech.filmstack.RetrofitService.ApiServiceClient;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieEntity;
import com.amniltech.filmstack.Util.DataProvider;
import com.amniltech.filmstack.Util.NetworkRequestErrorMapper;
import com.amniltech.filmstack.Util.ShowNoContentPopUp;
import com.amniltech.filmstack.View.CustomAdapter.MovieCustomAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultActivity extends AppCompatActivity {
    private SearchView searchView;
    private RecyclerView rvSearchMovieContainer;
    private ImageButton ibtnHome;
    private final String TAG = getClass().getSimpleName();
    private MovieCustomAdapter movieCustomAdapter = new MovieCustomAdapter();
    public static final String MOVIE_SEARCH_WORD = "MovieSearchWord";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        bindViews();
        int CURRENT_SCREEN_ORIENTATION = DataProvider.getCurrentOrientation(this);
        progressBar.setVisibility(View.GONE);


        ibtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rvSearchMovieContainer.setAdapter(movieCustomAdapter);

        if(CURRENT_SCREEN_ORIENTATION == Configuration.ORIENTATION_LANDSCAPE)
            rvSearchMovieContainer.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        else
            rvSearchMovieContainer.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));


        movieCustomAdapter.setOnItemClickListener(new MovieCustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(MovieEntity movieEntity) {
                Intent movieDetailIntent = new Intent(SearchResultActivity.this,MovieDetailActivity.class);
                movieDetailIntent.putExtra(MovieDetailActivity.MOVIE_ID,movieEntity.getId());
                startActivity(movieDetailIntent);
            }
        });

        searchView.setQueryHint(getResources().getString(R.string.search_movie));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String movieName) {
                updateRecyclerView(movieName);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        Intent intent = getIntent();
        if(intent!=null){
            if(intent.hasExtra(MOVIE_SEARCH_WORD)){
                String movieName = intent.getStringExtra(MOVIE_SEARCH_WORD);
                searchView.setQuery(movieName,true);
            }
        }
    }

    private void bindViews(){
        searchView = findViewById(R.id.sv_as_search_box);
        rvSearchMovieContainer = findViewById(R.id.rv_sr_movie_container);
        ibtnHome = findViewById(R.id.ibtn_sr_home);
        progressBar = findViewById(R.id.pb_sr_loading);
    }

    private void updateRecyclerView(final String movieName) {
        progressBar.setVisibility(View.VISIBLE);
        ApiInterface apiInterface = ApiServiceClient.getClient().create(ApiInterface.class);

        Call<MovieResponseDto> call = apiInterface.searchMovies(movieName,ApiServiceClient.apiKey, "en-US",1
        );
        call.enqueue(new Callback<MovieResponseDto>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponseDto> call,@NonNull Response<MovieResponseDto> response) {
                Log.i(TAG, "Response Code " + response.code());

                if (response.isSuccessful()) {
                    MovieResponseDto movieResponseDto = response.body();
                    if(movieResponseDto!=null && movieResponseDto.getResults().isEmpty())
                        ShowNoContentPopUp.showMessage(SearchResultActivity.this,
                                "Sorry, There are no such movies in our database with keyword "+movieName);
                    if(movieResponseDto!=null){
                        List<MovieEntity> movieEntities = new ArrayList<>();
                        for(Result movie : movieResponseDto.getResults()){
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
                            movieEntities.add(movieEntity);
                        }
                        movieCustomAdapter.setMovieList(movieEntities);
                        movieCustomAdapter.notifyDataSetChanged();
                    }

                    Log.i(TAG, "Response is successful");
                } else {
                    Log.d(TAG, "Response is not successful: " + response.message());
                }

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponseDto> call,@NonNull Throwable t) {
                NetworkRequestErrorMapper.checkErrorResponse(ibtnHome,SearchResultActivity.this);
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "Failed to hit server");
                t.printStackTrace();

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            rvSearchMovieContainer.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        else
            rvSearchMovieContainer.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

    }
}
