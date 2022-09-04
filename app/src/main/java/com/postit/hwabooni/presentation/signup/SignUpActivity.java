package com.postit.hwabooni.presentation.signup;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

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
//            Log.d("TAG", "현재 null");
//            //auth.signOut();
//        }
//        if(auth.getCurrentUser()!=null){
//            Log.d("TAG", "현재 notnull");
//            auth.signOut();
//        }

        binding.signUpButton.setOnClickListener(view -> {
            signup();
        });

        binding.buttonHelpCall.setOnClickListener((v) -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:01088584584"));
            startActivity(intent);
        });


    }


    void signup() {

        String id = binding.tvEmail.getText().toString();
        String password = binding.tvPhonenumber.getText().toString();

        if (!id.matches("^(.+)@(.+)$")) {
            Toast.makeText(this, "올바른 이메일 형식을 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(id, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "계정을 성공적으로 생성하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("SignUpActivity", "회원가입성공");
                    //Intent intent = new Intent(getApplicationContext(), MyInfoActivity.class);
                    //startActivity(intent);
                    //finish();
                } else {
                    Log.d("SignUpActivity", "회원가입실패");
                    Toast.makeText(getApplicationContext(), "계정 생성에 실패하였습니다..", Toast.LENGTH_SHORT).show();
                }
            }
        });

        User user = new User();
        user.setAddress(binding.tvAddress.getText().toString());
        user.setEmail(id);
        user.setAge(Integer.parseInt(binding.tvAge.getText().toString()));
        user.setName(binding.tvName.getText().toString());
        user.setPhone(binding.tvPhonenumber.getText().toString());

        if(male == true) user.setSex("남");
        else user.setSex("여");

        db.collection("User").document(id)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("SignUpActivity", "회원가입 후 DB 생성 성공");
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("SignUpActivity", "회원가입 후 DB 생성 실패");
                        Log.w("upload!!", "Error writing document", e);
                    }
                });

    }
}
