package com.example.reflex_traing_device_3_0.profile_operations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.reflex_traing_device_3_0.Bluetooth;
import com.example.reflex_traing_device_3_0.MainActivity;
import com.example.reflex_traing_device_3_0.PatientsDatabaseHelper;
import com.example.reflex_traing_device_3_0.ProfileManager;
import com.example.reflex_traing_device_3_0.R;
import com.example.reflex_traing_device_3_0.ReflexTraining;
import com.example.reflex_traing_device_3_0.StrengthTraining;
import com.example.reflex_traing_device_3_0.Utils;
import com.google.android.material.navigation.NavigationView;

public class SwitchProfile extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    EditText name;
    Button switchProfile;
    PatientsDatabaseHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        boolean isActivitySwitch = false;
                        switch (item.getItemId())
                        {
                            case R.id.menu_home:
                                toHomeActivity();
                                isActivitySwitch = true;
                                break;
                            case R.id.bluetooth:
                                toBleActivity();
                                isActivitySwitch = true;
                                break;
                            case R.id.stats:
                                Utils.toast(SwitchProfile.this, "Stats Selected");
                                break;
                            case R.id.info:
                                Utils.toast(SwitchProfile.this, "Info Selected");
                                break;
                            case R.id.profileManager:
                                toProfileActivity();
                                break;
                            case R.id.achievements:
                                Utils.toast(SwitchProfile.this, "Achievements Selected");
                                break;
                            case R.id.menu_reflex:
                                toReflexTrainingActivity();
                                isActivitySwitch = true;
                                break;
                            case R.id.menu_strength:
                                toStrengthTrainingActivity();
                                isActivitySwitch = true;
                                break;
                        }
                        if (isActivitySwitch)
                            finish();
                    }
                });
                return false;
            }
        });
        navigationView.bringToFront();
        DB = new PatientsDatabaseHelper(this);
        name = findViewById(R.id.name);
        switchProfile = findViewById(R.id.switch_profile_btn);
        switchProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameTXT = name.getText().toString();
                if (DB.checkName(nameTXT)) {
                    Cursor res = DB.getActiveProfile();
                    if (res.getCount() != 0) {
                        while (res.moveToNext()) {
                            DB.setStatus(res.getString(0), "0");
                        }
                    }
                    DB.setStatus(nameTXT, "1");
                    Toast.makeText(SwitchProfile.this, "Switched Profile", Toast.LENGTH_SHORT).show();
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            toHomeActivity();
                            finish();
                        }
                    });
                }

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    public void toHomeActivity() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
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

    public void toProfileActivity() {
        Intent switchActivityIntent = new Intent(this, ProfileManager.class);
        startActivity(switchActivityIntent);
    }

}