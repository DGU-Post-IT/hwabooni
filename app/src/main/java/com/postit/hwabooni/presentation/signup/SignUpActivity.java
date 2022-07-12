package com.postit.hwabooni.presentation.signup;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.postit.hwabooni.databinding.ActivitySignupBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignupBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.maleButton.setOnClickListener((v)->{
            if(binding.maleButton.isChecked()) {
                binding.maleButton.setTextColor(Color.WHITE);
                binding.femaleButton.setTextColor(getColor(android.R.color.holo_red_light));
            }
        });
        binding.femaleButton.setOnClickListener((v)->{
            if(binding.femaleButton.isChecked()) {
                binding.femaleButton.setTextColor(Color.WHITE);
                binding.maleButton.setTextColor(getColor(android.R.color.holo_red_light));
            }
        });
    }
}
