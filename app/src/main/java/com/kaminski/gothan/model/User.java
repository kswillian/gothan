package com.kaminski.gothan.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.kaminski.gothan.R;
import com.kaminski.gothan.activity.MainActivity;
import com.kaminski.gothan.firebase.Firebase;
import com.kaminski.gothan.util.Base64Custom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class User implements Serializable {

    private String id;
    private String name;
    private String cpf;
    private String email;
    private String password;
    private String imgUrl;
    private int yearsOld;
    public User() {
    }

    public User(String id, String name, String cpf, String email, String password, String imgUrl, int yearsOld) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.email = email;
        this.password = password;
        this.imgUrl = imgUrl;
        this.yearsOld = yearsOld;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getYearsOld() {
        return yearsOld;
    }

    public void setYearsOld(int yearsOld) {
        this.yearsOld = yearsOld;
    }

    public void register(){
        DatabaseReference databaseReference = Firebase.getFirebase();
        databaseReference
                .child("users")
                .child(Base64Custom.encodeBase64(this.email))
                .setValue(this);
    }

    public void update(final Resources resources, final Context context){
        DatabaseReference databaseReference = Firebase.getFirebase();
        DatabaseReference ref = databaseReference.child("users").child(this.id);
        Map<String, Object> map = toMap();
        ref.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                AlertDialog.Builder msg = new AlertDialog.Builder(context);
                msg.setTitle(resources.getString(R.string.alert_sucess_register_title));
                msg.setMessage(resources.getString(R.string.alert_sucess_register_update));
                msg.setPositiveButton(resources.getString(R.string.alert_neutral_button), null);
                msg.show();
            }
        });
    }

    @Exclude
    public Map<String, Object> toMap() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("cpf", this.cpf);
        result.put("email", this.email);
        result.put("id", this.id);
        result.put("imgUrl", this.imgUrl);
        result.put("name", this.name);
        result.put("password", this.password);

        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", yearsOld=" + yearsOld +
                '}';
    }
}
