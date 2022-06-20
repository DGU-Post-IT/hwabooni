package com.postit.hwabooni.presentation.friend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.FragmentFriendBinding;
import com.postit.hwabooni.model.FriendData;
import com.postit.hwabooni.presentation.emotionrecord.EmotionRecordFragment;
import com.postit.hwabooni.presentation.friendplant.FriendPlantFragment;

import java.util.ArrayList;

public class FriendFragment extends Fragment {

    private static final String TAG = "FriendFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FragmentFriendBinding binding;
    FriendListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db.collection("dummyFriend").get().addOnCompleteListener((document)->{
            if(document.isSuccessful()){
                ArrayList<FriendData> data = new ArrayList<>();
                for (DocumentSnapshot doc : document.getResult().getDocuments()) {
                    data.add(doc.toObject(FriendData.class));

                }

                adapter=new FriendListAdapter(data,getContext());
                binding.friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                adapter.setListener((v)->{
                    new EmotionRecordFragment().show(requireActivity().getSupportFragmentManager(), "EMOTION_RECORD");
                });
                adapter.setFriendClickListener((name)->{
                    Bundle bundle = new Bundle();
                    bundle.putString(FriendPlantFragment.FRIEND_NAME_CODE,name);
                    FriendPlantFragment fragment = new FriendPlantFragment();
                    fragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).addToBackStack(null).commit();
                });
                binding.friendRecyclerView.setAdapter(adapter);
            }

        });

    }
}
