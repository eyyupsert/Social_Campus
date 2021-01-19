package com.example.socialcampusnew.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialcampusnew.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;
import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private ArrayAdapter<String> myAdapterBranch;
    private ArrayAdapter<String> profile_myAdapter_uni;
    ProgressDialog pd;
    Uri imageData;


    EditText R_edit_email,R_edit_name_surname,R_edit_branch,R_edit_university,R_edit_password;
    Spinner R_spn_uni, R_spn_branch;
    TextView register_txt_uni,register_txt_branch;
    Button R_btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //firebase bağlantısı
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();


        R_edit_email = findViewById(R.id.register_Email);
        R_edit_name_surname = findViewById(R.id.register_name_surname);
        R_spn_uni = findViewById(R.id.register_spn_uni);
        R_spn_branch= findViewById(R.id.register_spn_branch);
        R_edit_password = findViewById(R.id.register_password);
        R_btn_register = findViewById(R.id.btn_register);
        register_txt_uni = findViewById(R.id.register_uni_txt);
        register_txt_branch = findViewById(R.id.register_branch_txt);

        setBranchList();
        setUniversityList();
        branchSpinnerSelect();
        uniSpinnerSelect();

    }


    public void sinUpClicked(View view){
            //firebase kayıt işlemleri
            pd = new ProgressDialog(SignUpActivity.this);
            pd.setMessage("Please Wait...");
            pd.show();
            String email = R_edit_email.getText().toString();
            String password = R_edit_password.getText().toString();

            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    String downloadUrl = "https://firebasestorage.googleapis.com/v0/b/socialcampus-db.appspot.com/o/images%2Fppstandart.jpg?alt=media&token=40fb77ab-b69b-446a-b429-fb9e95a6cdab";
                    String signup_email= R_edit_email.getText().toString();
                    String signup_namesurname= R_edit_name_surname.getText().toString();
                    String signup_university= register_txt_uni.getText().toString();
                    String signup_branch= register_txt_branch.getText().toString();
                    String veritabaniyolu = firebaseAuth.getCurrentUser().getUid();
                    String kullaniciYolu = firebaseAuth.getCurrentUser().getUid();


                    HashMap<String, Object> signUpData= new HashMap<>();
                    signUpData.put("userEmail", signup_email);
                    signUpData.put("userNameSurname", signup_namesurname);
                    signUpData.put("userUniversity", signup_university);
                    signUpData.put("userBranch", signup_branch);
                    signUpData.put("userImageUrl", downloadUrl);

                    firebaseFirestore.collection("Users").document(veritabaniyolu).set(signUpData).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
            });

    }

    public void setBranchList(){
        myAdapterBranch = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Branch));
        myAdapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        R_spn_branch.setAdapter(myAdapterBranch);
    }
    public void setUniversityList(){

        profile_myAdapter_uni = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.University));
        profile_myAdapter_uni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        R_spn_uni.setAdapter(profile_myAdapter_uni);
    }


    public void branchSpinnerSelect(){

        R_spn_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object  item = parent.getItemAtPosition(position);
                String selectItemBranch = item.toString();
                register_txt_branch.setText(selectItemBranch);
                System.out.println("branch: " + selectItemBranch);

            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    public void uniSpinnerSelect(){

        R_spn_uni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object  item = parent.getItemAtPosition(position);
                String selectItemUni = item.toString();
                register_txt_uni.setText(selectItemUni);
                System.out.println("uni: " + selectItemUni);

            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



}