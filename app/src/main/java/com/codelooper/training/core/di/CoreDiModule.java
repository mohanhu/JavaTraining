package com.codelooper.training.core.di;

import android.content.Context;

import com.codelooper.training.core.data.local.AppDatabase;
import com.codelooper.training.feature.train.data.SampleApiService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class CoreDiModule {

    @Provides
    @Singleton
    public static Retrofit provideRetrofit() {
        return new Retrofit.Builder()
                .baseUrl("https://api.tvmaze.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public static SampleApiService provideApiService(
            Retrofit retrofit
    ) {
        return retrofit.create(SampleApiService.class);
    }

    @Provides
    @Singleton
    public static AppDatabase provideAppDatabase(
            Context context
    ) {
        return AppDatabase.getInstance(context);
    }
}
