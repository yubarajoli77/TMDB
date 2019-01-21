package com.amniltech.filmstack.ViewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.amniltech.filmstack.Repository.MovieRepo;
import com.amniltech.filmstack.ViewModel.MovieDetailViewModel;

public class CustomMovieDetailViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private Long id;
    public CustomMovieDetailViewModelFactory(Application application, Long id){
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(application,id);
    }
}
