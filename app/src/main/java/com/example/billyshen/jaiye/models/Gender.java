package com.example.billyshen.jaiye.models;

/**
 * Created by billyshen on 09/01/2016.
 */
public class Gender {


    private String title;
    private String image;
    private String resourceUrl;

    public Gender(String title, String image, String resourceUrl) {
        this.title = title;
        this.image = image;
        this.resourceUrl = resourceUrl;
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

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }




}
