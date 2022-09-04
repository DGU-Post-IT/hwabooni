package com.postit.hwabooni.data.repository;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.postit.hwabooni.model.FriendData;
import com.postit.hwabooni.model.News;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

public class EventRepository {

    private static final String TAG = "EventRepository";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void getEventList(ArrayList<FriendData> friends, Callback callback) {
        AtomicInteger cnt = new AtomicInteger(friends.size());
        ArrayList<News> newsArrayList = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.add(Calendar.DATE,-2);
        for (FriendData friend : friends) {
            db.collection("User").document(friend.getEmail())
                    .collection("event").whereGreaterThanOrEqualTo("timestamp", today.getTime())
                    .get().addOnCompleteListener((task) -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Log.d(TAG, doc.getId() + " => " + doc.getData());
                                News news = doc.toObject(News.class);
                                newsArrayList.add(news);
                            }
                        }
                        if (cnt.decrementAndGet() == 0) {
                            Collections.sort(newsArrayList, Comparator.comparing(News::getTimestamp).reversed());
                            callback.invoke(newsArrayList);
                        }
                    });
        }
    }

    @FunctionalInterface
    public interface Callback {
        void invoke(ArrayList<News> data);
    }
}
