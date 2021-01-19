package com.example.socialcampusnew.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialcampusnew.Adapter.FeedRecyclerAdapter;
import com.example.socialcampusnew.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

public class HomeFragment extends Fragment {
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth  firebaseAuth;


    ArrayList<String> userEmailFromDB;
    ArrayList<String> userCommentFromDB;
    ArrayList<String> userImageFromDB;
    FeedRecyclerAdapter feedRecyclerAdapter;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userEmailFromDB = new ArrayList<>();
        userCommentFromDB = new ArrayList<>();
        userImageFromDB = new ArrayList<>();

        getDataFromFireStore();

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewHomeFragment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        feedRecyclerAdapter = new FeedRecyclerAdapter(userEmailFromDB,userCommentFromDB,userImageFromDB);
        recyclerView.setAdapter(feedRecyclerAdapter);

        return view;
    }
    public void getDataFromFireStore(){
        String post = "Posts";
        CollectionReference collectionReference = firebaseFirestore.collection(post);
        collectionReference.orderBy("dateTime", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {



                if(error != null){
                    Toast.makeText(getActivity(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value != null){

                    for(DocumentSnapshot snapshot : value.getDocuments()){

                        Map<String,Object> data = snapshot.getData();
                        //Casting
                        String comment = (String) data.get("commentText");
                        String userEmail = (String) data.get("userEmail");
                        String downloadUrl = (String) data.get("downloadUrl");
                        userEmailFromDB.add(userEmail);
                        userCommentFromDB.add(comment);
                        userImageFromDB.add(downloadUrl);
                        feedRecyclerAdapter.notifyDataSetChanged();


                    }

                }


            }
        });

    }


}