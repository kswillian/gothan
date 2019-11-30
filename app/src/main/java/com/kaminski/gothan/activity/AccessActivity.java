package com.kaminski.gothan.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.kaminski.gothan.R;

public class AccessActivity extends AppCompatActivity {

    private Button buttonRegister;
    private TextView textViewHaveAcount;

    @Override
    protected void onStart() {
        super.onStart();
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access);

        initComponent();
        clickEvents();
    }

    public void initComponent(){
        buttonRegister = findViewById(R.id.buttonAccRegister);
        textViewHaveAcount = findViewById(R.id.textViewAccLogin);
    }

    public void clickEvents(){

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        textViewHaveAcount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }
}
