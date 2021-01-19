package com.example.socialcampusnew.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialcampusnew.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.UniPostHolder> {

    private ArrayList<String> userEmailListUni;
    private ArrayList<String> userCommentListUni;
    private ArrayList<String> userImageListUni;
    private ArrayList<String> userUniversity;
    private String SecilenItem ;


    public UniversityAdapter(ArrayList<String> userEmailListUniDB, ArrayList<String> userCommentListUniDB, ArrayList<String> userImageListUniDB,ArrayList<String> userUniversityDB,String secilenItemDB) {
        this.userEmailListUni = userEmailListUniDB;
        this.userCommentListUni = userCommentListUniDB;
        this.userImageListUni = userImageListUniDB;
        this.userUniversity = userUniversityDB;
        this.SecilenItem = secilenItemDB;
    }

    @NonNull
    @Override
    public UniPostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_row,parent,false);

        return new UniPostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniPostHolder holder, int position) {

        holder.name_surname.setText(userEmailListUni.get(position));
        holder.main_comment.setText(userCommentListUni.get(position));
        Picasso.get().load(userImageListUni.get(position)).into(holder.image_post);
        holder.comments.setText("");
        holder.like_count.setText("");


    }

    @Override
    public int getItemCount() {
        return userEmailListUni.size();
    }

    class UniPostHolder extends RecyclerView.ViewHolder{

        ImageView image_post;
        TextView main_comment;
        TextView comments;
        TextView like_count;
        TextView name_surname;

            public UniPostHolder(@NonNull View itemView) {
                super(itemView);
                image_post = itemView.findViewById(R.id.feed_post_image);
                main_comment = itemView.findViewById(R.id.feed_comment_main);
                comments = itemView.findViewById(R.id.feed_comment);
                like_count = itemView.findViewById(R.id.feed_like_count);
                name_surname = itemView.findViewById(R.id.feed_name_surname);
            }
        }


}
