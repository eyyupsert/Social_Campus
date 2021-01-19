package com.example.socialcampusnew.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcampusnew.Activity.MainPageActivity;
import com.example.socialcampusnew.Activity.MyPostActivity;
import com.example.socialcampusnew.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.MyPostHolder> {

    private ArrayList<String> userEmailListMy;
    private ArrayList<String> userCommentListMy;
    private ArrayList<String> userImageListMy;
    private ArrayList<String> userPostIdMy;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;

    public MyPostAdapter(ArrayList<String> userEmailListMy, ArrayList<String> userCommentListMy, ArrayList<String> userImageListMy, ArrayList<String> userPostIdMy) {
        this.userEmailListMy = userEmailListMy;
        this.userCommentListMy = userCommentListMy;
        this.userImageListMy = userImageListMy;
        this.userPostIdMy = userPostIdMy;
    }

    @NonNull
    @Override
    public MyPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.my_user_post,parent,false);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        return new MyPostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPostHolder holder, int position) {
        holder.myPost_email.setText(userEmailListMy.get(position));
        holder.myPost_mainComment.setText(userCommentListMy.get(position));
        Picasso.get().load(userImageListMy.get(position)).into(holder.myPost_image);
        postDelete(userPostIdMy.get(position),holder.myPost_delete);
    }

    @Override
    public int getItemCount() { return userCommentListMy.size(); }

    class MyPostHolder extends RecyclerView.ViewHolder{

        TextView myPost_email, myPost_mainComment;
        ImageView myPost_image;
        Button myPost_delete;

        public MyPostHolder(@NonNull View itemView) {
            super(itemView);
            myPost_email = itemView.findViewById(R.id.mypost_Email);
            myPost_mainComment = itemView.findViewById(R.id.mypost_comment_main);
            myPost_image = itemView.findViewById(R.id.mypost_post_image);
            myPost_delete = itemView.findViewById(R.id.mypost_delete);


        }
    }

    public void postDelete(String id, Button deleteButton){

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseFirestore.collection("Posts").document(id).delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Intent intent = new Intent(view.getContext(), MainPageActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                view.getContext().startActivity(intent);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(view.getContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();

                    }
                });

            }
        });

    }

}
