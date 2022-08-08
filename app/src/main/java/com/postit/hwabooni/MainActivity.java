package com.postit.hwabooni;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.postit.hwabooni.databinding.ActivityMainBinding;
import com.postit.hwabooni.databinding.AppbarMainBinding;
import com.postit.hwabooni.presentation.friend.FriendFragment;
import com.postit.hwabooni.presentation.login.LoginActivity;
import com.postit.hwabooni.presentation.market.MarketFragment;
import com.postit.hwabooni.presentation.news.NewsFragment;
import com.postit.hwabooni.presentation.plant.PlantFragment;

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

    }

}