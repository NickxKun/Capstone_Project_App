package com.example.reflex_traing_device_3_0;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.broooapps.graphview.CurveGraphConfig;
import com.broooapps.graphview.CurveGraphView;
import com.broooapps.graphview.models.GraphData;
import com.broooapps.graphview.models.PointMap;
import com.example.reflex_traing_device_3_0.profile_operations.AddProfile;
import com.google.android.material.navigation.NavigationView;

import soup.neumorphism.NeumorphCardView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    ScrollView details;
    RelativeLayout addProfileToProceed;
    CurveGraphView curveGraphView1;
    CurveGraphView curveGraphView2;
    PatientsDatabaseHelper DB;
    ImageButton addProfile;
    TextView welcomeText;
    TextView headerTitle;
    NeumorphCardView reflexCardView, strengthCardView;

    ValuesDatabaseHelper statsDB;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                                Utils.toast(MainActivity.this, "Home Selected");
                                break;
                            case R.id.bluetooth:
                                toBleActivity();
                                isActivitySwitch = true;
                                break;
                            case R.id.stats:
                                Utils.toast(MainActivity.this, "Stats Selected");
                                break;
                            case R.id.info:
                                Utils.toast(MainActivity.this, "Info Selected");
                                break;
                            case R.id.profileManager:
                                toProfileManager();
                                isActivitySwitch = true;
                                break;
                            case R.id.achievements:
                                Utils.toast(MainActivity.this, "Achievements Selected");
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
//                        if (isActivitySwitch)
//                            finish();
                    }
                });
                return false;
            }
        });
        navigationView.bringToFront();

        details =  findViewById(R.id.details);
        addProfileToProceed = findViewById(R.id.addProfileToProceed);
        DB = new PatientsDatabaseHelper(this);
        statsDB = new ValuesDatabaseHelper(this);
        if (DB.getdata().getCount()==0) {
            details.setVisibility(View.INVISIBLE);
            addProfileToProceed.setVisibility(View.VISIBLE);

            addProfile = findViewById(R.id.addProfile);
            addProfile.setOnClickListener(this);
        }
        else {
            addProfileToProceed.setVisibility(View.INVISIBLE);
            details.setVisibility(View.VISIBLE);

            welcomeText = findViewById(R.id.welcomeMessage1);
            welcomeText.setText("Welcome " + Utils.getActivePatientName(this));
            View inflatedView = getLayoutInflater().inflate(R.layout.header, null);
            headerTitle = (TextView) inflatedView.findViewById(R.id.header_txt);
            headerTitle.setText(Utils.getActivePatientName(this));
        }

        reflexCardView = findViewById(R.id.reflex_btn);
        strengthCardView = findViewById(R.id.strength_btn);

        reflexCardView.setOnClickListener(this);
        strengthCardView.setOnClickListener(this);

        curveGraphView1 = findViewById(R.id.cgv);
        updateCurveGraph(this, 1, curveGraphView1, 2000, 1);
        curveGraphView2 = findViewById(R.id.cgv2);
        updateCurveGraph(this, 2, curveGraphView2, 10000, 5);

    }

    private void updateCurveGraph(MainActivity mainActivity, int activity, CurveGraphView curveGraphView, int maxVal, int colIndex) {
        curveGraphView.configure(
                new CurveGraphConfig.Builder(mainActivity)
                        .setAxisColor(R.color.axis_color)                                       // Set number of values to be displayed in X ax
                        .setVerticalGuideline(4)                                                // Set number of background guidelines to be shown.
                        .setHorizontalGuideline(2)
                        .setGuidelineColor(R.color.grey_200)                                    // Set color of the visible guidelines.
                        .setNoDataMsg("No Training Data")                                       // Message when no data is provided to the view.
                        .setxAxisScaleTextColor(R.color.white)                                  // Set X axis scale text color.
                        .setyAxisScaleTextColor(R.color.white)                                  // Set Y axis scale text color
                        .setAnimationDuration(2000)                                             // Set Animation Duration
                        .build()
        );
        PointMap pointMap = new PointMap();
        Cursor statsCursor = statsDB.getData(activity);
        if (statsCursor.getCount()>0) {
            for (int i=0; statsCursor.moveToNext(); i++){
                pointMap.addPoint(i, statsCursor.getInt(colIndex));
            }

            GraphData gd = GraphData.builder(mainActivity)
                    .setPointMap(pointMap)
                    .setGraphStroke(R.color.graph_start_color)
                    .setGraphGradient(R.color.graph_start_color, R.color.graph_end_color)
                    .build();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    curveGraphView.setData(statsCursor.getCount(), maxVal, gd);
                }
            }, 250);
        } else {
            curveGraphView.setVisibility(View.INVISIBLE);
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

    public void toProfileManager() {
        Intent switchActivityIntent = new Intent(this, ProfileManager.class);
        startActivity(switchActivityIntent);
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

    @Override
    public void onClick(View view) {
        Intent switchActivityIntent;
        switch (view.getId()) {
            case R.id.addProfile:
                switchActivityIntent = new Intent(this, AddProfile.class);
                startActivity(switchActivityIntent);
                break;
            case R.id.reflex_btn:
                switchActivityIntent = new Intent(this, ReflexTraining.class);
                startActivity(switchActivityIntent);
                break;
            case R.id.strength_btn:
                switchActivityIntent = new Intent(this, StrengthTraining.class);
                startActivity(switchActivityIntent);
                break;
            default:
                break;
        }
//            case R.id.btnUpdate:
//                Boolean checkupdatedata = DB.updateuserdata(nameTXT, contactTXT, dobTXT, genderTXT, diagnosisTXT);
//                if(checkupdatedata==true)
//                    Toast.makeText(MainActivity.this, "Entry Updated", Toast.LENGTH_SHORT).show();
//                else
//                    Toast.makeText(MainActivity.this, "New Entry Not Updated", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.btnView:
//                Cursor res = DB.getdata();
//                if(res.getCount()==0){
//                    Toast.makeText(MainActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                StringBuffer buffer = new StringBuffer();
//                while(res.moveToNext()){
//                    buffer.append("Name :"+res.getString(0)+"\n");
//                    buffer.append("Contact :"+res.getString(1)+"\n");
//                    buffer.append("Date of Birth :"+res.getString(2)+"\n\n");
//                    buffer.append("Gender :"+res.getString(3)+"\n\n");
//                    buffer.append("Diagnosis :"+res.getString(4)+"\n\n");
//                }
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                builder.setCancelable(true);
//                builder.setTitle("User Entries");
//                builder.setMessage(buffer.toString());
//                builder.show();
//                break;
//            default:
//                break;
//        }
    }

}