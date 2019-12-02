package com.kaminski.gothan.model;

import com.google.firebase.database.DatabaseReference;
import com.kaminski.gothan.firebase.Firebase;

import java.io.Serializable;

public class Ocurrence implements Serializable {

    private String id;
    private String type;
    private String description;
    private Double latitude;
    private Double longitude;
    private String userId;

    public Ocurrence() {
    }

    public Ocurrence(String id, String type, String description, Double latitude, Double longitude, String userId) {
        this.id = id;
        this.type = type;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void register(){
        DatabaseReference databaseReference = Firebase.getFirebase();

        databaseReference
                .child("ocurrences")
                .child(this.id)
                .setValue(this);


        databaseReference
                .child("location_global")
                .child(this.id)
                .setValue(this);
    }

    @Override
    public String toString() {
        return "Ocurrence{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", userId='" + userId + '\'' +
                '}';
    }
}
