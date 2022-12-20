package com.example.reflex_traing_device_3_0;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import com.example.reflex_traing_device_3_0.profile_operations.AddProfile;
import com.example.reflex_traing_device_3_0.profile_operations.DeleteProfile;
import com.example.reflex_traing_device_3_0.profile_operations.EditProfile;
import com.example.reflex_traing_device_3_0.profile_operations.SwitchProfile;
import com.google.android.material.navigation.NavigationView;

import soup.neumorphism.NeumorphCardView;

public class ProfileManager extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    NeumorphCardView addProfile;
    NeumorphCardView removeProfile;
    NeumorphCardView updateProfile;
    NeumorphCardView switchProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_manager);

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
                                Utils.toast(ProfileManager.this, "Stats Selected");
                                break;
                            case R.id.info:
                                Utils.toast(ProfileManager.this, "Info Selected");
                                break;
                            case R.id.profileManager:
                                Utils.toast(ProfileManager.this, "You are here!");
                                break;
                            case R.id.achievements:
                                Utils.toast(ProfileManager.this, "Achievements Selected");
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

        addProfile = findViewById(R.id.addProfile);
        removeProfile = findViewById(R.id.removeProfile);
        updateProfile = findViewById(R.id.updateProfile);
        switchProfile = findViewById(R.id.switchProfile);

        addProfile.setOnClickListener(this);
        removeProfile.setOnClickListener(this);
        updateProfile.setOnClickListener(this);
        switchProfile.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        Intent switchActivityIntent;
        switch (v.getId()) {
            case R.id.addProfile:
                switchActivityIntent = new Intent(this, AddProfile.class);
                startActivity(switchActivityIntent);
                break;
            case R.id.removeProfile:
                switchActivityIntent = new Intent(this, DeleteProfile.class);
                startActivity(switchActivityIntent);
                break;
            case R.id.updateProfile:
                switchActivityIntent = new Intent(this, EditProfile.class);
                startActivity(switchActivityIntent);
                break;
            case R.id.switchProfile:
                switchActivityIntent = new Intent(this, SwitchProfile.class);
                startActivity(switchActivityIntent);
                break;
            default:
                break;
        }
    }
}