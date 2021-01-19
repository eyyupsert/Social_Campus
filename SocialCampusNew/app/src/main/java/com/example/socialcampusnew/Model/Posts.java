package com.example.socialcampusnew.Model;

public class Posts {

    private String postiId;
    private String postPhotoUrl;
    private String postComment;
    private String postUserId;

    public Posts(String postiId, String postPhotoUrl, String postComment, String postUserId) {
        this.postiId = postiId;
        this.postPhotoUrl = postPhotoUrl;
        this.postComment = postComment;
        this.postUserId = postUserId;
    }

    public String getPostiId() {
        return postiId;
    }

    public void setPostiId(String postiId) {
        this.postiId = postiId;
    }

    public String getPostPhotoUrl() {
        return postPhotoUrl;
    }

    public void setPostPhotoUrl(String postPhotoUrl) {
        this.postPhotoUrl = postPhotoUrl;
    }

    public String getPostComment() {
        return postComment;
    }

    public void setPostComment(String postComment) {
        this.postComment = postComment;
    }

    public String getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(String postUserId) {
        this.postUserId = postUserId;
    }
}
