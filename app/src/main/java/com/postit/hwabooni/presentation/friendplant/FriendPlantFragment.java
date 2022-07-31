package com.postit.hwabooni.presentation.friendplant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.FragmentFriendPlantBinding;
import com.postit.hwabooni.model.PlantData;
import com.postit.hwabooni.model.PlantRecord;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendPlantFragment extends Fragment {

    private static final String TAG = "FriendPlantFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull FragmentFriendPlantBinding binding;
    private ArrayList<PlantData> plantDataList;
    private HashMap<String, Double> hashTemp;
    private HashMap<String, Double> hashHumid;
    private FriendPlantAdapter plantAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String name;
    private String email;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendPlantBinding.inflate(inflater, container, false);
        this.name = getArguments().getString(FRIEND_NAME_CODE);
        this.email = getArguments().getString(FRIEND_EMAIL_CODE);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (name == null) getActivity().getFragmentManager().popBackStack();
        binding.friendNameTextView.setText(name + "님의 식물들입니다.");
        recyclerView = binding.rvPlant;
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        plantDataList = new ArrayList<>();

        hashTemp = new HashMap<>();
        hashHumid = new HashMap<>();
        plantAdapter = new FriendPlantAdapter(plantDataList);

        plantAdapter.listener = (id, plant) -> {
            loadPlantInfo(plant);
        };
        recyclerView.setAdapter(plantAdapter);

        db.collection("User").document(email).collection("plant").get().addOnCompleteListener((task)->{
            if(task.isSuccessful()){
                for(QueryDocumentSnapshot doc : task.getResult()){
                    Log.d(TAG, doc.getId() + " => " + doc.getData());
                    PlantData plant = doc.toObject(PlantData.class);
                    plantDataList.add(plant);
                }
                if(plantDataList.size()>0){
                    loadPlantInfo(plantDataList.get(0));
                }
                plantAdapter.notifyDataSetChanged();
            }
        });

    }

    void loadPlantInfo(PlantData plant){
        db.collection("User").document(email)
                .collection("plant").document(plant.getId())
                .collection("record")
                .orderBy("timestamp")
                .limit(1).get().addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        if(task.getResult()!=null&&!task.getResult().isEmpty()){
                            PlantRecord record = task.getResult().toObjects(PlantRecord.class).get(0);
                            showPlantInfo(plant,record);
                        }
                    }
                });
    }

    void showPlantInfo(PlantData plant,PlantRecord record){
        String imageUrl = plant.getPicture();
        Glide.with(getContext()).load(imageUrl).into(binding.ivPlant);

        if (plant.getPrettyWord() != null) {
            binding.ivPrettyWord.setImageResource(R.drawable.button_misson_completion);
        } else {
            binding.ivPrettyWord.setImageResource(R.drawable.button_mission_incompletion);
        }

        try {
            setHumid(record.getHumid());
            setTemp(record.getTemp());
        } catch (Exception e) {
            setHumid(-99999);
            setTemp(-99999);
        }
    }

    void setHumid(double value) {
        if (value == -99999) {
            binding.humidNo.setVisibility(View.VISIBLE);
            binding.humidIndicator.setVisibility(View.GONE);
            return;
        } else {
            binding.humidNo.setVisibility(View.GONE);
            binding.humidIndicator.setVisibility(View.VISIBLE);
            binding.humidIndicator.setValue((value - 700) / 3300);
        }
    }

    void setTemp(double value) {
        if (value == -99999) {
            binding.tempNo.setVisibility(View.VISIBLE);
            binding.tempIndicator.setVisibility(View.GONE);
            return;
        }else{
            binding.tempNo.setVisibility(View.GONE);
            binding.tempIndicator.setVisibility(View.VISIBLE);
            binding.tempIndicator.setValue((value-15)/10);
        }
    }

    public static String FRIEND_NAME_CODE = "1000";
    public static String FRIEND_EMAIL_CODE = "2000";
}
