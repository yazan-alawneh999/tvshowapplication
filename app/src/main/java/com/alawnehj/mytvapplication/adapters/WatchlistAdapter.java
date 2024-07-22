package com.alawnehj.mytvapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alawnehj.mytvapplication.R;
import com.alawnehj.mytvapplication.databinding.ItemContainerTvShowBinding;
import com.alawnehj.mytvapplication.interfaces.WatchlistListener;
import com.alawnehj.mytvapplication.models.TvShow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowViewHolder> {
    private List<TvShow> tvShows;
    private LayoutInflater layoutInflater;
    private WatchlistListener watchlistListener;


    public WatchlistAdapter(List<TvShow> tvShows, WatchlistListener watchlistListener) {
        this.tvShows = tvShows;
        this.watchlistListener = watchlistListener;
    }

    @NonNull
    @Override
    public TVShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerTvShowBinding itemContainerTvShowBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_tv_show
                , parent
                , false);

        return new TVShowViewHolder(itemContainerTvShowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowViewHolder holder, int position) {
        holder.bindTvShow(tvShows.get(position));
    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

    public class TVShowViewHolder extends RecyclerView.ViewHolder {
        ItemContainerTvShowBinding itemContainerTvShowBinding;

        public TVShowViewHolder(@NonNull ItemContainerTvShowBinding itemContainerTvShowBinding) {
            super(itemContainerTvShowBinding.getRoot());
            this.itemContainerTvShowBinding = itemContainerTvShowBinding;
        }

        public void bindTvShow(TvShow tvShow) {
            this.itemContainerTvShowBinding.setTvShow(tvShow);
            this.itemContainerTvShowBinding.executePendingBindings();
            this.itemContainerTvShowBinding.getRoot().setOnClickListener(v -> {
                watchlistListener.onTVShowClicked(tvShow);
            });
            this.itemContainerTvShowBinding.imageDelete.setOnClickListener(v -> {
                watchlistListener.deleteTVShowFromWatchlist(tvShow, getAdapterPosition());
            });
            this.itemContainerTvShowBinding.imageDelete.setVisibility(View.VISIBLE);
        }


    }
}
