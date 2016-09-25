package com.example.paragon.socialapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListItem {
    private Long mId;
    private String mEmail;
    private String mName;
    private Bitmap mPhotoUrl;
    private Long mLike;
    private String mTechnique;

    public ListItem(Long id, String email, String name, Bitmap photourl, String mTechnique){
        this.mId = id;
        this.mEmail = email;
        this.mPhotoUrl = photourl;
        this.mName = name;
//      this.mLike = randomUserLike;
      this.mTechnique= mTechnique ;
    }

    public Long getID(){
        return mId;
    }

    public String getEmail(){
        return mEmail;
    }

    public String getName(){
        return mName;
    }

    public Long getLike(){
        return mLike;
    }
    public Bitmap getPhoto(){
        return mPhotoUrl;
    }
    public String getTechnique(){
        return mTechnique;
    }

}

