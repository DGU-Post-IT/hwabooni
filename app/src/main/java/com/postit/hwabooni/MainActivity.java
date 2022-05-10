package com.postit.hwabooni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.postit.hwabooni.databinding.ActivityMainBinding;
import com.postit.hwabooni.presentation.friend.FriendFragment;
import com.postit.hwabooni.presentation.news.NewsFragment;
import com.postit.hwabooni.presentation.plant.PlantFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();

        binding.navigationBar.setOnItemSelectedListener(item ->{
            switch (item.getItemId()){
                case R.id.main : getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new NewsFragment()).commit();break;
                case R.id.friend:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new FriendFragment()).commit();break;
                case R.id.plant:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new PlantFragment()).commit();break;
            }
            return true;
        });

    }

}