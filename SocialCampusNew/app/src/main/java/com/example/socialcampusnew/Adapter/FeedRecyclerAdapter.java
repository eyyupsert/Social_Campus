package com.example.socialcampusnew.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcampusnew.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FeedRecyclerAdapter extends RecyclerView.Adapter<FeedRecyclerAdapter.PostHolder> {

    private ArrayList<String> userEmailList;
    private ArrayList<String> userCommentList;
    private ArrayList<String> userImageList;



    public FeedRecyclerAdapter(ArrayList<String> userEmail, ArrayList<String> userCommentList, ArrayList<String> userImageList) {
        this.userEmailList = userEmail;
        this.userCommentList = userCommentList;
        this.userImageList = userImageList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);

        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println(position);

            }
        });

        holder.name_surname.setText(userEmailList.get(position));
        holder.main_comment.setText(userCommentList.get(position));
        Picasso.get().load(userImageList.get(position)).into(holder.image_post);
        holder.comments.setText("");
        holder.like_count.setText("");


    }

    @Override
    public int getItemCount() {
        return userEmailList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{
            ImageView image_post;
            ImageView like_image;
            TextView main_comment;
            TextView comments;
            TextView like_count;
            TextView name_surname;
        public PostHolder(@NonNull View itemView) {
            super(itemView);

            image_post = itemView.findViewById(R.id.feed_post_image);
            main_comment = itemView.findViewById(R.id.feed_comment_main);
            comments = itemView.findViewById(R.id.feed_comment);
            like_count = itemView.findViewById(R.id.feed_like_count);
            name_surname = itemView.findViewById(R.id.feed_name_surname);

        }
    }



}
