package com.alawnehj.mytvapplication.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.alawnehj.mytvapplication.R;
import com.alawnehj.mytvapplication.adapters.TvShowAdapter;
import com.alawnehj.mytvapplication.databinding.ActivityMainBinding;
import com.alawnehj.mytvapplication.helpers.AppHelper;
import com.alawnehj.mytvapplication.interfaces.TVClickListener;
import com.alawnehj.mytvapplication.models.TvShow;
import com.alawnehj.mytvapplication.viewmodels.MostPopularTVShowsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity implements TVClickListener {
    private MostPopularTVShowsViewModel viewModel;
    private ActivityMainBinding activityMainBinding;
    private TvShowAdapter tvShowAdapter;
    private List<TvShow> tvShows = new ArrayList<>();
    private int currentPage = 1;
    private int availablePages = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        doInitialization();


    }

    private void doInitialization() {
        activityMainBinding.tvShowsRecyclerView.setHasFixedSize(true);
        viewModel = new ViewModelProvider(this).get(MostPopularTVShowsViewModel.class);
        tvShowAdapter = new TvShowAdapter(tvShows, this);
        activityMainBinding.tvShowsRecyclerView.setAdapter(tvShowAdapter);
        activityMainBinding.tvShowsRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activityMainBinding.tvShowsRecyclerView.canScrollVertically(1)) {
                    if (currentPage <= availablePages) {
                        currentPage += 1;
                        getMostPopularTVShows();
                    }

                }
            }
        });
        activityMainBinding.imageWatchList.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, WatchlistActivity.class));
        });
        activityMainBinding.imageSearch.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SearchTVShowActivity.class));

        });
        getMostPopularTVShows();


    }

    @SuppressLint("NotifyDataSetChanged")
    public void getMostPopularTVShows() {
        toggleLoading();
        viewModel.getMostPopularTVShows(currentPage).observe(this, tvShowsResponse -> {
            toggleLoading();
            if (tvShowsResponse != null) {
                availablePages = tvShowsResponse.getTotal_pages();
                if (tvShowsResponse.getTvShows() != null) {
                    int oldCount = this.tvShows.size();
                    this.tvShows.addAll(tvShowsResponse.getTvShows());
                    this.tvShowAdapter.notifyItemRangeInserted(oldCount, this.tvShows.size());

                }
            }
        });
    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (activityMainBinding.getIsLoading() != null && activityMainBinding.getIsLoading()) {
                activityMainBinding.setIsLoading(false);
            } else {
                activityMainBinding.setIsLoading(true);
            }
        } else {
            if (activityMainBinding.getIsLoadingMore() != null && activityMainBinding.getIsLoadingMore()) {
                activityMainBinding.setIsLoadingMore(false);
            } else {
                activityMainBinding.setIsLoadingMore(true);
            }
        }


    }

    @Override
    public void OnTVClickListener(TvShow tvShow) {
        AppHelper.intentWithExtras(this, TVShowDetailsActivity.class, tvShow);

    }


}