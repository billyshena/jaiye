package com.example.billyshen.jaiye;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by billyshen on 10/01/2016.
 */
public class AudioPlayerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.audio_player_main);

        // Retrieve intent extra data
        Bundle extras = getIntent().getExtras();
        String gender;

        if (extras != null) {
            gender = extras.getString("gender");
        }

    }
}
