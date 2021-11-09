package com.adrian.practica4;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;

public class MenuActivity extends AppCompatActivity {

    //UI
    BottomAppBar appBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        appBar = findViewById(R.id.bottom_app_bar);

        appBar.setNavigationOnClickListener(v->{
            appBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        });
    }
}