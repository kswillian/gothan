package com.kaminski.gothan.activity;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kaminski.gothan.R;
import com.kaminski.gothan.firebase.Firebase;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FloatingActionButton floatingActionButtonLocation;
    private com.github.clans.fab.FloatingActionMenu floatingActionButtonInfo;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double latitude = 0;
    private double longitude = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebaseAuth = Firebase.getFirebaseAuth();
        initComponent();
        initEvent();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locateUser();
    }

    @SuppressLint("RestrictedApi")
    public void initComponent() {
        floatingActionButtonLocation = findViewById(R.id.floatingActionButtonMapLocation);
        floatingActionButtonInfo = findViewById(R.id.fab);

        floatingActionButtonLocation.setVisibility(View.INVISIBLE);
        floatingActionButtonInfo.setVisibility(View.INVISIBLE);
    }

    public void initEvent(){

        floatingActionButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveCameraToUserLocation();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void visibilityFloatActions(){

        if (latitude != 0) {
            floatingActionButtonLocation.setVisibility(View.VISIBLE);
            floatingActionButtonInfo.setVisibility(View.VISIBLE);
        }else{
            floatingActionButtonLocation.setVisibility(View.INVISIBLE);
            floatingActionButtonInfo.setVisibility(View.INVISIBLE);
        }
    }

    public void locateUser() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                LatLng myLocal = new LatLng(latitude, longitude);

                mMap.clear();
                visibilityFloatActions();
                mMap.addMarker(new MarkerOptions().position(myLocal).title("Eu estou aqui"));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    1000,
                    0,
                    locationListener
            );
        }
    }

    public void moveCameraToUserLocation(){

        LatLng coordinate = new LatLng(latitude, longitude);

        CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(
                coordinate, 15);
        mMap.animateCamera(myLocation);
    }
}
