package com.postit.hwabooni.presentation.emotionrecord;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.postit.hwabooni.databinding.FragmentEmotionRecordBinding;
import com.postit.hwabooni.model.Emotion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EmotionRecordFragment extends DialogFragment {

    private static final String TAG = "EmotionRecordFragment";

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentEmotionRecordBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEmotionRecordBinding.inflate(inflater,container,false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView();
    }

    private void bindView() {
        binding.happyButton.setOnClickListener(v->{
            writeEmotionToDb(Emotion.HAPPY);
        });
        binding.sadButton.setOnClickListener(v->{
            writeEmotionToDb(Emotion.SAD);
        });
        binding.angryButton.setOnClickListener(v->{
            writeEmotionToDb(Emotion.ANGRY);
        });
        binding.nervousButton.setOnClickListener(v->{
            writeEmotionToDb(Emotion.NERVOUS);
        });
        binding.hurtButton.setOnClickListener(v->{
            writeEmotionToDb(Emotion.HURT);
        });
        binding.confusedButton.setOnClickListener(v->{
            writeEmotionToDb(Emotion.CONFUSE);
        });
    }

    void writeEmotionToDb(Emotion emotion){

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String docSubject = format.format(today);
        Log.d(TAG, "writeEmotionToDb: "+docSubject);

        WriteBatch batch = db.batch();
        Map<String,Object> data1 = new HashMap<>();
        data1.put("emotion",String.valueOf(emotion.getType()));
        DocumentReference userRef = db.collection("User").document(auth.getCurrentUser().getEmail());
        batch.update(userRef,data1);

        Map<String,Object> data2 = new HashMap<>();
        data2.put("emotion",emotion.getType());
        data2.put("timestamp",new Timestamp(today));
        DocumentReference emotionRef = db.collection("User").document(auth.getCurrentUser().getEmail())
                .collection("emotion").document(docSubject);
        batch.set(emotionRef, data2);

        batch.commit().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                showToast("오늘의 감정 기록이 완료되었습니다!");
                dismiss();
            }
        });
    }

    void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
