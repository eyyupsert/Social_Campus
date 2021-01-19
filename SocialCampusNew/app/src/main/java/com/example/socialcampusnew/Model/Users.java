package com.example.socialcampusnew.Model;

public class Users {

    private String userId;
    private String userFullName;
    private String userBranch;
    private String userEmail;
    private String userUniversity;
    private String userPhotoUrl;

    public Users(String userId, String userFullName, String userBranch, String userEmail, String userUniversity, String userPhotoUrl) {
        this.userId = userId;
        this.userFullName = userFullName;
        this.userBranch = userBranch;
        this.userEmail = userEmail;
        this.userUniversity = userUniversity;
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUserBranch() {
        return userBranch;
    }

    public void setUserBranch(String userBranch) {
        this.userBranch = userBranch;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserUniversity() {
        return userUniversity;
    }

    public void setUserUniversity(String userUniversity) {
        this.userUniversity = userUniversity;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }
}
