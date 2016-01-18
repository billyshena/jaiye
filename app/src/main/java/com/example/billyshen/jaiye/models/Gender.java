package com.example.billyshen.jaiye.models;

/**
 * Created by billyshen on 09/01/2016.
 */
public class Gender {

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
}
