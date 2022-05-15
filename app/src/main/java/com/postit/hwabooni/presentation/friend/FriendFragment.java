package com.postit.hwabooni.presentation.friend;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.FragmentFriendBinding;
import com.postit.hwabooni.databinding.FragmentNewsBinding;
import com.postit.hwabooni.model.FriendData;
import com.postit.hwabooni.model.News;
import com.postit.hwabooni.presentation.news.NewsRecyclerViewAdapter;

import java.util.ArrayList;

public class FriendFragment extends Fragment {

    private static final String TAG = "FriendFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentFriendBinding binding;
    //FriendRecyclerViewAdapter adapter;
    AdapterPractice adapter;

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
                Log.d(TAG,"더미데이터! :"+data.get(1).getName());
                adapter=new AdapterPractice(data,getContext());
                binding.friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                binding.friendRecyclerView.setAdapter(adapter);
            }

        });



        /*adapter = new FriendRecyclerViewAdapter();
        binding.friendRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.friendRecyclerView.setAdapter(adapter);

        db.collection("dummyFriend").get().addOnCompleteListener((document)->{
            if(document.isSuccessful()){
                ArrayList<FriendData> data = new ArrayList<>();
                for (DocumentSnapshot doc : document.getResult().getDocuments()){
                    data.add(doc.toObject(FriendData.class));
                }
                adapter.submitList(data);

            }
        });*/
    }
}










/*public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //전화 어플에 있는 Activity 정보를 넣어 Intent 정의
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01033904771"));
                startActivity(intent);
            }
        });*/