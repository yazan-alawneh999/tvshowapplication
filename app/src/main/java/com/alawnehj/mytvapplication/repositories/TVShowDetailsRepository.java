package com.alawnehj.mytvapplication.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.alawnehj.mytvapplication.network.ApiClient;
import com.alawnehj.mytvapplication.network.ApiService;
import com.alawnehj.mytvapplication.responses.TvShowDetailsResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TVShowDetailsRepository {
    private ApiService apiService;

    public TVShowDetailsRepository() {
        apiService = ApiClient.getRetrofit().create(ApiService.class);
    }

    public LiveData<TvShowDetailsResponse> getTVShowDetails(String tvShowId) {
        MutableLiveData<TvShowDetailsResponse> tvShowDetails = new MutableLiveData<>();
        apiService.getTVShowDetails(tvShowId).enqueue(new Callback<TvShowDetailsResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowDetailsResponse> call, @NonNull Response<TvShowDetailsResponse> response) {
                tvShowDetails.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<TvShowDetailsResponse> call, @NonNull Throwable throwable) {
                tvShowDetails.setValue(null);
            }
        });

        return tvShowDetails;
    }
}
