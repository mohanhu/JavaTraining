package com.codelooper.training.core.data.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.codelooper.training.feature.train.data.local.MovieDao;
import com.codelooper.training.feature.train.data.local.MovieEntity;

@Database(
        entities = {MovieEntity.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract MovieDao movieDao();

    public static AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "movie_database"
                    ).fallbackToDestructiveMigration(true)
                            .build();
                }
            }
        }

        return INSTANCE;
    }
}