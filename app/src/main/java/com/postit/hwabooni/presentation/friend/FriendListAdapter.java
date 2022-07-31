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

public class FriendListAdapter extends RecyclerView.Adapter {

    interface FriendClickListener {
        void onClick(String name,String email);
    }

    private FriendClickListener friendClickListener;

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;

    private ArrayList<FriendData> friend;

    private Context context;

    private View.OnClickListener listener;

    private View.OnClickListener logoutListener;

    public void setMyName(String myName) {
        this.myName = myName;
    }

    private String myName = "";

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setLogoutListener(View.OnClickListener listener) {
        this.logoutListener = listener;
    }

    public void setFriendClickListener(FriendClickListener listener){this.friendClickListener=listener;}

    public FriendListAdapter(Context context) {
        this.friend = new ArrayList<>();
        this.context = context;
    }

    public FriendListAdapter(ArrayList<FriendData> friend, Context context) {
        this.friend = friend;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;

        if (viewType == TYPE_HEADER) {
            FriendRecyclerviewHeaderBinding headerBinding = FriendRecyclerviewHeaderBinding.inflate(LayoutInflater.from(context), parent, false);
            holder = new HeaderViewHolder(headerBinding);
        } else {
            CardviewFriendBinding itemBinding = CardviewFriendBinding.inflate(LayoutInflater.from(context), parent, false);
            holder = new FriendViewHolder(itemBinding);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            final FriendRecyclerviewHeaderBinding binding = headerViewHolder.binding;
            binding.myName.setText(myName);
            binding.helperName.setText("사회복지사분 성함");
            if (listener != null) binding.emotionRecordButton.setOnClickListener(listener);
            if(logoutListener!=null) binding.logoutButton.setOnClickListener(logoutListener);
        } else {
            position -= 1;
            FriendViewHolder itemViewHolder = (FriendViewHolder) holder;
            final CardviewFriendBinding binding = itemViewHolder.binding;
            String friendName = friend.get(position).getName();
            String friendEmail = friend.get(position).getEmail();
            binding.friendName.setText(friendName);

            Drawable drawable = AppCompatResources.getDrawable(binding.getRoot().getContext(), Emotion.values()[Integer.parseInt(friend.get(position).getEmotion())].getIcon());
            binding.friendEmotionView.setImageDrawable(drawable);

            binding.plantImageView.setVisibility(View.VISIBLE);
            Glide.with(binding.getRoot())
                    .load(friend.get(position).getPlantImage())
                    .transform(new CenterCrop(), new RoundedCorners(12))
                    .into(binding.plantImageView);


            String phoneNumber = "tel:" + friend.get(position).getPhone();

            binding.getRoot().setOnClickListener((v) -> {
                if (friendClickListener == null) return;
                friendClickListener.onClick(friendName,friendEmail);
            });

            binding.friendCallButton.setOnClickListener((v) -> {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phoneNumber));
                binding.getRoot().getContext().startActivity(intent);
            });

        }


    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        FriendRecyclerviewHeaderBinding binding;

        HeaderViewHolder(@NonNull FriendRecyclerviewHeaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class FriendViewHolder extends RecyclerView.ViewHolder {

        CardviewFriendBinding binding;

        FriendViewHolder(@NonNull CardviewFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

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
        return friend.size() + 1;
    }

    public void setFriend(ArrayList<FriendData> friend){
        this.friend = friend;
    }
}
