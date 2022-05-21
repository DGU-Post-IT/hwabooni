package com.postit.hwabooni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;

import com.postit.hwabooni.databinding.ActivityMainBinding;
import com.postit.hwabooni.databinding.AppbarMainBinding;
import com.postit.hwabooni.presentation.friend.FriendFragment;
import com.postit.hwabooni.presentation.market.MarketFragment;
import com.postit.hwabooni.presentation.news.NewsFragment;
import com.postit.hwabooni.presentation.plant.PlantFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    AppbarMainBinding appbarMainBinding;




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
                case R.id.market:getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new MarketFragment()).commit();break;
            }
            return true;
        });

    }


}