package com.example.billyshen.jaiye.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by billyshen on 09/01/2016.
 */
public class Gender implements Parcelable {

    private String id;
    private String name;
    private Picture picture;


    public Gender(String id, Picture picture, String name) {
        this.id = id;
        this.picture = picture;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPhoto(Picture picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Gender(Parcel in) {
        id = in.readString();
        name = in.readString();
        picture = (Picture) in.readValue(Picture.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeValue(picture);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Gender> CREATOR = new Parcelable.Creator<Gender>() {
        @Override
        public Gender createFromParcel(Parcel in) {
            return new Gender(in);
        }

        @Override
        public Gender[] newArray(int size) {
            return new Gender[size];
        }
    };
}