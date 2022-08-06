package com.postit.hwabooni.presentation.plant;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.FragmentPlantBinding;
import com.postit.hwabooni.model.PlantData;
import com.postit.hwabooni.model.PlantRecord;

import java.util.ArrayList;
import java.util.HashMap;

public class PlantFragment extends Fragment {
    private static final String TAG = "PlantFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    FragmentPlantBinding binding;
    private ArrayList<PlantData> plantDataList;
    private HashMap<String, Double> hashTemp;
    private HashMap<String, Double> hashHumid;
    private PlantAdapter plantAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    String currentPlantName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPlantBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = binding.rvPlant;
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        plantDataList = new ArrayList<>();

        hashTemp = new HashMap<>();
        hashHumid = new HashMap<>();
        plantAdapter = new PlantAdapter(plantDataList);

        plantAdapter.listener = (id,plant) -> {
            loadPlantInfo(plant);
        };
        recyclerView.setAdapter(plantAdapter);
        currentPlantName = "";


        binding.btnAdd.setOnClickListener(view1 -> new PlantAddFragment().show(requireActivity().getSupportFragmentManager(), "PLANT_ADD"));


        binding.btnPlantDelete.setOnClickListener(view1 -> {
            db.collection("User").document(auth.getCurrentUser().getEmail()).collection("plant").document(currentPlantName).delete()
                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "해당 식물 삭제");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "해당 식물 삭제 실패", e);
                    }
                });
            Toast.makeText(getContext(), "해당 식물 삭제", Toast.LENGTH_LONG).show();
        });

        db.collection("User").document(auth.getCurrentUser().getEmail()).collection("plant").get().addOnCompleteListener((task)->{
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
        db.collection("User").document(auth.getCurrentUser().getEmail())
                .collection("plant").document(plant.getId())
                .collection("record")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1).get().addOnCompleteListener((task)->{
                   if(task.isSuccessful()){
                       //Log.d(TAG, "loadPlantInfo: 성공");
                       if(task.getResult()!=null){
                           try{
                               PlantRecord record = task.getResult().toObjects(PlantRecord.class).get(0);
                               showPlantInfo(plant,record);
                               Log.d(TAG, "현재 식물 이름: "+plant.getName());
                               currentPlantName = plant.getName();
                           }                           
                           catch(Exception e){
                               Log.d(TAG, "loadPlantInfo: 식물상태 로드 불가");
                               Toast.makeText(getContext(), "식물상태 로드 불가", Toast.LENGTH_SHORT).show();
                           }
                       }
                   }
                });
    }

    void showPlantInfo(PlantData plant,PlantRecord record){
        String imageUrl = plant.getPicture();
        Glide.with(getContext()).load(imageUrl).into(binding.ivPlant);

        // ** PrettyWord가 컬렉션형태라면 수정 필요
//        if (plant.getPrettyWord() != null) {
//            binding.ivPrettyWord.setImageResource(R.drawable.button_misson_completion);
//        } else {
//            binding.ivPrettyWord.setImageResource(R.drawable.button_mission_incompletion);
//        }

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
            Log.d(TAG, "setHumid: "+value);
            if(value <= 700) binding.humidIndicator.setValue(0.999);
            if(value >= 4000) binding.humidIndicator.setValue(0.001);
            else binding.humidIndicator.setValue(1-(value - 700)/3300);
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
            Log.d(TAG, "setTemp: "+value);
            if(value <= 15) binding.humidIndicator.setValue(0.999);
            if(value >= 25) binding.humidIndicator.setValue(0.001);
            else binding.tempIndicator.setValue((value-15)/10);
        }
    }
}
