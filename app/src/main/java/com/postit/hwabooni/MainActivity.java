package com.postit.hwabooni;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.databinding.ActivityMainBinding;
import com.postit.hwabooni.databinding.AppbarMainBinding;
import com.postit.hwabooni.presentation.friend.FriendFragment;
import com.postit.hwabooni.presentation.login.LoginActivity;
import com.postit.hwabooni.presentation.market.MarketFragment;
import com.postit.hwabooni.presentation.news.NewsFragment;
import com.postit.hwabooni.presentation.plant.PlantFragment;
import com.postit.hwabooni.service.StatUploadService;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    AppbarMainBinding appbarMainBinding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            //refreshFragment();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(auth.getCurrentUser()==null){
            launcher.launch(new Intent(this, LoginActivity.class));
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();

        binding.navigationBar.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.main : getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewsFragment()).commit();break;
                case R.id.friend:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FriendFragment(),"FRIEND_FRAGMENT").commit();break;
                case R.id.plant:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PlantFragment()).commit();break;
                case R.id.market:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MarketFragment()).commit();break;
            }
            return true;
        });

        if(!checkStatPermission()){
            Toast.makeText(
                    this,
                    "앱 사용을 위해 사용 로그 권한이 필요합니다.",
                    Toast.LENGTH_LONG
            ).show();
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(checkStatPermission()) startService(new Intent(this, StatUploadService.class));
    }

    private boolean checkStatPermission(){
        AppOpsManager aom = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = aom.checkOpNoThrow(OPSTR_GET_USAGE_STATS, Process.myUid(), "com.postit.hwabooni");
        return mode == MODE_ALLOWED;
    }

}