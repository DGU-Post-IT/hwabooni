package com.postit.hwabooni.data.repository;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.hwabooni.model.FriendData;
import com.postit.hwabooni.model.PlantData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PlantRepository {

    private static final String TAG = "PlantRepository";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getFriendsPlantList(ArrayList<FriendData> friends, Callback callback) {
        AtomicInteger cnt = new AtomicInteger(friends.size());
        HashMap<String,PlantData> plantDataHashMap = new HashMap<>();
        for (FriendData friend : friends) {
            db.collection("User").document(friend.getEmail()).collection("plant").get().addOnCompleteListener((task) -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        Log.d(TAG, doc.getId() + " => " + doc.getData());
                        PlantData plant = doc.toObject(PlantData.class);
                        plantDataHashMap.put(friend.getEmail()+"/"+doc.getId(), plant);
                    }
                }
                if(cnt.decrementAndGet()==0){
                    callback.invoke(plantDataHashMap);
                }
            });
        }
    }

    @FunctionalInterface
    public interface Callback {
        void invoke(HashMap<String,PlantData> data);
    }
}
