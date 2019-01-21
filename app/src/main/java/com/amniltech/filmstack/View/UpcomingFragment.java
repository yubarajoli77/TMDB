package com.amniltech.filmstack.View;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amniltech.filmstack.Repository.MovieRepo;
import com.amniltech.filmstack.View.CustomAdapter.MovieCustomAdapter;
import com.amniltech.filmstack.R;
import com.amniltech.filmstack.RoomDatabase.Entity.MovieEntity;
import com.amniltech.filmstack.Util.DataProvider;
import com.amniltech.filmstack.ViewModel.CustomMovieViewModelFactory;
import com.amniltech.filmstack.ViewModel.MovieViewModel;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.ResourceSubscriber;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment {
    private RecyclerView rvUpcomingMovieContainer;
    private MovieViewModel movieViewModel;
    private MovieCustomAdapter movieAdapter;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public UpcomingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming, container, false);
        int CURRENT_OREINTATION = DataProvider.getCurrentOrientation(getActivity());

        rvUpcomingMovieContainer = view.findViewById(R.id.rv_upcoming_movie_container);
        movieAdapter = new MovieCustomAdapter();
        rvUpcomingMovieContainer.setAdapter(movieAdapter);

        if(CURRENT_OREINTATION == Configuration.ORIENTATION_PORTRAIT)
            rvUpcomingMovieContainer.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        else if(CURRENT_OREINTATION == Configuration.ORIENTATION_LANDSCAPE)
            rvUpcomingMovieContainer.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        else
            rvUpcomingMovieContainer.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        if(getActivity()!=null){
            movieViewModel = ViewModelProviders.of(this,
                    new CustomMovieViewModelFactory(getActivity().getApplication(), MovieRepo.UPCOMING_MOVIES))
                    .get(MovieViewModel.class);
        }


        movieAdapter.setOnItemClickListener(new MovieCustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClickListener(MovieEntity movieEntity) {
                Intent movieDetailIntent = new Intent(getContext(),MovieDetailActivity.class);
                movieDetailIntent.putExtra(MovieDetailActivity.MOVIE_ID,movieEntity.getId());
                startActivity(movieDetailIntent);
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(movieViewModel!=null){
            compositeDisposable.add(movieViewModel.getUpcomingMovies()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new ResourceSubscriber<List<MovieEntity>>() {
                        @Override
                        public void onNext(List<MovieEntity> movieEntityList) {
                            movieAdapter.setMovieList(movieEntityList);
                            movieAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(Throwable t) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        compositeDisposable.clear();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE)
            rvUpcomingMovieContainer.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));
        else if (newConfig.orientation==Configuration.ORIENTATION_PORTRAIT)
            rvUpcomingMovieContainer.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

    }
}
