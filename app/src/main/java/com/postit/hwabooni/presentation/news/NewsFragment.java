package com.postit.hwabooni.presentation.news;

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
import com.postit.hwabooni.databinding.FragmentNewsBinding;
import com.postit.hwabooni.model.News;

import java.util.ArrayList;

public class NewsFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentNewsBinding binding;
    NewsRecyclerViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNewsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new NewsRecyclerViewAdapter();
        binding.newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        binding.newsRecyclerView.setAdapter(adapter);

        db.collection("dummy").get().addOnCompleteListener((document)->{
            if(document.isSuccessful()){
                ArrayList<News> data = new ArrayList<>();
                for (DocumentSnapshot doc : document.getResult().getDocuments()){
                    data.add(doc.toObject(News.class));
                }
                adapter.submitList(data);
            }
        });
    }
}
