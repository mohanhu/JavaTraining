package com.codelooper.training.feature.train.data.repository;

import com.codelooper.training.core.network.BaseRemoteDataSource;
import com.codelooper.training.core.network.NetworkResult;
import com.codelooper.training.feature.train.data.SampleApiService;
import com.codelooper.training.feature.train.data.model.MovieListResponse;
import com.codelooper.training.feature.train.domain.repository.MainRemoteDataSource;

import javax.inject.Inject;

public class DefaultMainRemoteDataSource extends BaseRemoteDataSource implements MainRemoteDataSource {

    SampleApiService sampleApiService;

    @Inject
    public DefaultMainRemoteDataSource(SampleApiService sampleApiService) {
        this.sampleApiService = sampleApiService;
    }

    @Override
    public NetworkResult<MovieListResponse> getShows() {
        return safeApiCall(sampleApiService.getShows());
    }
}
