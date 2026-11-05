package com.codelooper.training.feature.train.domain.repository;

import com.codelooper.training.core.network.NetworkResult;
import com.codelooper.training.feature.train.data.model.MovieListResponse;

import retrofit2.Call;

public interface MainRemoteDataSource {
    NetworkResult<MovieListResponse> getShows();
}
