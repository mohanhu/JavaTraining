package com.codelooper.training.feature.train.data;

import com.codelooper.training.feature.train.data.model.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SampleApiService {


    @GET("shows")
    Call<MovieListResponse> getShows();

}
