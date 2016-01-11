package com.example.billyshen.jaiye;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by billyshen on 10/01/2016.
 */
public class AudioPlayerActivity extends AppCompatActivity {

    final MediaPlayer mp = new MediaPlayer();
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

        // Init MediaPlayer
        initMediaPlayer();

    }


    private void initMediaPlayer() {

        String songUrl = "http://programmerguru.com/android-tutorial/wp-content/uploads/2013/04/hosannatelugu.mp3";
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
        /* mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        }); */


    }
}
