package com.codelooper.training.main;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;
import com.codelooper.training.feature.train.domain.repository.MainRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final MainRepository mainRepository;


    //IO
    private final ExecutorService ioExecutor =
            Executors.newFixedThreadPool(4);

    //Default
    private final ExecutorService defaultExecutor =
            Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors()
            );

    //Main
    private final Handler mainHandler =
            new Handler(Looper.getMainLooper());

    // Live state handle
    private final MutableLiveData<List<MovieListResponseItem>> _movieList = new MutableLiveData<>();
    public LiveData<List<MovieListResponseItem>> movieList = _movieList;


    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);

    public LiveData<Boolean> isLoading =_isLoading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();

    public LiveData<String> error = _error;

    @Inject
    public MainViewModel(MainRepository mainRepository) {
        this.mainRepository = mainRepository;

        loadData();

    }

    private void loadData() {

    }


    @Override
    protected void onCleared() {
        ioExecutor.shutdown();
        defaultExecutor.shutdown();
    }
}