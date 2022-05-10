package com.postit.hwabooni.presentation.news;

import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.ItemNewsBinding;
import com.postit.hwabooni.model.Emotion;
import com.postit.hwabooni.model.News;

public class NewsRecyclerViewAdapter extends ListAdapter<News, NewsRecyclerViewAdapter.NewsViewHolder> {

    private static final String TAG = "NewsRecyclerViewAdapter";

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
            switch (news.getType()){
                case 1: //물주기
                    binding.getRoot().setCardBackgroundColor(AppCompatResources.getColorStateList(binding.getRoot().getContext(),R.color.blue));
                    binding.newsTextView.setText(String.format("%s님이 '%s'에 물을 주었습니다.",news.getName(),news.getData()));
                    break;
                case 2: //예쁜말하기
                    binding.getRoot().setCardBackgroundColor(AppCompatResources.getColorStateList(binding.getRoot().getContext(),R.color.pink));
                    binding.newsTextView.setText(String.format("%s님이 '%s'에 예쁜 말을 해 주었습니다.",news.getName(),news.getData()));
                    break;
                case 3: //감정기록
                    binding.getRoot().setCardBackgroundColor(AppCompatResources.getColorStateList(binding.getRoot().getContext(),R.color.orange));
                    SpannableStringBuilder string = new SpannableStringBuilder(String.format("%s님의 기분은 %s",news.getName(),Emotion.values()[Integer.parseInt(news.getData())].getKrEmotion()));
                    SpannableStringBuilder emotion = new SpannableStringBuilder("  ");
                    Drawable drawable = AppCompatResources.getDrawable(binding.getRoot().getContext(),Emotion.values()[Integer.parseInt(news.getData())].getIcon());
                    drawable.setBounds(0,0,binding.newsTextView.getLineHeight(),binding.newsTextView.getLineHeight());
                    emotion.setSpan(new ImageSpan(drawable),1,2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    string.append(emotion);
                    string.append("입니다.");
                    binding.newsTextView.setText(string);
                    break;
            }
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
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull News oldItem, @NonNull News newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

}
