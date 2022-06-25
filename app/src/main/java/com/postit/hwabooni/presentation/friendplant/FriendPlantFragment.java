package com.postit.hwabooni.presentation.friendplant;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.FragmentFriendPlantBinding;
import com.postit.hwabooni.model.PlantData;
import com.postit.hwabooni.model.PlantHumidData;
import com.postit.hwabooni.model.PlantTempData;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendPlantFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @NonNull FragmentFriendPlantBinding binding;
    private ArrayList<PlantData> arrayList;
    private HashMap<String, Double> hashTemp;
    private HashMap<String, Double> hashHumid;
    private FriendPlantAdapter plantAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFriendPlantBinding.inflate(inflater, container, false);
        this.name = getArguments().getString(FRIEND_NAME_CODE);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(name==null) getActivity().getFragmentManager().popBackStack();
        binding.friendNameTextView.setText(name+"님의 식물들입니다.");
        recyclerView = binding.rvPlant;
        linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();

        hashTemp = new HashMap<>();
        hashHumid = new HashMap<>();
        plantAdapter = new FriendPlantAdapter(arrayList);

        plantAdapter.listener = new FriendPlantAdapter.OnClickListener() {
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

                        Log.d("새로운 Humid", id + String.valueOf(hashHumid.get(id)));
                        setHumid(hashHumid.get(id));
                        Log.d("새로운 Temp", id + String.valueOf(hashTemp.get(id)));
                        setTemp(hashTemp.get(id));


                        break;
                    }
                }

            }
        };
        recyclerView.setAdapter(plantAdapter);

        db.collection("dummyPlant").get().addOnCompleteListener((document)->{
            if(document.isSuccessful()){
                ArrayList<PlantData> data = new ArrayList<>();
                int i = 0;

                for (DocumentSnapshot doc : document.getResult().getDocuments()){

                    PlantData temp = doc.toObject(PlantData.class);
                    data.add(temp);
                    Log.d("ID", temp.getId());

                    //처음 식물만 화면에 바로 나타내주기 위해서
                    if(i == 0){
                        db.collection("dummyPlant").document(temp.getId()).collection("humidRecord").get().addOnCompleteListener((docu)->{
                            if(docu.isSuccessful()){
                                PlantHumidData tempData = docu.getResult().getDocuments().get(0).toObject(PlantHumidData.class);
                                double humid = tempData.getHumidity();
                                Log.d("습도",String.valueOf(humid));
                                hashHumid.put(temp.getId(), humid);
                                setHumid(humid);

                            }
                        });

                        db.collection("dummyPlant").document(temp.getId()).collection("tempRecord").get()
                                .addOnCompleteListener((docu)->{
                                    if(docu.isSuccessful()){
                                        PlantTempData tempData = docu.getResult().getDocuments().get(0).toObject(PlantTempData.class);
                                        double temper = tempData.getTemperature();
                                        Log.d("온도",String.valueOf(temper));
                                        hashTemp.put(temp.getId(), temper);
                                        setTemp(temper);

                                    }
                                });
                    }
                    else{
                        db.collection("dummyPlant").document(temp.getId()).collection("humidRecord").get().addOnCompleteListener((docu) -> {
                            if (docu.isSuccessful()) {
                                QuerySnapshot documentResult = docu.getResult();
                                if (documentResult.isEmpty()) {

                                } else {
                                    PlantHumidData tempData = docu.getResult().getDocuments().get(0).toObject(PlantHumidData.class);
                                    double humid = tempData.getHumidity();
                                    Log.d("습도", String.valueOf(humid));
                                    hashHumid.put(temp.getId(), humid);
                                }

                            } else {
                                Log.d("TAG", "humid와 temp 오류");
                            }
                        });

                        db.collection("dummyPlant").document(temp.getId()).collection("tempRecord").get()
                                .addOnCompleteListener((docu) -> {
                                    if (docu.isSuccessful()) {
                                        QuerySnapshot documentResult = docu.getResult();
                                        if (documentResult.isEmpty()) {

                                        } else {
                                            PlantTempData tempData = docu.getResult().getDocuments().get(0).toObject(PlantTempData.class);
                                            double temper = tempData.getTemperature();
                                            Log.d("온도", String.valueOf(temper));
                                            hashTemp.put(temp.getId(), temper);
                                        }

                                    } else {
                                        Log.d("TAG", "humid와 temp 오류");
                                    }
                                });
                    }

                    i++;
                }


                arrayList.addAll(data);

                plantAdapter.notifyDataSetChanged();
                if(arrayList.get(0)!=null){
                    Log.d("FirstTest", arrayList.get(0).getId()+"," +arrayList.get(0).getmyPlantName());

                    String imageUrl = arrayList.get(0).getMyPlantPicture();
                    Glide.with(getContext()).load(imageUrl).into(binding.ivPlant);

                    if(arrayList.get(0).getPrettyWord()!=null){
                        binding.ivPrettyWord.setImageResource(R.drawable.button_misson_completion);
                    }
                    else{
                        binding.ivPrettyWord.setImageResource(R.drawable.button_mission_incompletion);
                        Log.d("오늘 예쁜말 없음", "오늘 예쁜말 없음");
                    }

                    Log.d("처음 ID", arrayList.get(0).getId());
//                        Log.d("setHumid의 처음세팅", hashHumid.get(arrayList.get(0).getId()).toString());
//                        Log.d("setTemp의 처음세팅", hashTemp.get(arrayList.get(0).getId()).toString());
//                        setHumid(hashHumid.get(arrayList.get(0).getId()));
//                        setTemp(hashTemp.get(arrayList.get(0).getId()));
                }
            }

        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setHumid(double humid) {
        viewHumid(humid);
    }

    public void setTemp(double temp) {
        viewTemp(temp);
    }

    void viewHumid(double value) {
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

    void viewTemp(double value) {
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
}
