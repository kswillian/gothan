package com.kaminski.gothan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kaminski.gothan.R;
import com.kaminski.gothan.firebase.Firebase;
import com.kaminski.gothan.fragment.AboutFragment;
import com.kaminski.gothan.fragment.ConfigurationFragment;
import com.kaminski.gothan.fragment.HomeFragment;
import com.kaminski.gothan.fragment.OcurrencesFragment;
import com.kaminski.gothan.model.User;
import com.kaminski.gothan.util.Base64Custom;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout frameLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private TextView textMenuEmail;
    private TextView textMenuUser;
    private User userCurrent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frame_container);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        firebaseAuth = Firebase.getFirebaseAuth();
        databaseReference = Firebase.getFirebase();

        initComponent();

        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, homeFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {

            HomeFragment homeFragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, homeFragment);
            fragmentTransaction.commit();

        }else if (id == R.id.nav_map) {

            startActivity(new Intent(this, MapsActivity.class));

        } else if (id == R.id.nav_ocurrence) {

            OcurrencesFragment ocurrencesFragment = new OcurrencesFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, ocurrencesFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_about) {

            AboutFragment aboutFragment = new AboutFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, aboutFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_configuration){

            ConfigurationFragment configurationFragment = new ConfigurationFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_container, configurationFragment);
            fragmentTransaction.commit();

        } else if (id == R.id.nav_logout) {

            AlertDialog.Builder msg = new AlertDialog.Builder(MainActivity.this);
            msg.setTitle(getResources().getString(R.string.logout_title));
            msg.setMessage(getResources().getString(R.string.logout_description));
            msg.setPositiveButton(getResources().getString(R.string.logout_button_yes), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    firebaseAuth.signOut();
                    finish();
                }
            });
            msg.setNegativeButton(getResources().getString(R.string.logout_button_no), null);
            msg.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initComponent(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        textMenuUser = headerView.findViewById(R.id.textMenuUserName);
        textMenuEmail = headerView.findViewById(R.id.textMenuUserEmail);
    }

    @Override
    protected void onResume() {
        super.onResume();

        firebaseAuth = Firebase.getFirebaseAuth();
        databaseReference = Firebase.getFirebase();

        final DatabaseReference users = databaseReference.child("users");
        DatabaseReference searchUser = users.child(Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail()));

        searchUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCurrent = dataSnapshot.getValue(User.class);

                if(userCurrent != null) {
                    textMenuUser.setText(userCurrent.getName());
                    textMenuEmail.setText(userCurrent.getEmail());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
