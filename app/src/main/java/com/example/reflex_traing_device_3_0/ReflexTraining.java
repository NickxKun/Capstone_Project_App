package com.example.reflex_traing_device_3_0;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ReflexTraining extends AppCompatActivity implements View.OnClickListener {


    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex_training);

        // BD -> Bass Drum
        Button playBD1 = (Button) this.findViewById(R.id.btnBD1);
        Button playBD2 = (Button) this.findViewById(R.id.btnBD2);
        Button playBD3 = (Button) this.findViewById(R.id.btnBD3);
        Button playBD4 = (Button) this.findViewById(R.id.btnBD4);
        Button playBD5 = (Button) this.findViewById(R.id.btnBD5);
        Button playBD6 = (Button) this.findViewById(R.id.btnBD6);

        playBD1.setOnClickListener(this);
        playBD2.setOnClickListener(this);
        playBD3.setOnClickListener(this);
        playBD4.setOnClickListener(this);
        playBD5.setOnClickListener(this);
        playBD6.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Reflex Training");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                toHomeActivity();
                return true;
            case R.id.menu_reflex:
                return true;
            case R.id.menu_strength:
                toStrengthTrainingActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void toHomeActivity() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    public void toStrengthTrainingActivity() {
        Intent switchActivityIntent = new Intent(this, StrengthTraining.class);
        startActivity(switchActivityIntent);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnBD1:
                mp = MediaPlayer.create(this, R.raw.bdl1a);
                break;
            case R.id.btnBD2:
                mp = MediaPlayer.create(this, R.raw.bdl2a);
                break;
            case R.id.btnBD3:
                mp = MediaPlayer.create(this, R.raw.bdl3a);
                break;
            case R.id.btnBD4:
                mp = MediaPlayer.create(this, R.raw.bdl4a);
                break;
            case R.id.btnBD5:
                mp = MediaPlayer.create(this, R.raw.bdl5a);
                break;
            case R.id.btnBD6:
                mp = MediaPlayer.create(this, R.raw.bdl6a);
                break;
            default:
                Toast.makeText(this, "Button Selection Failed", Toast.LENGTH_SHORT).show();

        }

        if (mp.isPlaying()) {
            mp.release();
        }

        mp.setOnPreparedListener(mediaPlayer -> mp.start());

        mp.setOnCompletionListener(mediaPlayer -> mp.release());
    }
}