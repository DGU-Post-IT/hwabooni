package com.postit.hwabooni.presentation.news;

import android.app.usage.UsageStatsManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.R;
import com.postit.hwabooni.data.repository.EventRepository;
import com.postit.hwabooni.data.repository.FriendRepository;
import com.postit.hwabooni.data.repository.PlantRepository;
import com.postit.hwabooni.databinding.FragmentNewsBinding;
import com.postit.hwabooni.model.FriendData;
import com.postit.hwabooni.model.News;
import com.postit.hwabooni.model.PlantData;
import com.postit.hwabooni.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    FragmentNewsBinding binding;
    NewsRecyclerViewAdapter adapter;

    User me;
    ArrayList<FriendData> friendDataList;
    HashMap<String, String> friendsNameMap;
    HashMap<String, String> friendsPhoneMap;
    HashMap<String, PlantData> friendsPlantMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UsageStatsManager usm = getContext().getSystemService(UsageStatsManager.class);
        friendsNameMap = new HashMap<>();
        friendsPhoneMap = new HashMap<>();
        friendDataList = new ArrayList<>();

        View temp = view.findViewById(R.id.toolBar);
        if (temp == null) Log.d(TAG, "onViewCreated: strange");
        adapter = new NewsRecyclerViewAdapter();
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.newsRecyclerView.setAdapter(adapter);

        binding.refreshLayout.setOnRefreshListener(() -> {
            binding.refreshLayout.setRefreshing(false);
        });

        if(auth.getCurrentUser() == null) return;
        db.collection("User").document(auth.getCurrentUser().getEmail()).get().addOnCompleteListener((task) -> {
            if (task.isSuccessful()) {
                me = task.getResult().toObject(User.class);
                new FriendRepository().getFriendsData(me.getFollower(), (data) -> {
                    friendDataList = data;
                    for (FriendData friendData : data) {
                        friendsNameMap.put(friendData.getId(), friendData.getName());
                        friendsPhoneMap.put(friendData.getId(), friendData.getPhone());
                    }
                    new PlantRepository().getFriendsPlantList(data, (friendsPlantMap) -> {
                        this.friendsPlantMap = friendsPlantMap;
                        new EventRepository().getEventList(friendDataList, (newsList) -> {
                            for (News news : newsList) {
                                if (news.getType() == 1 || news.getType() == 2) {
                                    news.setName(friendsNameMap.get(news.getEmail()));
                                    PlantData plantData = friendsPlantMap.get(news.getEmail() + "/" + news.getData());
                                    Log.d(TAG, "onViewCreated: " + plantData);
                                    if (plantData != null) {
                                        Log.d(TAG, "onViewCreated: " + plantData);
                                        news.setData(plantData.getName());
                                        news.setPicture(plantData.getPicture());
                                    }
                                }
                                if(news.getType()==3){
                                    news.setName(friendsNameMap.get(news.getEmail()));
                                }
                                news.setPhone(friendsPhoneMap.get(news.getEmail()));
                            }
                            adapter.submitList(newsList);
                        });
                    });
                });
            }
        });
    }
}
