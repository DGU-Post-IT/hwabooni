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
                        db.collection("dummyPlant").document(temp.getId()).collection("humidRecord").get().addOnCompleteListener((docu)->{
                            if(docu.isSuccessful()){
                                PlantHumidData tempData = docu.getResult().getDocuments().get(0).toObject(PlantHumidData.class);
                                double humid = tempData.getHumidity();
                                Log.d("습도",String.valueOf(humid));
                                hashHumid.put(temp.getId(), humid);
                            }
                        });

                        db.collection("dummyPlant").document(temp.getId()).collection("tempRecord").get()
                                .addOnCompleteListener((docu)->{
                                    if(docu.isSuccessful()){
                                        PlantTempData tempData = docu.getResult().getDocuments().get(0).toObject(PlantTempData.class);
                                        double temper = tempData.getTemperature();
                                        Log.d("온도",String.valueOf(temper));
                                        hashTemp.put(temp.getId(), temper);
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

    public void setHumid(double humid){
        viewHumid(humid,4000, 700);
    }
    public void setTemp(double temp){
        viewTemp(temp, 25, 15);
    }

    void viewHumid(double value, int high, int low){
        int v = -1;

        if(value < low) v = 4;
        else if(value < low+(high-low)/3) v = 3;
        else if(value < low+(high-low)/3*2) v = 2;
        else if(value < high) v = 1;
        else if(value >= high) v = 0;

        switch(v){
            case 0 :
                binding.humid0.setVisibility(View.VISIBLE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.GONE);
                break;

            case 1 :
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.VISIBLE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.GONE);
                break;
            case 2 :
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.VISIBLE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.GONE);
                break;

            case 3 :
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.VISIBLE);
                binding.humid4.setVisibility(View.GONE);
                break;

            case 4 :
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.VISIBLE);
                break;

            default:
                binding.humid0.setVisibility(View.GONE);
                binding.humid1.setVisibility(View.GONE);
                binding.humid2.setVisibility(View.GONE);
                binding.humid3.setVisibility(View.GONE);
                binding.humid4.setVisibility(View.GONE);
                break;
        }

    }

    void viewTemp(double value, int high, int low){
        int v = -1;

        if(value < low) v = 0;
        else if(value < low+(high-low)/3) v = 1;
        else if(value < low+(high-low)/3*2) v = 2;
        else if(value < high) v = 3;
        else if(value >= high) v = 4;

        switch(v){
            case 0 :
                binding.temp0.setVisibility(View.VISIBLE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.GONE);
                break;

            case 1 :
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.VISIBLE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.GONE);
                break;
            case 2 :
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.VISIBLE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.GONE);
                break;

            case 3 :
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.VISIBLE);
                binding.temp4.setVisibility(View.GONE);
                break;

            case 4 :
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.VISIBLE);
                break;

            default:
                binding.temp0.setVisibility(View.GONE);
                binding.temp1.setVisibility(View.GONE);
                binding.temp2.setVisibility(View.GONE);
                binding.temp3.setVisibility(View.GONE);
                binding.temp4.setVisibility(View.GONE);
                break;
        }
    }


    public static String FRIEND_NAME_CODE = "1000";
}
