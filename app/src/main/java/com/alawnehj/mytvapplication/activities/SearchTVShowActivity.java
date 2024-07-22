package com.alawnehj.mytvapplication.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;

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
import com.alawnehj.mytvapplication.databinding.ActivitySearchTvshowBinding;
import com.alawnehj.mytvapplication.helpers.AppHelper;
import com.alawnehj.mytvapplication.interfaces.TVClickListener;
import com.alawnehj.mytvapplication.models.TvShow;
import com.alawnehj.mytvapplication.viewmodels.SearchTVShowViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchTVShowActivity extends AppCompatActivity implements TVClickListener {
    private ActivitySearchTvshowBinding activitySearchTvshowBinding;
    private SearchTVShowViewModel searchTVShowViewModel;
    private List<TvShow> searchTVShowlist = new ArrayList<>();
    private TvShowAdapter tvShowAdapter;
    private int currentPage = 1;
    private int totalAvailablePages = 1;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activitySearchTvshowBinding = DataBindingUtil.setContentView(this, R.layout.activity_search_tvshow);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        doInitialization();
    }

    private void doInitialization() {
        searchTVShowViewModel = new ViewModelProvider(this).get(SearchTVShowViewModel.class);
        activitySearchTvshowBinding.imageBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
        activitySearchTvshowBinding.searchTVShowRecyclerView.setHasFixedSize(true);
        tvShowAdapter = new TvShowAdapter(searchTVShowlist, this);
        activitySearchTvshowBinding.searchTVShowRecyclerView.setAdapter(tvShowAdapter);
        activitySearchTvshowBinding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }

            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().trim().isEmpty()) {
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(() -> {
                                currentPage = 1;
                                totalAvailablePages = 1;
                                searchTVShow(s.toString());

                            });
                        }
                    }, 800);

                } else {
                    searchTVShowlist.clear();
                    tvShowAdapter.notifyDataSetChanged();

                }

            }
        });
        activitySearchTvshowBinding.searchTVShowRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!activitySearchTvshowBinding.searchTVShowRecyclerView.canScrollVertically(1)) {
                    if (!activitySearchTvshowBinding.inputSearch.getText().toString().trim().isEmpty()) {
                        if (currentPage < totalAvailablePages) {
                            currentPage += 1;
                            searchTVShow(activitySearchTvshowBinding.inputSearch.getText().toString());
                        }
                    }
                }

            }
        });
        activitySearchTvshowBinding.inputSearch.requestFocus();

    }

    private void searchTVShow(String query) {
        toggleLoading();
        searchTVShowViewModel.searchTVShows(query, currentPage)
                .observe(this, tvShowsResponse -> {
                    toggleLoading();
                    if (tvShowsResponse != null) {
                        totalAvailablePages = tvShowsResponse.getTotal_pages();
                        if (tvShowsResponse.getTvShows() != null) {
                            int oldCount = searchTVShowlist.size();
                            searchTVShowlist.addAll(tvShowsResponse.getTvShows());
                            tvShowAdapter.notifyItemRangeInserted(oldCount, searchTVShowlist.size());

                        }
                    }
                });


    }

    private void toggleLoading() {
        if (currentPage == 1) {
            if (activitySearchTvshowBinding.getIsLoading() != null && activitySearchTvshowBinding.getIsLoading()) {
                activitySearchTvshowBinding.setIsLoading(false);
            } else {
                activitySearchTvshowBinding.setIsLoading(true);
            }
        } else {
            if (activitySearchTvshowBinding.getIsLoadingMore() != null && activitySearchTvshowBinding.getIsLoadingMore()) {
                activitySearchTvshowBinding.setIsLoadingMore(false);
            } else {
                activitySearchTvshowBinding.setIsLoadingMore(true);
            }
        }


    }

    @Override
    public void OnTVClickListener(TvShow tvShow) {
        AppHelper.intentWithExtras(this, TVShowDetailsActivity.class, tvShow);

    }
}