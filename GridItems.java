package com.example.paragon.socialapp;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class GridItems implements Parcelable {

    private Long mId;
    private String mEmail;
    private String mName;
    private Bitmap mPhotoUrl;
    private int mLike;
    private String mTechniques;
    private String mDescription;
    private String userEmail;
    private String userName;
    private String userDescription;

    public GridItems(Long id, String email, String name, Bitmap photourl, int like, String technique){
        this.mId = id;
        this.mEmail = email;
        this.mPhotoUrl = photourl;
        this.mName = name;
        mLike = like;
      this.mTechniques = technique;
    }

    public GridItems(Long id, String s, String name, Bitmap mBitmapPhoto, int like, String email, String description){
        userDescription = description;
        userName = name;
        userEmail = email;

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

    public Integer getLike(){
        return mLike;
    }
    public Bitmap getPhoto(){
        return mPhotoUrl;
    }
    public String getTechniques(){
        return mTechniques;
    }

    public String getmDescription() {
        return mDescription;
    }
    public GridItems(Parcel source) {
        readFromParcelExpense(source);
    }
    public void readFromParcelExpense(Parcel Source) {

         userEmail= Source.readString();
        userName = Source.readString();
        userDescription= Source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeLong(mId);
//        dest.writeString(mTechniqueOne);
        dest.writeString(userEmail);
        dest.writeString(userName);
        dest.writeString(userDescription);
    }

    public static final Creator<GridItems> CREATOR = new Creator<GridItems>() {

        @Override
        public GridItems createFromParcel(Parcel source) {
            return new GridItems(source);
        }

        @Override
        public GridItems[] newArray(int size) {
            return new GridItems[size];
        }

    };

}
