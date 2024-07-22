package com.alawnehj.mytvapplication.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.alawnehj.mytvapplication.database.TvShowDatabase;
import com.alawnehj.mytvapplication.models.TvShow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class WatchlistViewModel extends AndroidViewModel {
    private TvShowDatabase tvShowDatabase ;
    public WatchlistViewModel(@NonNull Application application){
        super(application);
        tvShowDatabase = TvShowDatabase.getTvshowDatabase(application);
    }

    public Flowable<List<TvShow>> loadWatchlist(){
        return tvShowDatabase.tvShowDao().getWatchlist();
    }

    public Completable removeTVShowFromWatchlist(TvShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchlist(tvShow);
    }



}
