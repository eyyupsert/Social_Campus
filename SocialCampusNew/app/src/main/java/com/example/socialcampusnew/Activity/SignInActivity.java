package com.example.socialcampusnew.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialcampusnew.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    EditText L_edit_maildAdress,L_edit_password;
    Button L_btn_giris;
    TextView L_txt_RegisterPage,L_txt_forgetPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        L_edit_maildAdress = findViewById(R.id.login_Email);
        L_edit_password = findViewById(R.id.login_Password);
        L_btn_giris = findViewById(R.id.btn_login);
        L_txt_RegisterPage = findViewById(R.id.login_register);
        L_txt_forgetPassword = findViewById(R.id.login_password_forget);

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser !=null){
            Intent intent = new Intent(SignInActivity.this, MainPageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }


    public void signInClicked(View view){


        String giris_email = L_edit_maildAdress.getText().toString();
        String giris_password = L_edit_password.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(giris_email,giris_password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Intent intent = new Intent(SignInActivity.this, MainPageActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(SignInActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

            }
        });

    }
    public void registerPageTrans(View view){

        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
    public void forgotPasswordTrans(View view){

        Intent intent = new Intent(SignInActivity.this, ForgetPassActivity.class);
        startActivity(intent);
    }

}