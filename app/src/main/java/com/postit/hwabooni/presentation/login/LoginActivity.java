package com.postit.hwabooni.presentation.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.BuildConfig;
import com.postit.hwabooni.databinding.ActivityLoginBinding;
import com.postit.hwabooni.presentation.signup.SignUpActivity;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    ActivityLoginBinding binding;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bindLoginButton();
        bindSignUpButton();
    }


    private void bindSignUpButton() {
        binding.signUpButton.setOnClickListener((v) -> {
            Intent intent = new Intent(this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initViews();
    }

    private void initViews() {
        FirebaseUser user = auth.getCurrentUser();
        updateUI(user);
    }

    private boolean checkLogin() {
        FirebaseUser user = auth.getCurrentUser();
        return user != null;
    }

    void updateUI(FirebaseUser user) {
        if (user == null) {
            binding.idEditText.setText(null);
            binding.idEditText.setEnabled(true);
            binding.passwordEditText.setEnabled(true);
            binding.idEditText.setText("");
            binding.accountButtonLayout.setVisibility(View.VISIBLE);
            binding.accountInfoButtonLayout.setVisibility(View.GONE);
            binding.passwordEditText.setText("");
            binding.loginButton.setText("로그인");
        } else {
            binding.idEditText.setText(user.getEmail());
            binding.idEditText.setEnabled(false);
            binding.passwordEditText.setText("******");
            binding.passwordEditText.setEnabled(false);
            binding.accountInfoButtonLayout.setVisibility(View.VISIBLE);
            binding.accountButtonLayout.setVisibility(View.GONE);
            binding.loginButton.setText("로그아웃");
        }
    }

    void bindLoginButton() {
        binding.loginButton.setOnClickListener((v) -> {
            if (auth.getCurrentUser() == null) {
                String id = binding.idEditText.getEditableText().toString();
                String password = binding.passwordEditText.getEditableText().toString();
                if (id.equals("")) {
                    return;
                }
                if (password.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                requestLogin(id, password);
            } else {
                binding.progressView.setVisibility(View.VISIBLE);
                auth.signOut();
                updateUI(null);
                binding.progressView.setVisibility(View.GONE);
            }
        });
    }

    void requestLogin(String id, String password) {
        binding.progressView.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(id, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        binding.progressView.setVisibility(View.GONE);
                    }
                });
    }

}
