package com.postit.hwabooni.presentation.friend;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.LogDescriptor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.databinding.CardviewFriendBinding;
import com.postit.hwabooni.databinding.FriendRecyclerviewHeaderBinding;
import com.postit.hwabooni.model.Emotion;
import com.postit.hwabooni.model.FriendData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendListAdapter extends RecyclerView.Adapter {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    interface FriendClickListener {
        void onClick(String name,String email);
    }

    private FriendClickListener friendClickListener;

    private static final int TYPE_HEADER = 0;

    private static final int TYPE_ITEM = 1;

    private ArrayList<FriendData> friend;

    private Context context;

    private View.OnClickListener listener;

    private View.OnClickListener mypageListener;

    private View.OnClickListener friendAddListener;

    private View.OnClickListener friendDeleteListener;

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public void setMyEmotion(String emotion){
        this.myEmotion = emotion;
    }

    private String myName = "이름없음";

    private String myEmotion = "1";

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setMypageListener(View.OnClickListener listener) {
        this.mypageListener = listener;
    }

    public void setFriendAddListener(View.OnClickListener listener) {
        this.friendAddListener = listener;
    }

    public void setFriendDeleteListener(View.OnClickListener listener) {
        this.friendDeleteListener = listener;
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
            Log.d("user이름", myName);
            binding.myName.setText(myName);
            Drawable drawable = AppCompatResources.getDrawable(binding.getRoot().getContext(),Emotion.values()[Integer.parseInt(myEmotion)].getIcon());
            binding.myEmotionImage.setImageDrawable(drawable);
            binding.helperName.setText("사회복지사분 성함");
            if (listener != null) binding.emotionRecordButton.setOnClickListener(listener);
            if (mypageListener!=null) binding.infoButton.setOnClickListener(mypageListener);
            if (friendAddListener!=null) binding.friendAddButton.setOnClickListener(friendAddListener);

        } else {
            position -= 1;
            FriendViewHolder itemViewHolder = (FriendViewHolder) holder;
            final CardviewFriendBinding binding = itemViewHolder.binding;
            String friendName = friend.get(position).getName();
            String friendEmail = friend.get(position).getEmail();
            binding.friendName.setText(friendName);

            if(friend.get(position).getEmotion()!=null){
                Log.d("현재친구", friend.get(position).getName().toString());
                Log.d("해당친구 감정", friend.get(position).getEmotion());
                Drawable drawable = AppCompatResources.getDrawable(binding.getRoot().getContext(), Emotion.values()[Integer.parseInt(friend.get(position).getEmotion())].getIcon());
                binding.friendEmotionView.setImageDrawable(drawable);
            }


            binding.plantImageView.setVisibility(View.VISIBLE);
            Glide.with(binding.getRoot())
                    .load(friend.get(position).getPlantImage())
                    .transform(new CenterCrop(), new RoundedCorners(12))
                    .into(binding.plantImageView);


            String phoneNumber = "tel:" + friend.get(position).getPhone();

            binding.getRoot().setOnClickListener((v) -> {
                Log.d("", "click됨");
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
