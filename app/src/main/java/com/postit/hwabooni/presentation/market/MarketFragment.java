package com.postit.hwabooni.presentation.market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.databinding.FragmentMarketBinding;
import com.postit.hwabooni.model.News;
import com.postit.hwabooni.model.SaleArticle;

import java.util.ArrayList;

public class MarketFragment  extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentMarketBinding binding;
    SaleArticleRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMarketBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new SaleArticleRecyclerViewAdapter();
        binding.saleArticleRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext(), RecyclerView.VERTICAL,false));
        binding.saleArticleRecyclerView.setAdapter(adapter);
        db.collection("dummySale").get().addOnCompleteListener((document)->{
            if(document.isSuccessful()){
                ArrayList<SaleArticle> data = new ArrayList<>();
                for (DocumentSnapshot doc : document.getResult().getDocuments()){
                    data.add(doc.toObject(SaleArticle.class));
                }
                adapter.submitList(data);
            }
        });
    }
}
