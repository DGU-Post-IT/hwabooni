package com.postit.hwabooni.presentation.friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.postit.hwabooni.databinding.CardviewFriendBinding;
import com.postit.hwabooni.databinding.FriendRecyclerviewHeaderBinding;
import com.postit.hwabooni.model.Emotion;
import com.postit.hwabooni.model.FriendData;

import java.util.ArrayList;

public class AdapterPractice extends RecyclerView.Adapter {

    private static final int TYPE_HEADER=0;

    private static final int TYPE_ITEM=1;

    private ArrayList<FriendData> friend;

    private Context context;


    private View.OnClickListener listener;

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public AdapterPractice(ArrayList<FriendData> friend,Context context){
        this.friend=friend;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;

        if(viewType==TYPE_HEADER){
            FriendRecyclerviewHeaderBinding headerBinding=FriendRecyclerviewHeaderBinding.inflate(LayoutInflater.from(context),parent,false);
            holder= new HeaderViewHolder(headerBinding);
        }
        else{
            CardviewFriendBinding itemBinding=CardviewFriendBinding.inflate(LayoutInflater.from(context),parent,false);
            holder= new FriendViewHolder(itemBinding);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof HeaderViewHolder){
            HeaderViewHolder headerViewHolder=(HeaderViewHolder) holder;
            final FriendRecyclerviewHeaderBinding binding=headerViewHolder.binding;
            binding.helperName.setText("사회복지사분 성함");
            if(listener!=null) binding.emotionRecordButton.setOnClickListener(listener);

        }
        else{
            position-=1;
            FriendViewHolder itemViewHolder=(FriendViewHolder) holder;
            final CardviewFriendBinding binding=itemViewHolder.binding;
            binding.friendName.setText(friend.get(position).getName());

            Drawable drawable = AppCompatResources.getDrawable(binding.getRoot().getContext(), Emotion.values()[Integer.parseInt(friend.get(position).getEmotion())].getIcon());
            binding.friendEmotionView.setImageDrawable(drawable);

            binding.plantImageView.setVisibility(View.VISIBLE);
            Glide.with(binding.getRoot())
                    .load(friend.get(position).getPlantImage())
                    .transform(new CenterCrop(), new RoundedCorners(12))
                    .into(binding.plantImageView);



            String phoneNumber="tel:"+friend.get(position).getPhone();


            binding.friendCallButton.setOnClickListener((v)->{
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                binding.getRoot().getContext().startActivity(intent);
            });

        }



    }
    class HeaderViewHolder extends RecyclerView.ViewHolder{

        FriendRecyclerviewHeaderBinding binding;

        HeaderViewHolder(@NonNull FriendRecyclerviewHeaderBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        CardviewFriendBinding binding;

        FriendViewHolder(@NonNull CardviewFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /*void bind(FriendData friend) {
            binding.friendName.setText(friend.getName());

            Drawable drawable = AppCompatResources.getDrawable(binding.getRoot().getContext(), Emotion.values()[Integer.parseInt(friend.getEmotion())].getIcon());
            binding.friendEmotionView.setImageDrawable(drawable);

            binding.plantImageView.setVisibility(View.VISIBLE);
            Glide.with(binding.getRoot())
                    .load(friend.getPlantImage())
                    .transform(new CenterCrop(), new RoundedCorners(12))
                    .into(binding.plantImageView);



            String phoneNumber="tel:"+friend.getPhone();


            binding.friendCallButton.setOnClickListener((v)->{
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                binding.getRoot().getContext().startActivity(intent);
            });


        }*/
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }



    @Override
    public int getItemCount() {
        return friend.size()+1;
    }

}
