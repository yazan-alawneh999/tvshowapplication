package com.alawnehj.mytvapplication.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.alawnehj.mytvapplication.models.TvShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface TvShowDao {
    @Query("Select * from tvShows")
    Flowable<List<TvShow>> getWatchlist();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable addToWatchlist(TvShow tvshow);

    @Delete
    Completable removeFromWatchlist(TvShow tvShow);

    @Query("Select * from tvshows Where  id = :tvShowId ")
    Flowable<TvShow> getTVShowFromWatchlist(String tvShowId);

}
