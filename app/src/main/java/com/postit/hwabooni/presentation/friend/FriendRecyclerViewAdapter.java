package com.postit.hwabooni.presentation.friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.browse.MediaBrowser;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.CardviewFriendBinding;
import com.postit.hwabooni.databinding.FriendRecyclerviewHeaderBinding;
import com.postit.hwabooni.databinding.ItemNewsBinding;
import com.postit.hwabooni.model.Emotion;
import com.postit.hwabooni.model.FriendData;
import com.postit.hwabooni.model.News;

import java.util.ArrayList;

public class FriendRecyclerViewAdapter extends ListAdapter<FriendData, FriendRecyclerViewAdapter.FriendViewHolder> {

    private static final String TAG = "FriendRecyclerViewAdapter";
    private Context context;


    private final int TYPE_HEADER=0;
    private final int TYPE_ITEM=1;

    protected FriendRecyclerViewAdapter(ArrayList<FriendData> data, Context context) {
        super(diffCallback);
    }



    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new FriendViewHolder(CardviewFriendBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        /*context= parent.getContext();
        RecyclerView.ViewHolder holder;
        View view;


        if(viewType==TYPE_HEADER){
            FriendRecyclerviewHeaderBinding headerBinding=FriendRecyclerviewHeaderBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
            holder=new HeaderViewHolder(headerBinding);
            return holder;
        }
        else{
            holder=new FriendViewHolder(CardviewFriendBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
            return holder;
        }*/

        //new FriendViewHolder(CardviewFriendBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    /*@Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position){
        if(position==TYPE_HEADER && holder instanceof HeaderViewHolder){
            HeaderViewHolder itemViewHolder=(HeaderViewHolder) holder;
            final FriendRecyclerviewHeaderBinding binding=itemViewHolder.binding;
            binding.helperName.setText("사회복지사분 성함");
        }
        else{
            ((FriendViewHolder) holder).bind(getCurrentList().get(position));

        }
    }*/

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {

        if(position==TYPE_HEADER){



        }
        else {
            holder.bind(getCurrentList().get(position));
        }

    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        CardviewFriendBinding binding;

        public FriendViewHolder(@NonNull CardviewFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(FriendData friend) {
            binding.friendName.setText(friend.getName());

            Drawable drawable = AppCompatResources.getDrawable(binding.getRoot().getContext(), Emotion.values()[Integer.parseInt(friend.getEmotion())].getIcon());
            binding.friendEmotionView.setImageDrawable(drawable);

            binding.plantImageView.setVisibility(View.VISIBLE);
            Glide.with(binding.getRoot())
                    .load(friend.getPlantImage())
                    .transform(new CenterCrop(), new RoundedCorners(12))
                    .into(binding.plantImageView);


            /*if (friend.getPicture() != null) {
                binding.plantImageView.setVisibility(View.VISIBLE);
                Glide.with(binding.getRoot())
                        .load(friend.getPicture())
                        .transform(new CenterCrop(), new RoundedCorners(12))
                        .into(binding.plantImageView);
            } else {
                binding.plantImageView.setVisibility(View.GONE);
            }*/


            String phoneNumber="tel:"+friend.getPhone();

            Log.d(TAG,"폰번호!:"+phoneNumber);

            binding.friendCallButton.setOnClickListener((v)->{
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                binding.getRoot().getContext().startActivity(intent);
            });


        }
    }

    /*class HeaderViewHolder extends RecyclerView.ViewHolder{

        FriendRecyclerviewHeaderBinding binding;

        HeaderViewHolder(@NonNull FriendRecyclerviewHeaderBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }*/

    public int getItemViewType(int position){

        if(position==0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;

    }



    static DiffUtil.ItemCallback<FriendData> diffCallback = new DiffUtil.ItemCallback<FriendData>() {
        @Override
        public boolean areItemsTheSame(@NonNull FriendData oldItem, @NonNull FriendData newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FriendData oldItem, @NonNull FriendData newItem) {
            return oldItem.getId().equals(newItem.getId());
        }
    };

}
