package com.postit.hwabooni.presentation.emotionrecord;

import android.content.DialogInterface;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd:hh:mm:s");
        String docSubject = format.format(today);
        String eventSubject =  format2.format(today);
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

        Map<String,Object> data3 = new HashMap<>();
        data3.put("email",auth.getCurrentUser().getEmail());
        data3.put("data",String.valueOf(emotion.getType()));
        data3.put("type",3);
        data3.put("timestamp",new Timestamp(today));
        DocumentReference emotionRef2 = db.collection("User").document(auth.getCurrentUser().getEmail())
                .collection("event").document(eventSubject);
        batch.set(emotionRef2, data3);

        batch.commit().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                showToast("오늘의 감정 기록이 완료되었습니다!");
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        FragmentManager fm = getActivity().getSupportFragmentManager();

        Fragment f = fm.findFragmentByTag("FRIEND_FRAGMENT");
        if(f instanceof DialogInterface.OnDismissListener){
            ((DialogInterface.OnDismissListener) f).onDismiss(dialog);
        }
    }

    void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }
}
