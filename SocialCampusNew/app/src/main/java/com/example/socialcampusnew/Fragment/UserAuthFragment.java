package com.example.socialcampusnew.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.socialcampusnew.Activity.MainPageActivity;
import com.example.socialcampusnew.Activity.SignInActivity;
import com.example.socialcampusnew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


public class UserAuthFragment extends Fragment {


    private FirebaseUser firebaseUser;
    private FirebaseAuth  firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    ProgressDialog pd;

    EditText email,pass;
    Button btn_email,btn_pass,btn_delete;
    public UserAuthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_auth, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        email = view.findViewById(R.id.update_Email);
        pass = view.findViewById(R.id.update_password);
        btn_email = view.findViewById(R.id.update_email_btn);
        btn_pass = view.findViewById(R.id.update_password_btn);
        btn_delete = view.findViewById(R.id.delete_btn);

        email.setText(firebaseUser.getEmail());

        btn_email_click();
        btn_pass_click();
        delete_user();

        return view;
    }


    public void btn_email_click(){

        btn_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newEmail = email.getText().toString();

                firebaseUser.updateEmail(newEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"Email change is successful",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
    public void btn_pass_click(){

        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String newPass = pass.getText().toString();

                firebaseUser.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getActivity(),"Password change is successful",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }


    public void delete_user(){

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Social Campus");
                builder.setMessage("Do you really want to delete your account?");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        firebaseUser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


                    }
                });
                builder.show();

            }
        });

    }
}