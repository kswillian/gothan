package com.kaminski.gothan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kaminski.gothan.R.layout.activity_register);
    }
}
