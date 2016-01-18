package com.example.billyshen.jaiye.models;

/**
 * Created by billyshen on 18/01/2016.
 */
public class Picture {

    private String id;
    private String name;

    public Picture(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
