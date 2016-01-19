package com.example.billyshen.jaiye;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.billyshen.jaiye.models.Author;
import com.example.billyshen.jaiye.models.Gender;
import com.example.billyshen.jaiye.models.Picture;
import com.example.billyshen.jaiye.models.Song;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private List<Gender> genders = new ArrayList<Gender>();
    private RecyclerView recyclerView;
    private GenderListAdapter adapter;

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

                        // Populate data
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


    // Populate data into custom RecyclerView
    private void populateList() {

        recyclerView = (RecyclerView) findViewById(R.id.lv_genders);
        adapter = new GenderListAdapter(getApplicationContext(), genders, recyclerView, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }







}
