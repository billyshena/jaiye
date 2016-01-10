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

import com.example.billyshen.jaiye.models.Gender;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private List<Gender> genders = new ArrayList<Gender>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initGenders(); // Function to init genders list
        populateList();

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



    private void initGenders() {

        Log.i("okokokok", "okok");

        genders.add(new Gender("hip hop", "someImageHere", "gender1"));
        genders.add(new Gender("azonto", "someImageHere", "gender2"));
        genders.add(new Gender("coupe decale", "someImageHere", "gender3"));
        genders.add(new Gender("afro trap", "someImageHere", "gender4"));
        genders.add(new Gender("afro beats", "someImageHere", "gender5"));
    }

    private void populateList() {

        ArrayAdapter<Gender> adapter = new GenderListAdapter(MainActivity.this, (ArrayList<Gender>) genders);
        ListView list = (ListView) findViewById(R.id.lv_genders);
        list.setDivider(null);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Launch Audio Player Activity
                Intent intent = new Intent(getBaseContext(), AudioPlayerActivity.class);
                // TODO: Pass the selected Gender object
                intent.putExtra("gender", "ok");
                startActivity(intent);

            }
        });
    }







}
