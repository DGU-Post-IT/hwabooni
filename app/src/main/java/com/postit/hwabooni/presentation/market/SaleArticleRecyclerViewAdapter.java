package com.postit.hwabooni.presentation.market;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.postit.hwabooni.databinding.ItemSaleArticleBinding;
import com.postit.hwabooni.model.SaleArticle;

public class SaleArticleRecyclerViewAdapter extends ListAdapter<SaleArticle, SaleArticleRecyclerViewAdapter.SaleArticleViewHolder> {

    protected SaleArticleRecyclerViewAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public SaleArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SaleArticleViewHolder(ItemSaleArticleBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SaleArticleViewHolder holder, int position) {
        holder.bind(getCurrentList().get(position));
    }

    class SaleArticleViewHolder extends RecyclerView.ViewHolder{

        ItemSaleArticleBinding binding;

        public SaleArticleViewHolder(@NonNull ItemSaleArticleBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(SaleArticle article){
            binding.itemNameTextView.setText(article.getPlantName());
            binding.itemPriceTextView.setText(String.valueOf(article.getPrice()));
            Glide.with(binding.getRoot())
                    .load(article.getPicture())
                    .transform(new CenterCrop(), new RoundedCorners(12))
                    .into(binding.itemImageView);
        }

    }

    static DiffUtil.ItemCallback<SaleArticle> diffCallback = new DiffUtil.ItemCallback<SaleArticle>() {
        @Override
        public boolean areItemsTheSame(@NonNull SaleArticle oldItem, @NonNull SaleArticle newItem) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(@NonNull SaleArticle oldItem, @NonNull SaleArticle newItem) {
            return false;
        }
    };
}
