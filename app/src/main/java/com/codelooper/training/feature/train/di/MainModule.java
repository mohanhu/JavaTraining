package com.codelooper.training.feature.train.di;

import com.codelooper.training.feature.train.data.local.repository.DefaultMovieLocalRepository;
import com.codelooper.training.feature.train.data.repository.DefaultMainRemoteDataSource;
import com.codelooper.training.feature.train.data.repository.DefaultMainRepository;
import com.codelooper.training.feature.train.domain.local.repository.MovieLocalRepository;
import com.codelooper.training.feature.train.domain.repository.MainRemoteDataSource;
import com.codelooper.training.feature.train.domain.repository.MainRepository;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public interface MainModule {

    @Binds
    @Singleton
    MainRepository provideMainRepository(DefaultMainRepository defaultMainRepository);

    @Binds
    @Singleton
    MainRemoteDataSource provideMainRemoteDataSource(DefaultMainRemoteDataSource defaultMainRemoteDataSource);

    @Binds
    @Singleton
    MovieLocalRepository provideMovieLocalRepository(DefaultMovieLocalRepository defaultMovieLocalRepository);

}
