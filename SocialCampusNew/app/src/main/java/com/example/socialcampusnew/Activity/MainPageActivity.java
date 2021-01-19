package com.example.socialcampusnew.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialcampusnew.Fragment.ContactFragment;
import com.example.socialcampusnew.Fragment.HomeFragment;
import com.example.socialcampusnew.Fragment.ProfileFragment;
import com.example.socialcampusnew.Fragment.UniversityFragment;
import com.example.socialcampusnew.Fragment.UserAuthFragment;
import com.example.socialcampusnew.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

import java.util.Map;

public class MainPageActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth  firebaseAuth;
    private Spinner spinner;
    Fragment chooseFrame = null;
    ImageView profileImageView;
    TextView userNameTxt;
    Button postShareButton, logOutButton,mypostPageButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        bottomNavigationView = findViewById(R.id.bottom_navbar);
        profileImageView = findViewById(R.id.main_ProfilePhoto_top);
        userNameTxt = findViewById(R.id.main_nameSurname_top);
        postShareButton = findViewById(R.id.main_share_button);
        logOutButton = findViewById(R.id.main_logOutButton);
        mypostPageButton = findViewById(R.id.main_UserPostPage);


        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getUserInfo_feed_main();

        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment,new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

             switch (item.getItemId()){

                 case R.id.nav_home:
                     //durumlar
                     chooseFrame = new HomeFragment();
                     break;
                 case R.id.nav_contact:
                     //durumlar
                     chooseFrame = new ContactFragment();
                     break;
                 case R.id.nav_uni:
                     //durumlar
                     chooseFrame = new UniversityFragment();
                     break;
                 case R.id.nav_profile:
                     //durumlar
                     SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                     editor.putString("profileId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                     editor.apply();
                     chooseFrame = new ProfileFragment();
                     break;
                 case R.id.nav_update:
                     //durumlar
                     chooseFrame = new UserAuthFragment();
                     break;

             }

             if (chooseFrame != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment,chooseFrame).commit();
             }
            return true;
        }
    };

    public void sharePageTransitionMain(View view){
        Intent intent = new Intent(MainPageActivity.this, PostShareActivity.class);
        startActivity(intent);

    }
    public void myPostPageTransition(View view){
        Intent intent = new Intent(MainPageActivity.this, MyPostActivity.class);
        startActivity(intent);
    }
    public void signOutFeedMain(View view){

        try {
            firebaseAuth.signOut();
            Intent intent = new Intent(MainPageActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();

        }catch (Exception e){
            Toast.makeText(MainPageActivity.this,e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
        }


    }
    public void getUserInfo_feed_main(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String getUserEmail = firebaseUser.getEmail().toString();
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.whereEqualTo("userEmail",getUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null){
                    Toast.makeText(MainPageActivity.this,error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value != null){

                    for(DocumentSnapshot snapshot : value.getDocuments()){

                        Map<String,Object> data = snapshot.getData();
                        //Casting
                        String nameSurname_feed = (String) data.get("userNameSurname");
                        String downloadUrl_feed = (String) data.get("userImageUrl");

                        userNameTxt.setText(nameSurname_feed);
                        Picasso.get().load(downloadUrl_feed).into(profileImageView);

                    }

                }

            }
        });

    }


}