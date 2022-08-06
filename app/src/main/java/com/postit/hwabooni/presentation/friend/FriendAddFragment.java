package com.postit.hwabooni.presentation.friend;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.postit.hwabooni.databinding.FragmentFriendAddBinding;

public class FriendAddFragment extends DialogFragment {

    FragmentFriendAddBinding binding;
    final String TAG = "FriendAddFragment";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendAddBinding.inflate(inflater, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnAddFriend.setOnClickListener(v -> {
            String friendEmail = binding.tvFriendEmail.getText().toString();
            if (friendEmail.length() < 1) return;
            db.collection("User").document(friendEmail).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        updateFriendArray(friendEmail);
                    } else {
                        showToast("존재하지 않는 이메일입니다. 이메일 주소를 확인해주세요");
                    }
                }
            });
        });
    }

    void updateFriendArray(String friendEmail) {
        if (auth.getCurrentUser() == null||auth.getCurrentUser().getEmail() == null) return;
        DocumentReference documentRef = db.collection("User").document(auth.getCurrentUser().getEmail());
        documentRef.update("follower", FieldValue.arrayUnion(friendEmail))
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "친구추가완료");
                    showToast("친구추가완료");
                })
                .addOnFailureListener(e -> {
                    showToast("친구추가실패");
                    Log.w(TAG, "친구추가실패", e);
                });
    }

    void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }


}
