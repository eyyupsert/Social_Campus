package com.example.socialcampusnew.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialcampusnew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassActivity extends AppCompatActivity {


    EditText forget_mail;
    Button pass_mail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);


        forget_mail = findViewById(R.id.forget_Email);
        pass_mail = findViewById(R.id.btn_send_mail);

         firebaseAuth = FirebaseAuth.getInstance();

    }


    public void sendEMail(View view){

        String emailAddress = forget_mail.getText().toString();

        firebaseAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(ForgetPassActivity.this, SignInActivity.class);
                            startActivity(intent);
                            Toast.makeText(ForgetPassActivity.this, "Send Mail".toString(), Toast.LENGTH_LONG);
                        }
                    }
                });


    }




}