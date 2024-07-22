package com.alawnehj.mytvapplication.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.alawnehj.mytvapplication.R;
import com.alawnehj.mytvapplication.adapters.EpisodesAdapter;
import com.alawnehj.mytvapplication.adapters.ImageSliderAdapter;
import com.alawnehj.mytvapplication.databinding.ActivityMainBinding;
import com.alawnehj.mytvapplication.databinding.ActivityTvshowDetailsBinding;
import com.alawnehj.mytvapplication.databinding.LayoutEpisodesBottomSheetBinding;
import com.alawnehj.mytvapplication.models.TvShow;
import com.alawnehj.mytvapplication.models.TvShowDetails;
import com.alawnehj.mytvapplication.utilities.TempDataHolder;
import com.alawnehj.mytvapplication.viewmodels.TVShowDetailsViewModel;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {
    private ActivityTvshowDetailsBinding activityTvshowDetailsBinding;
    private TVShowDetailsViewModel tvShowDetailsViewModel;
    private BottomSheetDialog episodesBottomSheetDialog;
    private LayoutEpisodesBottomSheetBinding layoutEpisodesBottomSheetBinding;
    private TvShow tvShow;
    private Boolean isTVShowAvailableInWatchlist = false;

    private static final String TAG = "yazan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityTvshowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        doInitialization();


    }

    private void doInitialization() {
        Log.d(TAG, "doInitialization: TVShowDetails");
        tvShowDetailsViewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        tvShow = (TvShow) getIntent().getSerializableExtra("tvShow");
        setBackListener();
        checkTVShowInWatchlist();
        getTVShowDetails();


    }




    private void checkTVShowInWatchlist() {
        CompositeDisposable checkTvShowInWatchlistDisposable = new CompositeDisposable();
        checkTvShowInWatchlistDisposable.add(
                tvShowDetailsViewModel.getTVShowFromWatchlist(String.valueOf(tvShow.getId()))
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(tvShow1 -> {
                            isTVShowAvailableInWatchlist = true;
                            activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_check);
                            checkTvShowInWatchlistDisposable.dispose();
                        })
        );
    }


    private void setBackListener() {
        activityTvshowDetailsBinding.imageBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });
    }

    public void getTVShowDetails() {
        activityTvshowDetailsBinding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        Log.d(TAG, "getTVShowDetails: " + tvShowId);

        tvShowDetailsViewModel.getTVShowDetails(tvShowId).observe(this, tvShowDetailsResponse -> {
            activityTvshowDetailsBinding.setIsLoading(false);

            if (tvShowDetailsResponse.getTvShowDetails() != null) {


                if (tvShowDetailsResponse.getTvShowDetails().getPictures() != null) {
                    loadImageSlider(tvShowDetailsResponse.getTvShowDetails().getPictures());
                }

                activityTvshowDetailsBinding.setTvShowImageUrl(tvShowDetailsResponse.getTvShowDetails().getImagePath());
                activityTvshowDetailsBinding.imageTVShow.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.setDescription(
                        String.valueOf(
                                HtmlCompat.fromHtml(
                                        tvShowDetailsResponse.getTvShowDetails().getDescription(),
                                        HtmlCompat.FROM_HTML_MODE_LEGACY
                                )
                        )
                );
                activityTvshowDetailsBinding.textDescription.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.textReadMore.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.textReadMore.setOnClickListener(v -> {
                    if (activityTvshowDetailsBinding.textReadMore.getText().toString().equals("Read More")) {
                        Log.d(TAG, "getTVShowDetails: read more statement");
                        activityTvshowDetailsBinding.textDescription.setMaxLines(Integer.MAX_VALUE);
                        activityTvshowDetailsBinding.textDescription.setEllipsize(null);
                        activityTvshowDetailsBinding.textReadMore.setText(R.string.read_less);
                    } else {
                        activityTvshowDetailsBinding.textDescription.setMaxLines(4);
                        activityTvshowDetailsBinding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                        activityTvshowDetailsBinding.textReadMore.setText(R.string.read_more);
                    }
                });


                activityTvshowDetailsBinding.setRating(
                        String.format(
                                Locale.getDefault(),
                                "%.2f",
                                Double.parseDouble(tvShowDetailsResponse.getTvShowDetails().getRating())
                        ));
                if (tvShowDetailsResponse.getTvShowDetails().getGenres() != null) {
                    activityTvshowDetailsBinding.setGenre(tvShowDetailsResponse.getTvShowDetails().getGenres()[0]);
                } else {
                    activityTvshowDetailsBinding.setGenre("N/A");
                }
                activityTvshowDetailsBinding.setRuntime(tvShowDetailsResponse.getTvShowDetails().getRuntime() + " Min");
                activityTvshowDetailsBinding.viewDivider1.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.layoutMisc.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.viewDivider2.setVisibility(View.VISIBLE);

                activityTvshowDetailsBinding.buttonWebsite.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowDetails().getUrl()));
                    startActivity(intent);
                });

                activityTvshowDetailsBinding.buttonWebsite.setVisibility(View.VISIBLE);
                activityTvshowDetailsBinding.buttonEpisodes.setVisibility(View.VISIBLE);

                activityTvshowDetailsBinding.buttonEpisodes.setOnClickListener(v -> {
                    episodesBottomSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                    layoutEpisodesBottomSheetBinding = DataBindingUtil.inflate(
                            LayoutInflater.from(TVShowDetailsActivity.this)
                            , R.layout.layout_episodes_bottom_sheet
                            , findViewById(R.id.episodeContainer)
                            , false);

                    episodesBottomSheetDialog.setContentView(layoutEpisodesBottomSheetBinding.getRoot());
                    layoutEpisodesBottomSheetBinding.episodeRecyclerView.setAdapter(
                            new EpisodesAdapter(tvShowDetailsResponse.getTvShowDetails().getEpisodes())
                    );
                    layoutEpisodesBottomSheetBinding.textTitle.setText(
                            String.format("Episode | %s", tvShow.getName())
                    );
                    layoutEpisodesBottomSheetBinding.imageClose.setOnClickListener(v1 -> {
                        episodesBottomSheetDialog.dismiss();
                    });

                    FrameLayout frameLayout = episodesBottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    if (frameLayout != null) {
                        BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                        bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }
//
                    episodesBottomSheetDialog.show();

                });

                activityTvshowDetailsBinding.imageWatchlist.setOnClickListener(v -> {
                    CompositeDisposable compositeDisposable = new CompositeDisposable();

                    if (!isTVShowAvailableInWatchlist) {
                        compositeDisposable.add(
                                tvShowDetailsViewModel.addToWatchlist(tvShow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.ic_check);
                                            Toast.makeText(getApplicationContext(), "Added to Watchlist ", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();

                                        }));

                    } else {
                        compositeDisposable.add(
                                tvShowDetailsViewModel.removeTVShowFromWatchlist(tvShow)
                                        .subscribeOn(Schedulers.computation())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            isTVShowAvailableInWatchlist = false;
                                            TempDataHolder.IS_WATCHLIST_UPDATED = true;
                                            activityTvshowDetailsBinding.imageWatchlist.setImageResource(R.drawable.eye);
                                            Toast.makeText(getApplicationContext(), "Removed From Watchlist ", Toast.LENGTH_SHORT).show();
                                            compositeDisposable.dispose();
                                        }));
                    }
                });
                activityTvshowDetailsBinding.imageWatchlist.setVisibility(View.VISIBLE);
                loadBasicTvShowDetails();
            }


        });

    }

    private void loadImageSlider(String[] pictures) {
        activityTvshowDetailsBinding.sliderViewPager.setOffscreenPageLimit(1);
        activityTvshowDetailsBinding.sliderViewPager.setAdapter(new ImageSliderAdapter(pictures));
        activityTvshowDetailsBinding.sliderViewPager.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.fadingEdgeView.setVisibility(View.VISIBLE);
        setUpSliderIndicators(pictures.length);
        activityTvshowDetailsBinding.sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });
    }

    public void setUpSliderIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams layoutParams = new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext()
                    , R.drawable.background_slider_indicator_inactive));
            indicators[i].setLayoutParams(layoutParams);
            activityTvshowDetailsBinding.layoutSliderIndicators.addView(indicators[i]);
            setCurrentSliderIndicator(0);
        }
        activityTvshowDetailsBinding.layoutSliderIndicators.setVisibility(View.VISIBLE);
    }

    private void setCurrentSliderIndicator(int position) {
        int childesCount = activityTvshowDetailsBinding.layoutSliderIndicators.getChildCount();
        for (int i = 0; i < childesCount; i++) {
            ImageView imageView = (ImageView) activityTvshowDetailsBinding.layoutSliderIndicators.getChildAt(i);
            if (position == i) {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indicator_active));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                        R.drawable.background_slider_indicator_inactive));
            }

        }


    }


    private void loadBasicTvShowDetails() {
        activityTvshowDetailsBinding.setTvShowName(tvShow.getName());
        activityTvshowDetailsBinding.setStartedDate(tvShow.getStartDate());
        activityTvshowDetailsBinding.setNetworkCountry(tvShow.getNetwork() + "(" + tvShow.getCountry() + ")");
        activityTvshowDetailsBinding.setStatus(tvShow.getStatus());

        activityTvshowDetailsBinding.tvShowName.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStarted.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textStatus.setVisibility(View.VISIBLE);
        activityTvshowDetailsBinding.textNetworkCountry.setVisibility(View.VISIBLE);

    }
}