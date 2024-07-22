package com.alawnehj.mytvapplication.responses;

import com.alawnehj.mytvapplication.models.TvShow;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowsResponse {

    @SerializedName("page")
    private int page;
    @SerializedName("pages")
    private int total_pages;
    @SerializedName("tv_shows")
    private List<TvShow> tvShows;

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public List<TvShow> getTvShows() {
        return tvShows;
    }
}
