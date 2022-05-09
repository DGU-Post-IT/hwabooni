package com.postit.hwabooni.presentation.news;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.postit.hwabooni.databinding.ItemNewsBinding;
import com.postit.hwabooni.model.News;

import java.util.List;

public class NewsRecyclerViewAdapter extends ListAdapter<News, NewsRecyclerViewAdapter.NewsViewHolder> {

    List<News> data;

    protected NewsRecyclerViewAdapter(@NonNull DiffUtil.ItemCallback<News> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.bind(data.get(position));
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        ItemNewsBinding binding;

        public NewsViewHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(News news) {

        }
    }

    DiffUtil.ItemCallback<News> diffCallback = new DiffUtil.ItemCallback<News>() {
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
