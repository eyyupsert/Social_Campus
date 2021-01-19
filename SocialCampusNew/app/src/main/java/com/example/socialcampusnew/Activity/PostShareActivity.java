package com.example.socialcampusnew.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialcampusnew.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class
PostShareActivity extends AppCompatActivity {

    Bitmap selectedImage;
    ImageView postImage,post_ProfilePhoto;
    TextView post_nameSurname;
    EditText postText;
    Button share_btn,insert_btn;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth  firebaseAuth;
    public String universityDB;
    public String branchDB;
    Uri imageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_share);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        postImage = findViewById(R.id.insertImage);
        share_btn = findViewById(R.id.sharePost);
        insert_btn = findViewById(R.id.insertGallery);
        postText = findViewById(R.id.post_text);
        post_nameSurname = findViewById(R.id.post_nameSurname);
        post_ProfilePhoto = findViewById(R.id.post_ProfilePhoto);


        getUserInfo();
    }

    public void postShare(View view){


        UUID uuid = UUID.randomUUID();
        String imagename = "images/" + uuid + ".jpg";
        storageReference.child(imagename).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                StorageReference databaseReferance = FirebaseStorage.getInstance().getReference(imagename);
                databaseReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UUID newRandomNum = UUID.randomUUID();
                        String postID = newRandomNum.toString();
                        String downloadUrl = uri.toString();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        String userEmail = firebaseUser.getEmail();
                        String comment = postText.getText().toString();
                        HashMap<String, Object> postData= new HashMap<>();
                        try {
                            postData.put("userEmail", userEmail);
                            postData.put("downloadUrl", downloadUrl);
                            postData.put("commentText", comment);
                            postData.put("university",universityDB);
                            postData.put("branch",branchDB);
                            postData.put("postId",postID);

                            postData.put("dateTime", FieldValue.serverTimestamp());
                        }catch (Exception e){

                            Intent intent = new Intent(PostShareActivity.this, MainPageActivity.class);
                            startActivity(intent);
                            Toast.makeText(PostShareActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                        }
                        //olmazsa eklenecek
                        firebaseFirestore.collection("Posts").document(postID).set(postData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Intent intent = new Intent(PostShareActivity.this, MainPageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(PostShareActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostShareActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });

    }
    public void selectImage(View view){

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageData= data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    postImage.setImageBitmap(selectedImage);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    postImage.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getUserInfo(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String getUserEmail = firebaseUser.getEmail().toString();
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.whereEqualTo("userEmail",getUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(PostShareActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value != null){

                    for(DocumentSnapshot snapshot : value.getDocuments()){

                        Map<String,Object> data = snapshot.getData();
                        //Casting
                        String nameSurname = (String) data.get("userNameSurname");
                        String downloadUrl = (String) data.get("userImageUrl");
                        String uniDB = (String) data.get("userUniversity");
                        String braDB = (String) data.get("userBranch");
                        universityDB = uniDB;
                        branchDB = braDB;

                        post_nameSurname.setText(nameSurname);
                        Picasso.get().load(downloadUrl).into(post_ProfilePhoto);


                    }

                }

            }
        });

    }



}