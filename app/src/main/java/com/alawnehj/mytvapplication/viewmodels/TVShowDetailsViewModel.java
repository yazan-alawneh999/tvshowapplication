package com.alawnehj.mytvapplication.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.alawnehj.mytvapplication.database.TvShowDatabase;
import com.alawnehj.mytvapplication.models.TvShow;
import com.alawnehj.mytvapplication.repositories.TVShowDetailsRepository;
import com.alawnehj.mytvapplication.responses.TvShowDetailsResponse;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class TVShowDetailsViewModel extends AndroidViewModel {
    private TVShowDetailsRepository tvShowDetailsRepository;
    private TvShowDatabase tvShowDatabase;

    public TVShowDetailsViewModel(@NonNull Application application) {
        super(application);
        this.tvShowDetailsRepository = new TVShowDetailsRepository();
        tvShowDatabase = TvShowDatabase.getTvshowDatabase(application);
    }

    public LiveData<TvShowDetailsResponse> getTVShowDetails(String tvShowId) {
        return this.tvShowDetailsRepository.getTVShowDetails(tvShowId);

    }

    public Completable addToWatchlist(TvShow tvShow) {
        return tvShowDatabase.tvShowDao().addToWatchlist(tvShow);
    }

    public Flowable<TvShow> getTVShowFromWatchlist(String watchlistId) {
        return tvShowDatabase.tvShowDao().getTVShowFromWatchlist(watchlistId);
    }

    public Completable removeTVShowFromWatchlist(TvShow tvShow){
        return tvShowDatabase.tvShowDao().removeFromWatchlist(tvShow);
    }
}
