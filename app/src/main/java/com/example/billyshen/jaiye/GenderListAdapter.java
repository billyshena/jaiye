package com.example.billyshen.jaiye;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.billyshen.jaiye.models.Author;
import com.example.billyshen.jaiye.models.Gender;
import com.example.billyshen.jaiye.models.Picture;
import com.example.billyshen.jaiye.models.Song;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by billyshen on 09/01/2016.
 */
class GenderListAdapter extends RecyclerView.Adapter<GenderViewHolder> implements View.OnClickListener {


    private final Context ctx;
    private LayoutInflater inflater;
    List<Gender> genders = Collections.emptyList();
    RecyclerView rv;
    Activity activity;


    public GenderListAdapter(Context context, List<Gender> genders, RecyclerView rv, Activity activity) {
        inflater = LayoutInflater.from(context);
        ctx = context;
        this.genders = genders;
        this.rv = rv;
        this.activity = activity;
    }

    @Override
    public GenderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = inflater.inflate(R.layout.gender_row, parent, false);
        GenderViewHolder holder = new GenderViewHolder(view);

        // set recyclerview click listener
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(GenderViewHolder holder, int position) {

        Gender gender = genders.get(position);
        holder.genderTitle.setText(gender.getName().toUpperCase());

        // Set Gender cover
        Ion
                .with(holder.genderImage)
                .placeholder(R.drawable.placeholder)
                .animateIn(R.layout.fade_in_animation)
                .load(ctx.getResources().getString(R.string.picture_url) + "/" + gender.getPicture().getName());

    }

    @Override
    public int getItemCount() {
        return genders.size();
    }

    @Override
    public void onClick(View v) {

        int itemPosition = rv.getChildAdapterPosition(v);
        Gender gender = genders.get(itemPosition);
        String api_url = ctx.getResources().getString(R.string.api_url) + "/items/random/" + gender.getId();

        Log.d("clicked on", gender.getId());
        Ion.with(ctx.getApplicationContext())
                .load("GET", api_url)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (e != null) {
                            Log.d("error", e.toString());
                            return;
                        }

                        String[] fields = {"_id", "name", "title"};
                        JsonArray songs = result.getAsJsonArray("data");
                        List<Song> songsList = new ArrayList<Song>();

                                /* Initialize parameters */
                        Random rand = new Random();
                        int max = songs.size() - 1;
                        int min = 0;
                        int randomNum = 0;

                        try{
                            randomNum = rand.nextInt((max - min) + 1) + min;
                        }
                        catch(IllegalArgumentException ill) {
                            ill.printStackTrace();
                        }


                        for(int i = 0; i < songs.size(); i++) {

                            try {
                                JsonObject song_json = songs.get(i).getAsJsonObject();
                                JsonObject author_json = song_json.getAsJsonObject("author");
                                JsonObject picture_json = song_json.getAsJsonObject("cover");

                                Song song = new Song(
                                        song_json.get(fields[0]).getAsString(),
                                        new Author(
                                                author_json.get(fields[0]).getAsString(),
                                                author_json.get(fields[1]).getAsString()
                                        ),
                                        song_json.get(fields[2]).getAsString(),
                                        new Picture(
                                                picture_json.get(fields[0]).getAsString(),
                                                picture_json.get(fields[1]).getAsString()
                                        )

                                );

                                songsList.add(song);

                            } catch (IndexOutOfBoundsException ex) {
                                ex.printStackTrace();
                            }
                        }



                        try {
                            // Launch Audio Player Activity
                            Intent intent = new Intent(ctx.getApplicationContext(), AudioPlayerActivity.class);
                            Bundle b = new Bundle();
                            b.putParcelable("song", (Parcelable) songsList.get(randomNum));
                            b.putParcelableArrayList("songs", (ArrayList<? extends Parcelable>) songsList);
                            intent.putExtras(b);

                            activity.startActivity(intent);
                        }
                        catch(IndexOutOfBoundsException index) {
                            index.printStackTrace();
                        }

                    }


                });

    }
}
