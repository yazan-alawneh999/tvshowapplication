package com.alawnehj.mytvapplication.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alawnehj.mytvapplication.network.ApiClient;
import com.alawnehj.mytvapplication.network.ApiService;
import com.alawnehj.mytvapplication.responses.TvShowsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchTVShowRepository {

    private ApiService apiService;

    public SearchTVShowRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowsResponse> searchTVShow(String query, int page) {
        MutableLiveData<TvShowsResponse> searchTVShowResponseMutableLiveData = new MutableLiveData<>();
        apiService.searchTVShow(query, page).enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowsResponse> call, @NonNull Response<TvShowsResponse> response) {
                searchTVShowResponseMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowsResponse> call, @NonNull Throwable throwable) {
                searchTVShowResponseMutableLiveData.setValue(null);
            }
        });
        return searchTVShowResponseMutableLiveData;
    }
}
