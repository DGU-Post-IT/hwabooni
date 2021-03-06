package com.postit.hwabooni.presentation.signup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.postit.hwabooni.databinding.ActivitySignupBinding;
import com.postit.hwabooni.model.User;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore db;
    boolean male = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        binding.maleButton.setOnClickListener((v) -> {
            if (binding.maleButton.isChecked()) {
                binding.maleButton.setTextColor(Color.WHITE);
                binding.femaleButton.setTextColor(getColor(android.R.color.holo_red_light));
            }
            male = true;
        });
        binding.femaleButton.setOnClickListener((v) -> {
            if (binding.femaleButton.isChecked()) {
                binding.femaleButton.setTextColor(Color.WHITE);
                binding.maleButton.setTextColor(getColor(android.R.color.holo_red_light));
            }
            male = false;
        });

        auth = FirebaseAuth.getInstance();
//        if(auth.getCurrentUser()==null){
//            Log.d("TAG", "?????? null");
//            //auth.signOut();
//        }
//        if(auth.getCurrentUser()!=null){
//            Log.d("TAG", "?????? notnull");
//            auth.signOut();
//        }

        binding.signUpButton.setOnClickListener(view -> {
            signup();
        });


    }


    void signup() {

        String id = binding.tvEmail.getText().toString();
        String password = binding.tvPhonenumber.getText().toString();

        if (!id.matches("^(.+)@(.+)$")) {
            Toast.makeText(this, "????????? ????????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "????????? ??????????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    Log.d("SignUpActivity", "??????????????????");
                    //Intent intent = new Intent(getApplicationContext(), MyInfoActivity.class);
                    //startActivity(intent);
                    //finish();
                } else {
                    Log.d("SignUpActivity", "??????????????????");
                    Toast.makeText(getApplicationContext(), "?????? ????????? ?????????????????????..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        User user = new User();
        user.setAddress(binding.tvAddress.getText().toString());
        user.setEmail(id);
        user.setAge(Integer.parseInt(binding.tvAge.getText().toString()));
        user.setName(binding.tvName.getText().toString());
        user.setPhone(binding.tvPhonenumber.getText().toString());

        if(male == true) user.setSex("???");
        else user.setSex("???");

        db.collection("User").document(id)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("SignUpActivity", "???????????? ??? DB ?????? ??????");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("SignUpActivity", "???????????? ??? DB ?????? ??????");
                        Log.w("upload!!", "Error writing document", e);
                    }
                });

    }
}
