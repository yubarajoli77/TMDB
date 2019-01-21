package com.amniltech.filmstack.ViewModel;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class CustomMovieViewModelFactory implements ViewModelProvider.Factory {
    private Application application;
    private String category;
    public CustomMovieViewModelFactory(Application application, String category){
        this.application = application;
        this.category = category;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieViewModel(application,category);
    }
}
