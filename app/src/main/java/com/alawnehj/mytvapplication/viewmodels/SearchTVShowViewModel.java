package com.alawnehj.mytvapplication.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.alawnehj.mytvapplication.repositories.SearchTVShowRepository;
import com.alawnehj.mytvapplication.responses.TvShowsResponse;

public class SearchTVShowViewModel extends ViewModel    {
    private SearchTVShowRepository searchTVShowRepository;
    public SearchTVShowViewModel(){
        searchTVShowRepository = new SearchTVShowRepository();
    }

    public LiveData<TvShowsResponse> searchTVShows(String query,int page){
        return searchTVShowRepository.searchTVShow(query,page);
    }
}
