package com.kaminski.gothan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kaminski.gothan.R;
import com.kaminski.gothan.firebase.Firebase;
import com.kaminski.gothan.model.Ocurrence;
import com.kaminski.gothan.util.Base64Custom;
import com.kaminski.gothan.util.UpdateGlobalLocal;

import java.util.ArrayList;
import java.util.Random;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GeoQueryEventListener {

    private GoogleMap mMap;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FloatingActionButton floatingActionButtonLocation;
    private com.github.clans.fab.FloatingActionMenu floatingActionButtonInfo;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private double latitude = 0;
    private double longitude = 0;

    private Spinner spinnerOcurrenceType;
    private EditText editTextDescriptionOcurrence;

    private Marker markerUser;
    private Marker markerOcurrence;
    private LatLng userLocal;

    private Circle circle;
    private GeoFire geoFire;

    private ArrayList<LatLng> dangerousArea;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        firebaseAuth = Firebase.getFirebaseAuth();
        databaseReference = Firebase.getFirebase();
        dangerousArea = new ArrayList<>();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initComponent();
        initEvent();
        listOcurrence();
        settiongGeofire();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        locateUser();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {


                AlertDialog.Builder msg = new AlertDialog.Builder(MapsActivity.this);
                LayoutInflater inflater = getLayoutInflater();

                View view = getLayoutInflater().inflate(R.layout.dialog_ocurrence, null);
                editTextDescriptionOcurrence = view.findViewById(R.id.editTextAlertOcurrenceDesc);
                spinnerOcurrenceType = view.findViewById(R.id.spinnerOcurrenceType);

                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                        getApplicationContext(),
                        R.array.alert_array, android.R.layout.simple_spinner_item
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerOcurrenceType.setAdapter(adapter);

                msg.setView(inflater.inflate(R.layout.dialog_ocurrence, null));
                msg.setTitle(getResources().getString(R.string.alert_ocurrence_title));
                msg.setPositiveButton(getResources().getString(R.string.alert_ocurrence_button_regs), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        registerOcurrence(latLng, spinnerOcurrenceType.getSelectedItem().toString(), "");
                        listOcurrence();
                    }
                });
                msg.setNegativeButton(getResources().getString(R.string.logout_button_no), null);
                msg.show();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void initComponent() {
        floatingActionButtonLocation = findViewById(R.id.floatingActionButtonMapLocation);
        floatingActionButtonInfo = findViewById(R.id.fab);
        floatingActionButtonLocation.setVisibility(View.INVISIBLE);
        floatingActionButtonInfo.setVisibility(View.INVISIBLE);
    }

    public void initEvent() {
        floatingActionButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveCameraToUserLocation();
            }
        });
    }

    @SuppressLint("RestrictedApi")
    public void visibilityFloatActions() {
        if (latitude != 0) {
            floatingActionButtonLocation.setVisibility(View.VISIBLE);
            floatingActionButtonInfo.setVisibility(View.VISIBLE);
        } else {
            floatingActionButtonLocation.setVisibility(View.INVISIBLE);
            floatingActionButtonInfo.setVisibility(View.INVISIBLE);
        }
    }

    public void locateUser() {

        if (markerOcurrence != null) {
            markerOcurrence.remove();
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();

                userLocal = new LatLng(latitude, longitude);
                visibilityFloatActions();

                listOcurrence();

                UpdateGlobalLocal.updatelocation(
                        Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail()),
                        latitude,
                        longitude
                );

                if (markerUser != null) {
                    markerUser.remove();
                }

                markerUser = mMap.addMarker(
                        new MarkerOptions().
                                position(userLocal)
                                .title(firebaseAuth.getCurrentUser().getDisplayName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.batman))
                );

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0,
                    locationListener
            );
        }
    }

    public void moveCameraToUserLocation() {
        LatLng coordinate = new LatLng(latitude, longitude);
        CameraUpdate myLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 20);
        mMap.animateCamera(myLocation);
    }

    public void registerOcurrence(LatLng latLng, String type, String description) {
        Ocurrence ocurrenceUser = new Ocurrence();
        ocurrenceUser.setId(Base64Custom.encodeBase64("" + latLng.latitude + latLng.longitude));
        ocurrenceUser.setDescription(description);
        ocurrenceUser.setType(type);
        ocurrenceUser.setLatitude(latLng.latitude);
        ocurrenceUser.setLongitude(latLng.longitude);
        ocurrenceUser.setUserId(Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail()));
        ocurrenceUser.register();
        listOcurrence();
    }

    public void listOcurrence() {

        DatabaseReference ocurrences = databaseReference.child("ocurrences");

        ocurrences.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(circle != null){
                    circle.remove();
                }

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Ocurrence ocurrence = ds.getValue(Ocurrence.class);

                    switch (ocurrence.getType()){
                        case "Assalto":

                            markerOcurrence = mMap.addMarker(
                                    new MarkerOptions()
                                            .position(new LatLng(ocurrence.getLatitude(), ocurrence.getLongitude()))
                                            .title(ocurrence.getType())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.coringa))
                            );

                            break;
                        case "Delito":

                            markerOcurrence = mMap.addMarker(
                                    new MarkerOptions()
                                            .position(new LatLng(ocurrence.getLatitude(), ocurrence.getLongitude()))
                                            .title(ocurrence.getType())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.charada))
                            );

                            break;
                    }

                    // Add circle
                    mMap.addCircle(
                            new CircleOptions()
                                    .center(new LatLng(ocurrence.getLatitude(), ocurrence.getLongitude()))
                                    .radius(50)
                                    .fillColor(Color.argb(90, 255, 153, 0))
                                    .strokeColor(Color.argb(190, 255, 152, 0))
                    );

                    // GeoQuery
                    GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(ocurrence.getLatitude(), ocurrence.getLongitude()), 5.0f);
                    geoQuery.addGeoQueryEventListener(MapsActivity.this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void settiongGeofire(){
        databaseReference = Firebase.getFirebase().child("location_global");
        geoFire = new GeoFire(databaseReference);
    }

    public void sendNotification(String title, String content){

        String NOTIFICATION_CHANNEL_ID = "edmt_multiple_location";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "My Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.YELLOW);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.
                setContentTitle(title).
                setContentText(content).
                setAutoCancel(false).
                setSmallIcon(R.mipmap.ic_launcher).
                setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

        Notification notification = builder.build();
        notificationManager.notify(new Random().nextInt(), notification);
    }

    @Override
    public void onKeyEntered(String key, GeoLocation location) {
        sendNotification("Gothan", "Você entrou em uma zona de perigo!");
    }

    @Override
    public void onKeyExited(String key) {
        sendNotification("Gothan", "Deixando uma zona de perigo!");
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {
        //sendNotification("Gothan", "movendo se em uma zona de perigo");
    }

    @Override
    public void onGeoQueryReady() {

    }

    @Override
    public void onGeoQueryError(DatabaseError error) {

    }
}
