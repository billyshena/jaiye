package com.example.billyshen.jaiye.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by billyshen on 18/01/2016.
 */
public class Song implements Parcelable {

    private String id;
    private Author author;
    private String title;
    private Picture cover;

    public Song(String id, Author author, String title, Picture cover) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Picture getCover() {
        return cover;
    }

    public void setCover(Picture cover) {
        this.cover = cover;
    }

    protected Song(Parcel in) {
        id = in.readString();
        author = (Author) in.readValue(Author.class.getClassLoader());
        title = in.readString();
        cover = (Picture) in.readValue(Picture.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeValue(author);
        dest.writeString(title);
        dest.writeValue(cover);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };
}