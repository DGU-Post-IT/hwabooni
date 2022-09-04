package com.postit.hwabooni.presentation.friendplant;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.hwabooni.R;
import com.postit.hwabooni.databinding.FragmentFriendPlantBinding;
import com.postit.hwabooni.model.PlantData;
import com.postit.hwabooni.model.PlantRecord;
import com.postit.hwabooni.presentation.friend.FriendAddFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class FriendPlantFragment extends Fragment {

    private static final String TAG = "FriendPlantFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

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


        binding.btnFriendDelete.setOnClickListener(view1 -> {
            DocumentReference documentRef = db.collection("User").document(auth.getCurrentUser().getEmail());
            documentRef.update("follower", FieldValue.arrayRemove(email))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("TAG", "친구삭제완료");
                            Toast.makeText(view.getContext(), "친구삭제완료", Toast.LENGTH_LONG).show();
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            fragmentManager.beginTransaction().remove(FriendPlantFragment.this).commit();
                            fragmentManager.popBackStack();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "친구삭제실패", Toast.LENGTH_LONG).show();
                            Log.w("TAG", "친구삭제실패", e);
                        }
                    });;
        });


    }
    void loadPlantInfo(PlantData plant){
        Log.d(TAG, "loadPlantInfo: ");
        db.collection("User").document(email)
                .collection("plant").document(plant.getId())
                .collection("record")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1).get().addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        if(task.getResult()!=null&&!task.getResult().isEmpty()){
                            PlantRecord record = task.getResult().toObjects(PlantRecord.class).get(0);
                            loadPlantPrettyWordRecord(plant,record);
                        }
                    }
                });
    }

    void loadPlantPrettyWordRecord(PlantData plant,PlantRecord record){
        Log.d(TAG, "loadPlantPrettyWordRecord: ");
        db.collection("User").document(email)
                .collection("plant").document(plant.getId())
                .collection("prettyWord")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1).get().addOnCompleteListener((task)->{
                    if(task.isSuccessful()){
                        if(task.getResult().isEmpty()) Log.d(TAG, "loadPlantPrettyWordRecord: empty result");
                        if(task.getResult()!=null&&!task.getResult().isEmpty()){
                            String data = task.getResult().getDocuments().get(0).getId().substring(0,10);
                            Log.d(TAG, "loadPlantPrettyWordRecord: "+data);
                            showPlantInfo(plant,record,data);
                        }else{
                            showPlantInfo(plant,record,null);
                        }
                    }
                });
    }

    void showPlantInfo(PlantData plant,PlantRecord record,String prettyWord){
        Log.d(TAG, "showPlantInfo: ");
        String imageUrl = plant.getPicture();
        Glide.with(getContext()).load(imageUrl).into(binding.ivPlant);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (format.format(new Date()).equals(prettyWord)) {
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
            if(value <= 700) {
                binding.humidIndicator.setValue(0.999);
            } else if(value >= 4000) {
                binding.humidIndicator.setValue(0.001);
            } else {
                binding.humidIndicator.setValue(1 - (value - 700) / 3300);
            }
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
            if(value <= 20) {
                binding.tempIndicator.setValue(0.001);
            } else if(value >= 30) {
                binding.tempIndicator.setValue(0.999);
            } else {
                binding.tempIndicator.setValue((value - 15) / 10);
            }
        }
    }

    public static String FRIEND_NAME_CODE = "1000";
    public static String FRIEND_EMAIL_CODE = "2000";
}
