package com.example.reflex_traing_device_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Rymo Technologies");
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
                return true;
            case R.id.menu_reflex:
                toReflexTrainingActivity();
                return true;
            case R.id.menu_strength:
                toStrengthTrainingActivity();
                return true;
            case R.id.bluetooth:
                toBleActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void toReflexTrainingActivity() {
        Intent switchActivityIntent = new Intent(this, ReflexTraining.class);
        startActivity(switchActivityIntent);
    }

    public void toStrengthTrainingActivity() {
        Intent switchActivityIntent = new Intent(this, StrengthTraining.class);
        startActivity(switchActivityIntent);
    }

    public void toBleActivity() {
        Intent switchActivityIntent = new Intent(this, Bluetooth.class);
        startActivity(switchActivityIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

}