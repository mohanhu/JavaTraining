package com.codelooper.training.feature.train.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieEntity movie);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MovieEntity> movies);

    @Query("SELECT * FROM movies ORDER BY name ASC")
    LiveData<List<MovieEntity>> getAllMovies();

    @Query("SELECT * FROM movies WHERE name LIKE '%' || :query || '%' ORDER BY name ASC")
    LiveData<List<MovieEntity>> searchMovies(String query);

    @Query("DELETE FROM movies")
    void deleteAll();
}