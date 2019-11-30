package com.kaminski.gothan.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.kaminski.gothan.R;
import com.kaminski.gothan.firebase.Firebase;
import com.kaminski.gothan.model.User;
import com.kaminski.gothan.util.Alert;
import com.kaminski.gothan.util.Validation;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassaword;
    private Button buttonLogin;

    private FirebaseAuth firebaseAuth;
    private User user;

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponent();
        initEvent();
    }

    public void initComponent(){
        buttonLogin = findViewById(R.id.buttonLoginAcess);
        editTextEmail = findViewById(R.id.editTextLoginEmail);
        editTextPassaword = findViewById(R.id.editTextLoginPassaword);
    }

    public void initEvent(){

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFild();
            }
        });
    }

    public void validateFild(){

        try {

            user = new User();

            // Email
            if(Validation.validateEmail(editTextEmail.getText().toString())){
                user.setEmail(editTextEmail.getText().toString());
            }else{
                throw new Exception(getResources().getString(R.string.ex_user_register_email));
            }

            // Password
            if(editTextPassaword.getText().toString().isEmpty()){
                throw new Exception(getResources().getString(R.string.ex_user_register_password));
            }else if(editTextPassaword.getText().toString().length() < 6){
                throw new Exception(getResources().getString(R.string.ex_user_register_password_size));
            }

            user.setPassword(editTextPassaword.getText().toString());

            authenticateUser(user);

        }catch (Exception e){

            AlertDialog.Builder msg = new AlertDialog.Builder(LoginActivity.this);
            msg.setTitle(getResources().getString(R.string.alert_error_login_title));
            msg.setMessage(e.getMessage());
            msg.setNeutralButton(getResources().getString(R.string.alert_neutral_button), null);
            msg.show();

        }
    }

    public void authenticateUser(User user){

        firebaseAuth = Firebase.getFirebaseAuth();

        firebaseAuth.signInWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                }else{

                    try{

                        throw task.getException();

                    }catch (FirebaseAuthInvalidUserException e){

                        Alert.showAlert(
                                LoginActivity.this,
                                getResources().getString(R.string.alert_error_login_title),
                                getResources().getString(R.string.ex_login_access),
                                getResources().getString(R.string.alert_neutral_button)
                        );

                    }catch (FirebaseAuthInvalidCredentialsException e){

                        Alert.showAlert(
                                LoginActivity.this,
                                getResources().getString(R.string.alert_error_login_title),
                                getResources().getString(R.string.ex_login_auth),
                                getResources().getString(R.string.alert_neutral_button)
                        );

                    }catch (Exception e){

                        Alert.showAlert(
                                LoginActivity.this,
                                getResources().getString(R.string.alert_error_login_title),
                                getResources().getString(R.string.ex_login),
                                getResources().getString(R.string.alert_neutral_button)
                        );

                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
