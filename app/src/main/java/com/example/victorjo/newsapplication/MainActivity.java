package com.example.victorjo.newsapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationView navigationView;

    @LayoutRes
    protected int getLayoutResId(){
        return R.layout.activity_main;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());


        openFragement(FeedsFragment.newInstance());


        navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.news_feeds:
                    openFragement(FeedsFragment.newInstance());
                    return true;
                case R.id.search:
                    openFragement(NewsSearch.newInstance());
                    return true;
                case R.id.bookmarks:


                    return true;
                case R.id.more:

                    return true;

                default:
                    return false;
            }
        });

    }


    protected void openFragement(Fragment fragmentNew){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.frag_container);

        if(fragment == null) {
            fragment = fragmentNew;
            fm.beginTransaction().add(R.id.frag_container, fragment).commit();
        }else{
           fm.beginTransaction().remove(fragment).commit();
           fragment = fragmentNew;
           fm.beginTransaction().add(R.id.frag_container, fragment).commit();
        }

    }


}
