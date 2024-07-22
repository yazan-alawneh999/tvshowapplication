package com.alawnehj.mytvapplication.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alawnehj.mytvapplication.network.ApiClient;
import com.alawnehj.mytvapplication.network.ApiService;
import com.alawnehj.mytvapplication.responses.TvShowsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MostPopularTVShowsRepository {

    private ApiService apiService;

    public MostPopularTVShowsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowsResponse> getMostPopularTVShows(int page) {
        MutableLiveData<TvShowsResponse> data = new MutableLiveData<>();
        apiService.getMostPopularTVShows(page).enqueue(new Callback<TvShowsResponse>() {
            @Override
            public void onResponse(Call<TvShowsResponse> call, Response<TvShowsResponse> response) {
                data.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TvShowsResponse> call, Throwable throwable) {
                data.setValue(null);
            }
        });

        return data;
    }
}
