package com.kaminski.gothan.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.kaminski.gothan.R;
import com.kaminski.gothan.firebase.Firebase;
import com.kaminski.gothan.model.User;
import com.kaminski.gothan.util.Alert;
import com.kaminski.gothan.util.Base64Custom;
import com.kaminski.gothan.util.Validation;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextPassword, editTextConfPassord;
    private Button buttonRegister;
    private User user;

    private FirebaseAuth firebaseAuth ;

    @Override
    protected void onStart() {
        super.onStart();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kaminski.gothan.R.layout.activity_register);

        initComponent();
        initEvent();
    }

    public void initComponent(){
        editTextName = findViewById(R.id.editTextRegisterName);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegisterUser);
        editTextEmail = findViewById(R.id.editTextRegisterEmail);
        editTextConfPassord = findViewById(R.id.editTextRegisterConfPassword);
    }

    public void initEvent(){

        buttonRegister.setOnClickListener(new View.OnClickListener() {
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
            if(Validation.validateName(editTextName.getText().toString())){
                user.setName(editTextName.getText().toString());
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
            if(editTextPassword.getText().toString().isEmpty()){
                throw new Exception(getResources().getString(R.string.ex_user_register_password));
            }else if(editTextPassword.getText().toString().length() < 6){
                throw new Exception(getResources().getString(R.string.ex_user_register_password_size));
            }

            // Password Confirme
            if(editTextConfPassord.getText().toString().isEmpty()){
                throw new Exception(getResources().getString(R.string.ex_user_register_password_conf));
            }

            // Validate Password
            if(editTextPassword.getText().toString().equals(editTextConfPassord.getText().toString())){
                user.setPassword(editTextPassword.getText().toString());
            }else{
                throw new Exception(getResources().getString(R.string.ex_user_register_password_validate));
            }

            user.setId(Base64Custom.encodeBase64(user.getEmail()));
            user.setCpf("");
            user.setImgUrl("");
            user.setYearsOld(0);

            createUser();

        }catch (Exception e){

            Alert.showAlert(
                    RegisterActivity.this,
                    getResources().getString(R.string.alert_error_register_title),
                    e.getMessage(),
                    getResources().getString(R.string.alert_neutral_button)
            );

        }
    }

    public void createUser(){

        firebaseAuth = Firebase.getFirebaseAuth();

        firebaseAuth.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()

        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    user.register();

                    AlertDialog.Builder msg = new AlertDialog.Builder(RegisterActivity.this);
                    msg.setTitle(getResources().getString(R.string.alert_sucess_register_title));
                    msg.setMessage(getResources().getString(R.string.alert_sucess_register_dec));
                    msg.setNeutralButton(getResources().getString(R.string.alert_neutral_button), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    });
                    msg.show();

                }else{

                    try {

                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e){

                        Alert.showAlert(
                                RegisterActivity.this,
                                getResources().getString(R.string.alert_error_register_title),
                                getResources().getString(R.string.firebase_register_pass),
                                getResources().getString(R.string.alert_neutral_button)
                        );

                    }catch (FirebaseAuthInvalidCredentialsException e){

                        Alert.showAlert(
                                RegisterActivity.this,
                                getResources().getString(R.string.alert_error_register_title),
                                getResources().getString(R.string.firebase_register_email),
                                getResources().getString(R.string.alert_neutral_button)
                        );

                    } catch (FirebaseAuthUserCollisionException e){

                        Alert.showAlert(
                                RegisterActivity.this,
                                getResources().getString(R.string.alert_error_register_title),
                                getResources().getString(R.string.firebase_register_acount),
                                getResources().getString(R.string.alert_neutral_button)
                        );


                    }catch (Exception e) {
                        Alert.showAlert(
                                RegisterActivity.this,
                                getResources().getString(R.string.alert_error_register_title),
                                e.getMessage(),
                                getResources().getString(R.string.alert_neutral_button)
                        );
                    }
                }
            }
        });
    }

    public void clear(){
        editTextName.setText(null);
        editTextEmail.setText(null);
        editTextPassword.setText(null);
        editTextConfPassord.setText(null);
    }
}
