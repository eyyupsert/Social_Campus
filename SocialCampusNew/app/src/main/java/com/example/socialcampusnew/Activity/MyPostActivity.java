package com.example.socialcampusnew.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialcampusnew.Adapter.MyPostAdapter;
import com.example.socialcampusnew.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

public class MyPostActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    ArrayList<String> userEmailFromDBMyPost;
    ArrayList<String> userCommentFromDBMyPost;
    ArrayList<String> userImageFromDBMyPost;
    ArrayList<String> userPostIdMyPost;
    ImageView pp;
    TextView email;
    Button signout;
    MyPostAdapter myPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        pp = findViewById(R.id.myPost_ProfilePhoto_top);
        email = findViewById(R.id.myPost_nameSurname_top);
        signout = findViewById(R.id.myPost_logOutButton);

        userEmailFromDBMyPost = new ArrayList<>();
        userCommentFromDBMyPost = new ArrayList<>();
        userImageFromDBMyPost = new ArrayList<>();
        userPostIdMyPost = new ArrayList<>();

        getUserInfo_Mypost();
        getDataFromFireStoreMyPost();
        RecyclerView recyclerView = findViewById(R.id.recyclerViewMyPost);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myPostAdapter = new MyPostAdapter(userEmailFromDBMyPost,userCommentFromDBMyPost,userImageFromDBMyPost,userPostIdMyPost);
        recyclerView.setAdapter(myPostAdapter);

    }

    public void getDataFromFireStoreMyPost(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String getUserEmail = firebaseUser.getEmail();
        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.whereEqualTo("userEmail",getUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if(error != null){
                    Toast.makeText(MyPostActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value != null){


                    for(DocumentSnapshot snapshot : value.getDocuments()){

                        Map<String,Object> data = snapshot.getData();
                        //Casting
                        String comment = (String) data.get("commentText");
                        String userEmail = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");
                        String postIdDB = (String) data.get("postId");
                        userEmailFromDBMyPost.add(userEmail);
                        userCommentFromDBMyPost.add(comment);
                        userImageFromDBMyPost.add(downloadUrl);
                        userPostIdMyPost.add(postIdDB);
                        myPostAdapter.notifyDataSetChanged();


                    }

                }


            }
        });

    }
    public void getUserInfo_Mypost(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String getUserEmail = firebaseUser.getEmail().toString();
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.whereEqualTo("userEmail",getUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(MyPostActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value != null){

                    for(DocumentSnapshot snapshot : value.getDocuments()){

                        Map<String,Object> data = snapshot.getData();
                        //Casting
                        String nameSurname_feed = (String) data.get("userNameSurname");
                        String downloadUrl_feed = (String) data.get("userImageUrl");

                        email.setText(nameSurname_feed);
                        Picasso.get().load(downloadUrl_feed).into(pp);

                    }

                }

            }
        });

    }
    public void signOutMypost(View view){

        try {
            firebaseAuth.signOut();
            Intent intent = new Intent(MyPostActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();

        }catch (Exception e){
            Toast.makeText(MyPostActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
        }


    }

}