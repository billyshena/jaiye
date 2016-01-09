package com.example.billyshen.jaiye.models;

/**
 * Created by billyshen on 09/01/2016.
 */
public class Gender {


    private String title;
    private String image;

    public Gender(String title, String image) {
        this.title = title;
        this.image = image;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



}
