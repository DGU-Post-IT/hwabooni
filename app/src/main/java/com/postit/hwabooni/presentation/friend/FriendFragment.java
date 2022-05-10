package com.postit.hwabooni.presentation.friend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.FragmentFriendBinding;

public class FriendFragment extends Fragment {

    FragmentFriendBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //전화 어플에 있는 Activity 정보를 넣어 Intent 정의
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01033904771"));
                startActivity(intent);
            }
        });
    }










    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
