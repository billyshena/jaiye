package com.example.billyshen.jaiye;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by billyshen on 10/01/2016.
 */
public class AudioPlayerActivity extends AppCompatActivity{

    private MediaPlayer mp = new MediaPlayer();
    private MaterialPlayPauseButton materialPlayPauseButton;
    private SeekBar sb;
    private Thread updateSeekBar;
    private Boolean isPlaying = true;
    private Boolean isDragging = false;
    private int currentPosition = 0;
    private SeekBar volumeSeekBar = null;
    private AudioManager audioManager = null;
    private TextView maxDuration;
    private TextView minDuration;
    private List<Song> songs = Collections.emptyList();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setContentView(R.layout.audio_player_main);

        Log.d("AudioPlayerActivity", "initialized");

        // Retrieve intent extra data
        Bundle extras = getIntent().getExtras();
        Song song = (Song) extras.getParcelable("song");
        Gender gender = (Gender) extras.getParcelable("gender");
        songs = extras.getParcelableArrayList("songs");

        initPlayPause();
        initMediaPlayer(song);
        initVolumeBar();
        initSongInfo(song, gender);


    }


    private void initSongInfo(Song param, Gender gender) {

        TextView songTitle = (TextView) findViewById(R.id.songTitle);
        ImageView songCover =  (ImageView) findViewById(R.id.songCover);

        songTitle.setText(gender.getName().toUpperCase());


        // Set Gender cover
        Ion.with(getApplicationContext())
                .load(getResources().getString(R.string.picture_url) + "/" + param.getCover().getName())
                .noCache()
                .withBitmap()
                .placeholder(R.drawable.placeholder)
                .intoImageView(songCover);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
        mp = null;
        isPlaying = false;
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


    private void initPlayPause() {

        // Init play pause button
        materialPlayPauseButton = (MaterialPlayPauseButton) findViewById(R.id.materialPlayPauseButton);

        materialPlayPauseButton.setColor(Color.WHITE);

        materialPlayPauseButton.setAnimDuration(300);

        materialPlayPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (materialPlayPauseButton.getState() == MaterialPlayPauseButton.PAUSE) {

                    isPlaying = false;
                    mp.pause();
                    materialPlayPauseButton.setToPlay();

                } else if (materialPlayPauseButton.getState() == MaterialPlayPauseButton.PLAY) {

                    mp.seekTo(mp.getCurrentPosition());
                    isPlaying = true;
                    mp.start();
                    materialPlayPauseButton.setToPause();
                }

            }
        });
    }


    private void initMediaPlayer(Song param) {

        String songUrl = getResources().getString(R.string.song_url) + '/' + param.getId() + getResources().getString(R.string.song_extension);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        // Setting DataSource
        try {
            mp.setDataSource(songUrl);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
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



}
