package com.example.billyshen.jaiye;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.billyshen.jaiye.models.Author;
import com.example.billyshen.jaiye.models.Gender;
import com.example.billyshen.jaiye.models.Picture;
import com.example.billyshen.jaiye.models.Song;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private List<Gender> genders = new ArrayList<Gender>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Genders
        initGenders();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    /* Init genders through HTTP request */
    private void initGenders() {

        /* Initialize request parameters */
        String api_url = getResources().getString(R.string.api_url) + "/genres/find";
        JsonObject json = new JsonObject();
        json.addProperty("populate", "photo");


        Log.d("initGenders", "true");
        /* GET all genders */
        Ion.with(getApplicationContext())
                .load("POST", api_url)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        if (e != null) {
                            Log.d("error", e.toString());
                            return;
                        }

                        Log.d("result", result.toString());
                        populateGenders(result.getAsJsonArray("data"));
                        populateList();

                    }
                });
    }

    /* Populate genders ArrayList */
    private void populateGenders(JsonArray data) {

        String[] fields = { "photo", "_id", "name" };

        for (int i = 0; i < data.size(); i++) {

            // Parse result into readable JSON
            JsonObject gender_json = data.get(i).getAsJsonObject();
            JsonObject picture_json = gender_json.getAsJsonObject(fields[0]);

            // Creating Gender object
            Gender gender = new Gender(
                    gender_json.get(fields[1]).getAsString(),
                    new Picture(
                            picture_json.get(fields[1]).getAsString(),
                            picture_json.get(fields[2]).getAsString()
                    ),
                    gender_json.get(fields[2]).getAsString()
            );

            // Add Gender result to the list
            genders.add(gender);
        }


    }


    private void populateList() {

        ArrayAdapter<Gender> adapter = new GenderListAdapter(MainActivity.this, (ArrayList<Gender>) genders);
        ListView list = (ListView) findViewById(R.id.lv_genders);
        list.setDivider(null);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Gender gender = genders.get(position);
                String api_url = getResources().getString(R.string.api_url) + "/items/random/" + gender.getId();


                /* GET Random Song */
                Ion.with(getApplicationContext())
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

                                /* Initialize parameters */
                                Random rand = new Random();
                                int max = songs.size();
                                int min = 0;
                                int randomNum = rand.nextInt((max - min) + 1) + min;
                                JsonObject song_json = songs.get(randomNum).getAsJsonObject();
                                JsonObject author_json = song_json.getAsJsonObject("author");

                                Song song = new Song(
                                        song_json.get(fields[0]).getAsString(),
                                        new Author(
                                                author_json.get(fields[0]).getAsString(),
                                                author_json.get(fields[1]).getAsString()
                                        ),
                                        song_json.get(fields[2]).getAsString()
                                );

                                // Launch Audio Player Activity
                                Intent intent = new Intent(getBaseContext(), AudioPlayerActivity.class);
                                Bundle b = new Bundle();
                                b.putParcelable("song", (Parcelable) song);
                                intent.putExtras(b);

                                startActivity(intent);

                            }
                        });


            }
        });
    }







}
