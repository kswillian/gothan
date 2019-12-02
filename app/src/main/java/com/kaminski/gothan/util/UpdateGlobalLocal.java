package com.kaminski.gothan.util;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.kaminski.gothan.firebase.Firebase;

public class UpdateGlobalLocal {

    public static void updatelocation(String id, double lat, double lgt){

        DatabaseReference locationUser = Firebase.getFirebase().child("location_global");
        GeoFire geoFire = new GeoFire(locationUser);

        geoFire.setLocation(
                id,
                new GeoLocation(lat, lgt),
                new GeoFire.CompletionListener() {
                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        if(error != error){
                            System.out.println("ERROR");
                        }
                    }
                }
        );
    }
}
