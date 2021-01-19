package com.example.socialcampusnew.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialcampusnew.Activity.MainPageActivity;
import com.example.socialcampusnew.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    ArrayList<String> userImageFromDB;
    private String selectItemUni,selectItemBranch;
    private Spinner selectUni, selectBranch;
    private EditText P_edit_namesurname;
    private ImageView pp_image_profile;
    private Button P_btn_update,pp_select;
    private TextView P_uniText,P_branchText;
    private ArrayAdapter<String> myAdapterBranch;
    private ArrayAdapter<String> profile_myAdapter_uni;

    Uri imageData;
    Bitmap selectedImage;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userImageFromDB = new ArrayList<>();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();


        selectUni = (Spinner) view.findViewById(R.id.profile_spinner_Uni);
        P_uniText = (TextView) view.findViewById(R.id.profile_textView_uni);
        selectBranch = (Spinner) view.findViewById(R.id.profile_spinner_Branch);
        P_branchText = (TextView) view.findViewById(R.id.profile_textView_branch);
        pp_image_profile = (ImageView) view.findViewById(R.id.imageView_Profile);
        P_btn_update = (Button) view.findViewById(R.id.profile_Update);
        P_edit_namesurname = (EditText) view.findViewById(R.id.profile_name_surname);
        pp_select = (Button) view.findViewById(R.id.profile_photo_select);

        setBranchList();
        setUniversityList();
        getUserInfo_profile();
        selectImage_profile();
        data_update_firebase();





        return view;
    }


    public void setBranchList(){
        myAdapterBranch = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.Branch));
        myAdapterBranch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectBranch.setAdapter(myAdapterBranch);
    }
    public void setUniversityList(){

        profile_myAdapter_uni = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.University));
        profile_myAdapter_uni.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectUni.setAdapter(profile_myAdapter_uni);
    }


    public void branchSpinnerSelect(){

        selectBranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object  item = parent.getItemAtPosition(position);
                selectItemBranch = item.toString();
                P_branchText.setText(selectItemBranch);
                System.out.println("branch: " + selectItemBranch);

            }
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }



    public void uniSpinnerSelect(){

        selectUni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object  item = parent.getItemAtPosition(position);
                selectItemUni = item.toString();
                P_uniText.setText(selectItemUni);
                System.out.println("uni: " + selectItemUni);

            }
            public void onNothingSelected(AdapterView<?> parent) {
                
            }
        });

    }
    public void getUserInfo_profile(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String getUserEmail = firebaseUser.getEmail().toString();
        CollectionReference collectionReference = firebaseFirestore.collection("Users");
        collectionReference.whereEqualTo("userEmail",getUserEmail).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {


                if(error != null){
                    Toast.makeText(getActivity(),error.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                if(value != null){
                    uniSpinnerSelect();
                    branchSpinnerSelect();
                    for(DocumentSnapshot snapshot : value.getDocuments()){

                        Map<String,Object> data = snapshot.getData();
                        //Casting
                        String nameSurname_profile = (String) data.get("userNameSurname");
                        String downloadUrl_profile = (String) data.get("userImageUrl");
                        userImageFromDB.add(downloadUrl_profile);
                        String university_profile = (String) data.get("userUniversity");
                        String branch_profile = (String) data.get("userBranch");

                        P_edit_namesurname.setText(nameSurname_profile);
                        P_branchText.setText(branch_profile);
                        P_uniText.setText(university_profile);
                        selectUni.setSelection(profile_myAdapter_uni.getPosition(university_profile));
                        selectBranch.setSelection(myAdapterBranch.getPosition(branch_profile));
                        //System.out.println("uni profile: "+university_profile);
                        //System.out.println("adapter profile: "+profile_myAdapter_uni.getPosition(university_profile));
                        Picasso.get().load(downloadUrl_profile).into(pp_image_profile);

                    }

                }

            }
        });

    }


    public void selectImage_profile(){

        pp_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intentToGallery,2);
                }
            }
        });


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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageData= data.getData();
            try {
                if (Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    pp_image_profile.setImageBitmap(selectedImage);
                }else{
                    selectedImage = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),imageData);
                    pp_image_profile.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void data_update_firebase(){

        P_btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UUID uuid = UUID.randomUUID();
                String imagenameYeni = "images/" + uuid + ".jpg";
                storageReference.child(imagenameYeni).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        StorageReference databaseReferance = FirebaseStorage.getInstance().getReference(imagenameYeni);
                        databaseReferance.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String downloadUrl = uri.toString();
                                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                String userUid = firebaseUser.getUid();
                                String userEmail = firebaseUser.getEmail();
                                String nameSurname = P_edit_namesurname.getText().toString();
                                String branch = P_branchText.getText().toString();
                                String uni = P_uniText.getText().toString();
                                HashMap<String, Object> postData= new HashMap<>();
                                if(downloadUrl == (String) userImageFromDB.get(0)){
                                    postData.put("userNameSurname",nameSurname);
                                    postData.put("userUniversity",uni);
                                    postData.put("userBranch",branch);
                                    postData.put("userEmail", userEmail);
                                    postData.put("userImageUrl", userImageFromDB.get(0));
                                    Intent intent = new Intent(getContext(), MainPageActivity.class);
                                    startActivity(intent);
                                }else {
                                    try {
                                        postData.put("userNameSurname",nameSurname);
                                        postData.put("userUniversity",uni);
                                        postData.put("userBranch",branch);
                                        postData.put("userEmail", userEmail);
                                        postData.put("userImageUrl", downloadUrl);
                                    }catch (Exception e){

                                        Intent intent = new Intent(getContext(), MainPageActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                                    }
                                }



                                firebaseFirestore.collection("Users").document(userUid).set(postData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Intent intent = new Intent(getContext(), MainPageActivity.class);
                                        startActivity(intent);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

    }

}