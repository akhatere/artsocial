package com.example.paragon.socialapp;

import android.app.Activity;
import android.os.Bundle;

public class UserInfo {

    private String mId;
    private String mEmail;
    private String mName;
    private String mDescription;
    private String mPhoto;

    private UserInfo(String email, String name){
        this.mEmail = email;
        this.mName = name;

    }

    public String getID(){
        return mId;
    }

    public String getEmail(){
        return mEmail;
    }

    public String getName(){
        return mName;
    }

    public String getDescription(){
        return mDescription;
    }

    public String getPhoto(){
        return mPhoto;
    }


}
