package com.example.billyshen.jaiye.models;

/**
 * Created by billyshen on 18/01/2016.
 */
public class Song {

    private String id;
    private Author author;
    private String title;

    public Song(String id, Author author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
