package com.postit.hwabooni.presentation.plant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.FragmentPlantBinding;
import com.postit.hwabooni.model.PlantData;
import com.postit.hwabooni.model.PlantTempData;

import java.util.ArrayList;

public class PlantFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentPlantBinding binding;
    private ArrayList<PlantData> arrayList;
    private PlantAdapter plantAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

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
        arrayList = new ArrayList<>();

        plantAdapter = new PlantAdapter(arrayList);

        plantAdapter.listener = new PlantAdapter.OnClickListener() {
            @Override
            public void onClick(String id) {
                Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
                for(PlantData pd : arrayList){
                    if(pd.getId().equals(id)){
                        String imageUrl = pd.getMyPlantPicture();
                        Glide.with(getContext()).load(imageUrl).into(binding.ivPlant);

                        if(pd.getPrettyWord()!=null){
                            binding.ivPrettyWord.setImageResource(R.drawable.button_misson_completion);
                        }
                        else{
                            binding.ivPrettyWord.setImageResource(R.drawable.button_mission_incompletion);
                            Log.d("오늘 예쁜말 없음", "오늘 예쁜말 없음");
                        }
                        break;
                    }
                }

            }
        };
        recyclerView.setAdapter(plantAdapter);

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlantData plantData = new PlantData();
                plantData.setmyPlantName("정집민");
                arrayList.add(plantData);
                plantAdapter.notifyDataSetChanged();
            }
        });

        db.collection("dummyPlant").get().addOnCompleteListener((document)->{
            if(document.isSuccessful()){
                ArrayList<PlantData> data = new ArrayList<>();
                ArrayList<PlantTempData> tempData = new ArrayList<>();
                for (DocumentSnapshot doc : document.getResult().getDocuments()){
                    PlantData temp = doc.toObject(PlantData.class);
                    data.add(temp);
//                    db.collection("dummyPlant").document(temp.getId()).collection("tempRecord").get()
//                            .addOnCompleteListener((docu)->{
//                                if(docu.isSuccessful()){
//                                    PlantTempData tempTemp = docu.getResult().getDocuments().get(0).toObject(PlantTempData.class);
//                                    Log.d("온도",tempTemp.getTemperature().toString());
//                                }
//
//                            });
                }

                arrayList.addAll(data);
                plantAdapter.notifyDataSetChanged();
                if(arrayList.get(0)!=null){
                    Log.d("FirstTest", arrayList.get(0).getmyPlantName());

                    String imageUrl = arrayList.get(0).getMyPlantPicture();
                    Glide.with(getContext()).load(imageUrl).into(binding.ivPlant);

                    if(arrayList.get(0).getPrettyWord()!=null){
                        binding.ivPrettyWord.setImageResource(R.drawable.button_misson_completion);
                    }
                    else{
                        binding.ivPrettyWord.setImageResource(R.drawable.button_mission_incompletion);
                        Log.d("오늘 예쁜말 없음", "오늘 예쁜말 없음");
                    }
                }
            }
        });








    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
}
