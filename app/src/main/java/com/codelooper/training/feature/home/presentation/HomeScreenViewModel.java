package com.codelooper.training.feature.home.presentation;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.codelooper.training.core.network.ResourceResult;
import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;
import com.codelooper.training.feature.train.domain.repository.MainRepository;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class HomeScreenViewModel extends ViewModel {

    private final MainRepository mainRepository;

    // IO Executor
    private final ExecutorService ioExecutor;

    // Default Executor
    private final ExecutorService defaultExecutor;

    // Live state handle
    private final MutableLiveData<List<MovieListResponseItem>> _movieList = new MutableLiveData<>();
    public LiveData<List<MovieListResponseItem>> movieList = _movieList;

    private final MutableLiveData<Boolean> _isLoading = new MutableLiveData<>(false);
    public LiveData<Boolean> isLoading = _isLoading;

    private final MutableLiveData<String> _error = new MutableLiveData<>();
    public LiveData<String> error = _error;

    private final MutableLiveData<String> _search = new MutableLiveData<>();
    public LiveData<String> search = _search;

    @Inject
    public HomeScreenViewModel(MainRepository mainRepository) {
        this(
            mainRepository,
            Executors.newFixedThreadPool(4),
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
        );
    }

    /**
     * Testing constructor allowing injection of custom ExecutorServices (e.g. direct synchronous executor).
     */
    public HomeScreenViewModel(
            MainRepository mainRepository,
            ExecutorService ioExecutor,
            ExecutorService defaultExecutor
    ) {
        this.mainRepository = mainRepository;
        this.ioExecutor = ioExecutor;
        this.defaultExecutor = defaultExecutor;
    }

    public void search(String search) {
        _search.postValue(search);
    }

    void loadData() {

        System.out.println("mainRepository getMessage() calling ");

        ioExecutor.execute(() -> {
            _isLoading.postValue(true);
            _error.postValue(null);
            ResourceResult<List<MovieListResponseItem>> result = mainRepository.getShows();
            if (result != null) {
                if (result instanceof ResourceResult.Success<List<MovieListResponseItem>> success) {
                    System.out.println("mainRepository getMessage() response " + result);
                    _movieList.postValue(success.getData());
                } else if (result instanceof ResourceResult.Error<List<MovieListResponseItem>> error) {
                    _error.postValue(error.getException().getMessage());
                }
                _isLoading.postValue(false);
            }
        });
    }

    public void toggleSelection(int itemId) {
        List<MovieListResponseItem> currentList = _movieList.getValue();

        if (currentList == null) return;

        List<MovieListResponseItem> updatedList = currentList.stream()
                .<MovieListResponseItem>map(item -> {
                    MovieListResponseItem copy = item.copy();
                    copy.toggle = true;
                    if (item.id == itemId) {
                        copy.isSelected = !Boolean.TRUE.equals(item.isSelected);
                    }
                    return copy;
                })
                .collect(Collectors.toList());

        _movieList.setValue(updatedList);
    }

    public void enableToggle(int itemId, boolean enable) {
        List<MovieListResponseItem> currentList = _movieList.getValue();

        if (currentList == null) return;

        List<MovieListResponseItem> updatedList = currentList.stream()
                .<MovieListResponseItem>map(item -> {
                    MovieListResponseItem copy = item.copy();
                    copy.toggle = enable;
                    if (item.id == itemId && enable) {
                        copy.isSelected = !copy.isSelected;
                    }
                    else {
                        copy.isSelected = false;
                    }
                    return copy;
                })
                .collect(Collectors.toList());

        _movieList.setValue(updatedList);
    }

    public void deleteItem() {
        List<MovieListResponseItem> currentList = _movieList.getValue();
        if (currentList == null) return;
        List<MovieListResponseItem> updatedList = currentList.stream()
                .filter(item -> !item.isSelected)
                .collect(Collectors.toList());
        _movieList.setValue(updatedList);
        enableToggle(0, false);
    }

    @Override
    protected void onCleared() {
        ioExecutor.shutdown();
        defaultExecutor.shutdown();
    }
}