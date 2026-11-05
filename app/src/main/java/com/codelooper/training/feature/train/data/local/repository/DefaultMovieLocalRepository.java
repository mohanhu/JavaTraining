package com.codelooper.training.feature.train.data.local.repository;

import com.codelooper.training.core.data.local.AppDatabase;
import com.codelooper.training.feature.train.domain.local.repository.MovieLocalRepository;

public class DefaultMovieLocalRepository implements MovieLocalRepository {

    private final AppDatabase database;

    DefaultMovieLocalRepository(
            AppDatabase database
    ) {
        this.database = database;
    }

}
