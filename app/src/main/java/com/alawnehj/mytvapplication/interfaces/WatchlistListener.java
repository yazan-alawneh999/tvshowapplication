package com.alawnehj.mytvapplication.interfaces;

import com.alawnehj.mytvapplication.models.TvShow;

public interface WatchlistListener {
    
    void onTVShowClicked(TvShow tvShow);

    void deleteTVShowFromWatchlist(TvShow tvShow,int position);
}
