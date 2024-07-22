package com.alawnehj.mytvapplication.network;

import androidx.lifecycle.LiveData;

import com.alawnehj.mytvapplication.responses.TvShowDetailsResponse;
import com.alawnehj.mytvapplication.responses.TvShowsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("most-popular")
    Call<TvShowsResponse> getMostPopularTVShows(@Query("page") int page);

    @GET("show-details")
    Call<TvShowDetailsResponse> getTVShowDetails(@Query("q")String tvShowId);

    @GET("search")
    Call<TvShowsResponse> searchTVShow(@Query("q")String query, @Query("page")int page);

}
