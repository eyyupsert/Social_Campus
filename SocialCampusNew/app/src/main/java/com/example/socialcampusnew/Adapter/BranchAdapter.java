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

public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BranchHolder> {

    private ArrayList<String> userEmailListBranch;
    private ArrayList<String> userCommentListBranch;
    private ArrayList<String> userImageListBranch;

    public BranchAdapter(ArrayList<String> userEmailListUni, ArrayList<String> userCommentListUni, ArrayList<String> userImageListUni) {
        this.userEmailListBranch = userEmailListUni;
        this.userCommentListBranch = userCommentListUni;
        this.userImageListBranch = userImageListUni;
    }

    @NonNull
    @Override
    public BranchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);

        return new BranchHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull BranchHolder holder, int position) {

        holder.name_surname.setText(userEmailListBranch.get(position));
        holder.main_comment.setText(userCommentListBranch.get(position));
        Picasso.get().load(userImageListBranch.get(position)).into(holder.image_post);
        holder.comments.setText("");
        holder.like_count.setText("");
    }

    @Override
    public int getItemCount() {
        return userEmailListBranch.size();
    }

    class BranchHolder extends RecyclerView.ViewHolder{

        ImageView image_post;
        ImageView like_image;
        TextView main_comment;
        TextView comments;
        TextView like_count;
        TextView name_surname;
        public BranchHolder(@NonNull View itemView) {
            super(itemView);
            image_post = itemView.findViewById(R.id.feed_post_image);
            main_comment = itemView.findViewById(R.id.feed_comment_main);
            comments = itemView.findViewById(R.id.feed_comment);
            like_count = itemView.findViewById(R.id.feed_like_count);
            name_surname = itemView.findViewById(R.id.feed_name_surname);

        }
    }

}
