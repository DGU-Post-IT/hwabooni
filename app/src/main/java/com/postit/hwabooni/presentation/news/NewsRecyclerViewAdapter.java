package com.postit.hwabooni.presentation.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.postit.hwabooni.databinding.ItemNewsBinding;
import com.postit.hwabooni.model.News;

public class NewsRecyclerViewAdapter extends ListAdapter<News, NewsRecyclerViewAdapter.NewsViewHolder> {

    protected NewsRecyclerViewAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(getCurrentList().get(position));
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        ItemNewsBinding binding;

        public NewsViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(News news) {
            binding.newsTextView.setText(news.getName());
            if(news.getPicture() != null){
                Glide.with(binding.getRoot())
                        .load(news.getPicture())
                        .transform(new CenterCrop(), new RoundedCorners(12))
                        .into(binding.newsImageView);
            }else{
                binding.newsImageView.setVisibility(View.GONE);
            }
        }
    }

    static DiffUtil.ItemCallback<News> diffCallback = new DiffUtil.ItemCallback<News>() {
        @Override
        public boolean areItemsTheSame(@NonNull News oldItem, @NonNull News newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull News oldItem, @NonNull News newItem) {
            return false;
        }
    };

}
