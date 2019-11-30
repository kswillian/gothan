package com.kaminski.gothan.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    private static FirebaseAuth auth;
    private static DatabaseReference databaseReference;

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
