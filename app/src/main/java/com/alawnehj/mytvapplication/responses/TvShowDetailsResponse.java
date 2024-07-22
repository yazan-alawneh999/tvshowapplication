package com.alawnehj.mytvapplication.responses;

import com.alawnehj.mytvapplication.models.TvShowDetails;
import com.google.gson.annotations.SerializedName;

public class TvShowDetailsResponse {
    @SerializedName("tvShow")
    private TvShowDetails tvShowDetails;
    public TvShowDetails getTvShowDetails() {
        return this.tvShowDetails;
    }
}
