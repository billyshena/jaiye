package com.example.billyshen.jaiye;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by billyshen on 10/01/2016.
 */
public class AudioPlayerActivity extends AppCompatActivity {

    private MediaPlayer mp = new MediaPlayer();
    private MaterialPlayPauseButton materialPlayPauseButton;
    private SeekBar sb;
    private Thread updateSeekBar;
    private Boolean isPlaying = true;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Hide status bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player_main);

        // Retrieve intent extra data
        Bundle extras = getIntent().getExtras();
        String gender;

        if (extras != null) {
            gender = extras.getString("gender");
        }


        initPlayPause();
        initMediaPlayer();
        initSeekBar();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.release();
        mp = null;
    }


    private void initSeekBar() {

        sb = (SeekBar) findViewById(R.id.seekBar);

        updateSeekBar = new Thread(){

            @Override
            public void run() {

                int totalDuration = mp.getDuration();
                sb.setMax(totalDuration);



                while (currentPosition < totalDuration) {

                    try {
                        sleep(500);
                        if(isPlaying) {
                            currentPosition = mp.getCurrentPosition();
                            sb.setProgress(currentPosition);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }

        };


        // Set drag/drop events on SeekBar
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
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


                    isPlaying = false;
                    mp.pause();
                    materialPlayPauseButton.setToPlay();
                    Log.d("Stopping music", isPlaying + "");

                } else if (materialPlayPauseButton.getState() == MaterialPlayPauseButton.PLAY) {


                    Log.d("Playing again", "YES");
                    mp.seekTo(mp.getCurrentPosition());
                    isPlaying = true;
                    mp.start();
                    materialPlayPauseButton.setToPause();
                }

            }
        });
    }


    private void initMediaPlayer() {

        String songUrl = "http://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        Log.d("here", "here");
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

        Log.d("here2", "here2");

        // Starting player
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {

                Log.d("onPrepared", "YES");
                mp.start();
                materialPlayPauseButton.setToPause();
                updateSeekBar.start();
            }
        });


    }
}
