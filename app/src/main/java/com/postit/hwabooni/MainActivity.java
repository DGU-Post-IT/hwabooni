package com.postit.hwabooni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
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

        this.settingSideNavBar();


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

    /***
     *  -> 사이드 네브바 세팅
     *   - 클릭 아이콘 설정
     *   - 아이템 클릭 이벤트 설정
     */
    public void settingSideNavBar()
    {
        Toolbar toolbar = findViewById(R.id.toolBar);

        if(toolbar==null){
            Log.d("MainActivity","strange");
        }

        setSupportActionBar(toolbar);

        // 사이드 메뉴를 오픈하기위한 아이콘 추가
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_48);

        // 사이드 네브바 구현
        DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                MainActivity.this,
                drawLayout,
                appbarMainBinding.toolBar,
                R.string.open,
                R.string.closed
        );

        // 사이드 네브바 클릭 리스너
        drawLayout.addDrawerListener(actionBarDrawerToggle);

        // -> 사이드 네브바 아이템 클릭 이벤트 설정
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();

                if (id == R.id.menu_item1){
                    Toast.makeText(getApplicationContext(), "메뉴아이템 1 선택", Toast.LENGTH_SHORT).show();
                }else if(id == R.id.menu_item2){
                    Toast.makeText(getApplicationContext(), "메뉴아이템 2 선택", Toast.LENGTH_SHORT).show();
                }else if(id == R.id.menu_item3){
                    Toast.makeText(getApplicationContext(), "메뉴아이템 3 선택", Toast.LENGTH_SHORT).show();
                }


                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }


    /***
     *  -> 뒤로가기시, 사이드 네브바 닫는 기능
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    public void InitializeLayout()
//    {
//        //toolBar를 통해 App Bar 생성
//        Toolbar toolbar = findViewById(R.id.toolBar);
//        View temp = (View)findViewById(R.id.toolBar);
//
//
//        if(toolbar==null){
//            Log.d("MainActivity","이거이상한데");
//        }
//        if(temp==null){
//            Log.d("MainActivity","temp strange");
//        }
//
//        setSupportActionBar(toolbar);
//
//        if(getSupportActionBar()!=null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_48);}
//
//
//        //App Bar의 좌측 영영에 Drawer를 Open 하기 위한 Incon 추가
//
//
//
//        DrawerLayout drawLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//
//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
//                this,
//                drawLayout,
//                toolbar,
//                R.string.open,
//                R.string.closed
//        );
//
//        if(drawLayout!=null){
//            drawLayout.addDrawerListener(actionBarDrawerToggle);
//        }
//
//        if(navigationView!=null){
//            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                    switch (menuItem.getItemId())
//                    {
//                        case R.id.menu_item1:
//                            Toast.makeText(getApplicationContext(), "SelectedItem 1", Toast.LENGTH_SHORT).show();
//                        case R.id.menu_item2:
//                            Toast.makeText(getApplicationContext(), "SelectedItem 2", Toast.LENGTH_SHORT).show();
//                        case R.id.menu_item3:
//                            Toast.makeText(getApplicationContext(), "SelectedItem 3", Toast.LENGTH_SHORT).show();
//                    }
//
//                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
//                    drawer.closeDrawer(GravityCompat.START);
//                    return true;
//                }
//            });
//        }
//
//    }
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//



}