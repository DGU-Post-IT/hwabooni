package com.postit.hwabooni.data.repository;

import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.model.FriendData;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class FriendRepository {
    private static final String TAG = "FriendRepository";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getFriendsData(ArrayList<String> followers, Callback callback){
        if(followers==null) return;
        AtomicInteger cnt = new AtomicInteger(followers.size());
        ArrayList<FriendData> data = new ArrayList<>();
        for (String id : followers){
            db.collection("User").document(id).get().addOnCompleteListener((task)->{
                if(task.isSuccessful()){
                    if(task.getResult().getData()!=null){
                        data.add(task.getResult().toObject(FriendData.class));
                    }
                }
                if(cnt.decrementAndGet()==0){
                    callback.invoke(data);
                }
            });
        }
    }

    @FunctionalInterface
    public interface Callback{
        void invoke(ArrayList<FriendData> data);
    }
}

