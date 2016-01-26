package com.example.billyshen.jaiye;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.billyshen.jaiye.models.Author;
import com.example.billyshen.jaiye.models.Gender;
import com.example.billyshen.jaiye.models.Picture;
import com.example.billyshen.jaiye.models.Song;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    private List<Gender> genders = new ArrayList<Gender>();
    private RecyclerView recyclerView;
    private GenderListAdapter adapter;
    private SlidingUpPanelLayout mLayout;
    private RelativeLayout miniPlayer;



    /* Media Player - TODO: Refractor into a Service to handle the AudioPlayer */

    ImageView backButton;
    static Context context;
    static MediaPlayer mp = new MediaPlayer();
    static MaterialPlayPauseButton materialPlayPauseButton;
    static SeekBar volumeSeekBar = null;
    static AudioManager audioManager = null;
    static TextView songTitle;
    static ImageView songCover;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;



        // Configure the Slider panel and hide it by default
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.getChildAt(1).setOnClickListener(null);
        mLayout.setPanelHeight(0);


        // Fetch UI Elements to setup song Info
        songTitle = (TextView) findViewById(R.id.songTitle);
        songCover = (ImageView) findViewById(R.id.songCover);
        backButton = (ImageView) findViewById(R.id.backButton);
        miniPlayer = (RelativeLayout) findViewById(R.id.miniPlayer);


        // Backbutton to minimize the Player
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("clicked", "yes");
                miniPlayer.setVisibility(View.VISIBLE);
                mLayout.setPanelHeight(100);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });


        // Expand full screen on miniPlayer click action
        miniPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miniPlayer.setVisibility(View.INVISIBLE);
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });


        // Listen to drag up event on the sliding panel
        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelCollapsed(View panel) {

                miniPlayer.setVisibility(View.VISIBLE);
                mLayout.setPanelHeight(100);

            }

            @Override
            public void onPanelExpanded(View panel) {
                miniPlayer.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onPanelAnchored(View panel) {

            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });

        // Initialize Genders
        initGenders();
        initPlayPause();
        initVolumeBar();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
        mp = null;
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
        adapter = new GenderListAdapter(getApplicationContext(), genders, recyclerView, this, mLayout);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


    // Initialize Audio Player
    public static void startAudioPlayer(Song song, Gender gender) {


        if(mp != null) {
            mp.release();
            mp = null;
        }

        mp = new MediaPlayer();

        /* Initilize play/pause button */
        String songUrl = context.getResources().getString(R.string.song_url) + '/' + song.getId() + context.getResources().getString(R.string.song_extension);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        songTitle.setText(gender.getName().toUpperCase());


        // Set Gender cover
        Ion.with(context.getApplicationContext())
                .load(context.getResources().getString(R.string.picture_url) + "/" + song.getCover().getName())
                .noCache()
                .withBitmap()
                .placeholder(R.drawable.placeholder)
                .intoImageView(songCover);


        // Setting DataSource
        try {
            mp.setDataSource(songUrl);
        } catch (IllegalArgumentException e) {
            Toast.makeText(context.getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(context.getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(context.getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Player playback
        mp.prepareAsync();


        // Starting player
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                materialPlayPauseButton.setToPause();
            }
        });



    }


    private void initPlayPause() {

        // Init play pause button
        materialPlayPauseButton = (MaterialPlayPauseButton) findViewById(R.id.materialPlayPauseButton);

        materialPlayPauseButton.setColor(Color.WHITE);

        materialPlayPauseButton.setAnimDuration(300);

        materialPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (materialPlayPauseButton.getState() == MaterialPlayPauseButton.PAUSE) {
                    mp.pause();
                    materialPlayPauseButton.setToPlay();

                } else if (materialPlayPauseButton.getState() == MaterialPlayPauseButton.PLAY) {

                    mp.seekTo(mp.getCurrentPosition());
                    mp.start();
                    materialPlayPauseButton.setToPause();
                }

            }
        });
    }



    private void initVolumeBar() {
        try
        {
            volumeSeekBar = (SeekBar)findViewById(R.id.volumeControl);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekBar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekBar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }






}
