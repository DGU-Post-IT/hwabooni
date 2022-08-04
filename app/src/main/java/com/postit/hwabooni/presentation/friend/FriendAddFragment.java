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
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.postit.hwabooni.databinding.FragmentFriendAddBinding;
import com.postit.hwabooni.databinding.FragmentPlantAddBinding;

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
        binding = FragmentFriendAddBinding.inflate(inflater,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StorageReference storageRef = storage.getReference();

        binding.btnAddFriend.setOnClickListener(v -> {
            DocumentReference documentRef = db.collection("User").document(auth.getCurrentUser().getEmail());
            //Log.d(TAG, auth.getCurrentUser().getEmail());
            documentRef.update("follower", FieldValue.arrayUnion(binding.tvFriendEmail.getText().toString()))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "친구추가완료");
                            Toast.makeText(getContext(), "친구추가완료", Toast.LENGTH_LONG).show();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().remove(FriendAddFragment.this).commit();
                            fragmentManager.popBackStack();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "친구추가실패", Toast.LENGTH_LONG).show();
                            Log.w(TAG, "친구추가실패", e);
                        }
                    });;
        });
    }


}
