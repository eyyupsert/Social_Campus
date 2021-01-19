package com.example.socialcampusnew.Fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.socialcampusnew.Adapter.BranchAdapter;
import com.example.socialcampusnew.Adapter.FeedRecyclerAdapter;
import com.example.socialcampusnew.Adapter.UniversityAdapter;
import com.example.socialcampusnew.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;


public class ContactFragment extends Fragment {

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String selectItemSpinner;

    ArrayList<String> userEmailFromDBBranch;
    ArrayList<String> userCommentFromDBBranch;
    ArrayList<String> userImageFromDBBranch;
    FeedRecyclerAdapter feedRecyclerAdapterBranch;
    ArrayList<String> userBranch;
    BranchAdapter branchAdapter;
    private Spinner branchSpinner;

    public ContactFragment() {
        // Required empty public constructor
    }
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_university, container, false);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userEmailFromDBBranch = new ArrayList<>();
        userCommentFromDBBranch = new ArrayList<>();
        userImageFromDBBranch = new ArrayList<>();
        userBranch = new ArrayList<>();
        branchSpinner = (Spinner) view.findViewById(R.id.spinnerUni);
        ArrayAdapter<String> myAdapterBranch = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Branch));
        myAdapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        branchSpinner.setAdapter(myAdapterBranch);

        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userImageFromDBBranch.clear();
                userCommentFromDBBranch.clear();
                userBranch.clear();
                userEmailFromDBBranch.clear();
                Object  item = parent.getItemAtPosition(position);
                selectItemSpinner = item.toString();
                System.out.println(selectItemSpinner);
                getDataFromFireStoreUni(selectItemSpinner);

            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewUni);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        branchAdapter = new BranchAdapter(userEmailFromDBBranch,userCommentFromDBBranch,userImageFromDBBranch);
        recyclerView.setAdapter(branchAdapter);


        return view;
    }

    public void getDataFromFireStoreUni(String secim){


        CollectionReference collectionReference = firebaseFirestore.collection("Posts");

        collectionReference.whereEqualTo("branch",secim).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                        String universityDB = (String) data.get("university");
                        userEmailFromDBBranch.add(userEmail);
                        userCommentFromDBBranch.add(comment);
                        userImageFromDBBranch.add(downloadUrl);
                        branchAdapter.notifyDataSetChanged();



                    }

                }


            }
        });

    }
}