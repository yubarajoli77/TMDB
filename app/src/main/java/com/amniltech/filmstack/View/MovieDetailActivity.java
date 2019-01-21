package com.amniltech.filmstack.View;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amniltech.filmstack.Model.MovieVideoResponseDto;
import com.amniltech.filmstack.Repository.RemoteMovieRepo;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieEntity;
import com.amniltech.filmstack.Util.ShowNoContentPopUp;
import com.amniltech.filmstack.View.CustomAdapter.MovieCustomAdapter;
import com.amniltech.filmstack.View.CustomAdapter.ProComLogoCustomAdapter;
import com.amniltech.filmstack.R;
import com.amniltech.filmstack.RetrofitService.ApiServiceClient;
import com.amniltech.filmstack.RoomDatabase.Entity.GenreEntity;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieDetailEntity;
import com.amniltech.filmstack.RoomDatabase.MovieDetailWithGenersAndProCompanies;
import com.amniltech.filmstack.Util.NullValueChecker;
import com.amniltech.filmstack.ViewModel.CustomMovieDetailViewModelFactory;
import com.amniltech.filmstack.ViewModel.MovieDetailViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_ID = "MovieId";
    private ImageView ivBackgroudImage;
    private ImageView ivMoviePoster;
    private TextView tvMovieName;
    private TextView tvTagline;
    private TextView tvReleaseYear;
    private TextView tvRating;
    private TextView tvOverview;
    private TextView tvDuration;
    private TextView tvRevenue;
    private TextView tvBudget;
    private RecyclerView rvSimilarMoviesContainer;
    private TextView tvGener;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MovieDetailViewModel movieDetailViewModel;
    private ProComLogoCustomAdapter logoCustomAdapter = new ProComLogoCustomAdapter();
    private MovieCustomAdapter movieCustomAdapter = new MovieCustomAdapter();
    private ImageButton ibtnPlayVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = findViewById(R.id.tb_movie_detail_toolbar);
        setSupportActionBar(toolbar);

        NullValueChecker.setUpSupportActionBar(getSupportActionBar(), "", true);

        bindViews();
        final Long movieId = getIntent().getLongExtra(MOVIE_ID, 0);

        movieDetailViewModel = ViewModelProviders.of(this,
                new CustomMovieDetailViewModelFactory(this.getApplication(), movieId)).get(MovieDetailViewModel.class);

        rvSimilarMoviesContainer.setAdapter(movieCustomAdapter);
        rvSimilarMoviesContainer.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        movieCustomAdapter.setOnItemClickListener(new MovieCustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(MovieEntity movieEntity) {
                Intent movieDetailIntent = new Intent(MovieDetailActivity.this,MovieDetailActivity.class);
                movieDetailIntent.putExtra(MovieDetailActivity.MOVIE_ID,movieEntity.getId());
                startActivity(movieDetailIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        compositeDisposable.add(movieDetailViewModel.getMovieDetailById()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<MovieDetailWithGenersAndProCompanies>() {
                    @Override
                    public void onNext(MovieDetailWithGenersAndProCompanies movieDetailEntity) {
                        updateUI(movieDetailEntity);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

        compositeDisposable.add(movieDetailViewModel.getSimilarMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new ResourceSubscriber<List<MovieEntity>>() {
                    @Override
                    public void onNext(List<MovieEntity> movieEntityList) {
                        movieCustomAdapter.setMovieList(movieEntityList);
                        movieCustomAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));

    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    private void bindViews() {
        ivBackgroudImage = findViewById(R.id.iv_movie_detail_bg);
        ivMoviePoster = findViewById(R.id.iv_movie_detail_poster);
        tvMovieName = findViewById(R.id.tv_movie_detail_name);
        tvTagline = findViewById(R.id.tv_movie_detail_tagline);
        tvRating = findViewById(R.id.tv_movie_detail_rating);
        tvGener = findViewById(R.id.tv_movie_detail_gener);
        tvOverview = findViewById(R.id.tv_movie_detail_overview);
        tvReleaseYear = findViewById(R.id.tv_movie_detail_release_date);
        tvRevenue = findViewById(R.id.tv_movie_detail_revenue);
        tvBudget = findViewById(R.id.tv_movie_detail_budget);
        tvDuration = findViewById(R.id.tv_movie_detail_run_time);
        rvSimilarMoviesContainer = findViewById(R.id.rv_movie_detail_similar_movie_container);
        ibtnPlayVideo = findViewById(R.id.ibtn_play_video);

    }

    private void updateUI(final MovieDetailWithGenersAndProCompanies movieDetailWithGenersAndProCompanies) {
        MovieDetailEntity movieDetailEntity = movieDetailWithGenersAndProCompanies.getMovieDetailEntity();
        if (movieDetailEntity.getBackdropPath() != null)
            Picasso.get().load(ApiServiceClient.BACKDROP_IMAGE_BASE_URL + movieDetailEntity.getBackdropPath())
                    .placeholder(R.drawable.test_image)
                    .error(R.drawable.test_image)
                    .into(ivBackgroudImage);

        if (movieDetailEntity.getPosterPath() != null)
            Picasso.get().load(ApiServiceClient.POSTER_IMAGE_BASE_URL + movieDetailEntity.getPosterPath())
                    .placeholder(R.drawable.test_image)
                    .error(R.drawable.test_image)
                    .into(ivMoviePoster);

        tvMovieName.setText(movieDetailEntity.getTitle());
//        tvTagline.setText(movieDetailEntity.getTagline());
        tvRating.setText(String.valueOf(movieDetailEntity.getVoteAverage()));
        StringBuilder generBuilder = new StringBuilder();

        int i =0;
        for (GenreEntity genre : movieDetailWithGenersAndProCompanies.getGenres()) {
            if(i<movieDetailWithGenersAndProCompanies.getGenres().size()-1){
                generBuilder.append(genre.getName());
                generBuilder.append(", ");
            }else if(i==movieDetailWithGenersAndProCompanies.getGenres().size()-1){
                generBuilder.append(genre.getName());
            }

            i++;
        }

        tvGener.setText(generBuilder.toString());

        StringBuilder durationBuilder = new StringBuilder(String.valueOf(movieDetailEntity.getRuntime()));
        durationBuilder.append(" Min");

        StringBuilder revenueBuilder = new StringBuilder("$ ");
        revenueBuilder.append(movieDetailEntity.getRevenue());

        StringBuilder budgetBuilder = new StringBuilder("$ ");
        revenueBuilder.append(movieDetailEntity.getBudget());



        tvOverview.setText(movieDetailEntity.getOverview());
        tvReleaseYear.setText(movieDetailEntity.getReleaseDate());
        tvDuration.setText(String.valueOf(durationBuilder));
        tvRevenue.setText(String.valueOf(revenueBuilder));
        tvBudget.setText(String.valueOf(budgetBuilder));

        ibtnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoKey;
                if(!movieDetailWithGenersAndProCompanies.getMovieTrailerEntities().isEmpty()){
                    videoKey = movieDetailWithGenersAndProCompanies.getMovieTrailerEntities().get(0).getKey();

                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+videoKey)));
                }else{

                    ShowNoContentPopUp.showMessage(
                            MovieDetailActivity.this,
                            "Sorry, There is no video available related to movie "+
                                    movieDetailWithGenersAndProCompanies.movieDetailEntity.getOriginalTitle());
                }

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
