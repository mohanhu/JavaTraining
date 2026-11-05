package com.codelooper.training.feature.train.data.repository;

import com.codelooper.training.core.network.NetworkResult;
import com.codelooper.training.core.network.ResourceResult;
import com.codelooper.training.feature.train.data.model.MovieListResponse;
import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;
import com.codelooper.training.feature.train.domain.repository.MainRemoteDataSource;
import com.codelooper.training.feature.train.domain.repository.MainRepository;

import java.util.List;

import javax.inject.Inject;

public class DefaultMainRepository implements MainRepository {

    MainRemoteDataSource mainRemoteDataSource;

    @Inject
    public DefaultMainRepository(
            MainRemoteDataSource mainRemoteDataSource

    ) {
        this.mainRemoteDataSource = mainRemoteDataSource;
    }

    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public ResourceResult<List<MovieListResponseItem>> getShows() {
        NetworkResult<MovieListResponse> result = mainRemoteDataSource.getShows();
        if (result instanceof NetworkResult.Success<MovieListResponse> success) {
            return new ResourceResult.Success<>(success.data.toMovieListResponse());
        } else if (result instanceof NetworkResult.Error<MovieListResponse> error) {
            return new ResourceResult.Error<>(new Exception(error.message));
        }
        return new ResourceResult.Error<>(new Exception("Something went wrong"));
    }
}
