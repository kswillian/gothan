package com.kaminski.gothan.firebase;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaminski.gothan.model.User;
import com.kaminski.gothan.util.Base64Custom;

public class Firebase {

    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;
    private static User user;

    //Retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAuth(){

        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    //Retorna a instancia do FirebaseDatabase
    public static DatabaseReference getFirebase(){
        if(databaseReference == null){
            databaseReference = FirebaseDatabase.getInstance().getReference();
        }
        return databaseReference;
    }
}
