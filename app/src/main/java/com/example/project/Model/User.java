package com.example.project.Model;

import android.net.Uri;
import android.widget.ImageView;

public class User {
    public String email;
    public String profileImageUrl;
    public String username;
    public String uid;
    public String pushToken;
    public String job;
    public String comment;
    public boolean checkBox;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPushToken() {
        return pushToken;
    }

    public void setPushToken(String pushToken) {
        this.pushToken = pushToken;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Boolean chechBox) {
        this.checkBox = chechBox;
    }
}
