package com.alawnehj.mytvapplication.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.alawnehj.mytvapplication.R;
import com.alawnehj.mytvapplication.databinding.ItemContainerImageSliderBinding;



public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder> {
    private String[] imageUrls;
    private LayoutInflater layoutInflater;

    public ImageSliderAdapter(String[] imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerImageSliderBinding imageSliderBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_image_slider,
                parent,
                false);

        return new ImageViewHolder(imageSliderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
    holder.bindingImage(imageUrls[position]);
    }

    @Override
    public int getItemCount() {
        return imageUrls.length;
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ItemContainerImageSliderBinding imageSliderBinding;

        public ImageViewHolder(@NonNull ItemContainerImageSliderBinding itemView) {
            super(itemView.getRoot());
            this.imageSliderBinding = itemView;

        }

        public void bindingImage(String imageUrl) {
            this.imageSliderBinding.setImageUrl(imageUrl);
            this.imageSliderBinding.executePendingBindings();
        }
    }
}
