package com.kaminski.gothan.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kaminski.gothan.R;
import com.kaminski.gothan.activity.RegisterActivity;
import com.kaminski.gothan.firebase.Firebase;
import com.kaminski.gothan.model.User;
import com.kaminski.gothan.util.Alert;
import com.kaminski.gothan.util.Base64Custom;
import com.kaminski.gothan.util.Validation;

public class ConfigurationFragment extends Fragment {

    private EditText editTextNome, editTextEmail, editTextPassord, editTextConfPassord;
    private Button buttonAlter;
    private User user;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_configuration, container, false);

        initComponent(v);
        initEvent();
        findDataUser();
        return v;
    }

    public void initComponent(View v){
        editTextNome = v.findViewById(R.id.editTextNewName);
        editTextEmail = v.findViewById(R.id.editTextNewEmail);
        editTextPassord = v.findViewById(R.id.editTextNewPassword);
        editTextConfPassord = v.findViewById(R.id.editTextNewPasswordConf);
        buttonAlter = v.findViewById(R.id.buttonAlterRegister);

        editTextEmail.setEnabled(false);
        editTextPassord.setEnabled(false);

    }
    public void initEvent(){

        buttonAlter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFild();
            }
        });
    }

    public void validateFild(){

        try{

            user = new User();

            // Name
            if(Validation.validateName(editTextNome.getText().toString())){
                user.setName(editTextNome.getText().toString());
            }else{
                throw new Exception(getResources().getString(R.string.ex_user_register_name));
            }

            // E-mail
            if(Validation.validateEmail(editTextEmail.getText().toString())){
                user.setEmail(editTextEmail.getText().toString());
            }else{
                throw new Exception(getResources().getString(R.string.ex_user_register_email));
            }

            // Password
            if(editTextEmail.getText().toString().isEmpty()){
                throw new Exception(getResources().getString(R.string.ex_user_register_password));
            }else if(editTextEmail.getText().toString().length() < 6){
                throw new Exception(getResources().getString(R.string.ex_user_register_password_size));
            }

            // Password Confirme
            if(editTextPassord.getText().toString().isEmpty()){
                throw new Exception(getResources().getString(R.string.ex_user_register_password_conf));
            }

            // Validate Password
            if(editTextPassord.getText().toString().equals(editTextConfPassord.getText().toString())){
                user.setPassword(editTextPassord.getText().toString());
            }else{
                throw new Exception(getResources().getString(R.string.ex_user_register_password_validate));
            }

            user.setId(Base64Custom.encodeBase64(user.getEmail()));
            user.setCpf("");
            user.setImgUrl("");
            user.setYearsOld(0);
            user.update(getResources(), getContext());

        }catch (Exception e){

            Alert.showAlert(
                    getActivity(),
                    getResources().getString(R.string.alert_error_update_title),
                    e.getMessage(),
                    getResources().getString(R.string.alert_neutral_button)
            );

        }
    }

    public void findDataUser(){

        firebaseAuth = Firebase.getFirebaseAuth();
        databaseReference = Firebase.getFirebase();

        final DatabaseReference users = databaseReference.child("users");
        DatabaseReference searchUser = users.child(Base64Custom.encodeBase64(firebaseAuth.getCurrentUser().getEmail()));

        searchUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);

                if (user != null) {
                    editTextNome.setText(user.getName());
                    editTextEmail.setText(user.getEmail());
                    editTextPassord.setText(user.getPassword());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

}
