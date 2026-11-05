package com.codelooper.training.feature.train.domain.repository;

import com.codelooper.training.core.network.ResourceResult;
import com.codelooper.training.feature.train.domain.model.MovieListResponseItem;

import java.util.List;

public interface MainRepository {

    String getMessage();

    ResourceResult<List<MovieListResponseItem>> getShows();

}
