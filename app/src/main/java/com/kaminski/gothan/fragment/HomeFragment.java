package com.kaminski.gothan.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kaminski.gothan.R;
import com.kaminski.gothan.firebase.Firebase;
import com.kaminski.gothan.model.User;
import com.kaminski.gothan.util.Base64Custom;

public class HomeFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private User userCurrent;

    private TextView textViewText1, textViewText2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        initComponent(v);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        firebaseAuth = Firebase.getFirebaseAuth();
        databaseReference = Firebase.getFirebase();

        final DatabaseReference users = databaseReference.child("users");
        DatabaseReference searchUser = users.child(Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail()));

        searchUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCurrent = dataSnapshot.getValue(User.class);

                if(userCurrent != null){
                    textViewText2.setText(getResources().getString(R.string.home_presentation_dec) + " " + userCurrent.getName() + ".");
                }else{
                    textViewText2.setVisibility(View.INVISIBLE);
                    textViewText1.setText(getResources().getString(R.string.error));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void initComponent(View v){
        textViewText2 = v.findViewById(R.id.textViewFragHomeText2);
    }

}
