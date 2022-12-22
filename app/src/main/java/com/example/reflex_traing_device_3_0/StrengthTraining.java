package com.example.reflex_traing_device_3_0;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.google.android.material.navigation.NavigationView;

public class StrengthTraining extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private ProgressBar progressBar2;
    private int progressStatus2 = 0;
    private TextView textView2;
    private int tests = 0;
    private int maxForce = 0;
    private boolean running = false;
    private Handler handler = new Handler();
    ValuesDatabaseHelper DB;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_training);

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
                                Utils.toast(StrengthTraining.this, "Stats Selected");
                                break;
                            case R.id.info:
                                Utils.toast(StrengthTraining.this, "Info Selected");
                                break;
                            case R.id.achievements:
                                Utils.toast(StrengthTraining.this, "Achievements Selected");
                                break;
                            case R.id.menu_reflex:
                                toReflexTrainingActivity();
                                isActivitySwitch = true;
                                break;
                            case R.id.menu_strength:
                                Utils.toast(StrengthTraining.this, "You are here!");
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

        progressBar = (ProgressBar) findViewById(R.id.strPb);
        textView = findViewById(R.id.text_view_1);
        progressBar2 = (ProgressBar) findViewById(R.id.strPb2);
        textView2 = findViewById(R.id.text_view_2);

        DB = new ValuesDatabaseHelper(this);

        Button stActBtn = this.findViewById(R.id.startBtn);
        Button stopActBtn = this.findViewById(R.id.endBtn);

        stActBtn.setOnClickListener(this);
        stopActBtn.setOnClickListener(this);

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

    public void toBleActivity() {
        Intent switchActivityIntent = new Intent(this, Bluetooth.class);
        startActivity(switchActivityIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startBtn:
                if (Utils.getCONNECTION_STATUS(0) == 1) {
                    BleManager.getInstance().write(
                            Utils.getBleDevice(0),
                            Utils.getBluetoothGattService(),
                            Utils.getCharacteristicWrite(),
                            Utils.hexStringToBytes(Integer.toHexString(18)),
                            new BleWriteCallback() {
                                @Override
                                public void onWriteSuccess(int current, int total, byte[] justWrite) {

                                    Log.i("Write", "data written");
                                }

                                @Override
                                public void onWriteFailure(BleException exception) {
                                    Log.i("Write", exception.getDescription());
                                }
                            });
                }
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Utils.getCONNECTION_STATUS(1) == 1) {
                    BleManager.getInstance().write(
                            Utils.getBleDevice(1),
                            Utils.getBluetoothGattService(),
                            Utils.getCharacteristicWrite(),
                            Utils.hexStringToBytes(Integer.toHexString(18)),
                            new BleWriteCallback() {
                                @Override
                                public void onWriteSuccess(int current, int total, byte[] justWrite) {

                                    Log.i("Write", "data written");
                                }

                                @Override
                                public void onWriteFailure(BleException exception) {
                                    Log.i("Write", exception.getDescription());
                                }
                            });
                }
                running = true;
                updateProgressBars();
                break;
            case R.id.endBtn:
                running = false;
                try {
                    t.stop();
                } catch (Exception e) {
                    Log.i("StrengthActivity", "Done.");
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Utils.getCONNECTION_STATUS(0) == 1) {
                    BleManager.getInstance().write(
                            Utils.getBleDevice(0),
                            Utils.getBluetoothGattService(),
                            Utils.getCharacteristicWrite(),
                            Utils.hexStringToBytes(Integer.toHexString(19)),
                            new BleWriteCallback() {
                                @Override
                                public void onWriteSuccess(int current, int total, byte[] justWrite) {

                                }

                                @Override
                                public void onWriteFailure(BleException exception) {
                                    Log.i("Write", exception.getDescription());
                                }
                            });
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (Utils.getCONNECTION_STATUS(1) == 1) {
                    BleManager.getInstance().write(
                            Utils.getBleDevice(1),
                            Utils.getBluetoothGattService(),
                            Utils.getCharacteristicWrite(),
                            Utils.hexStringToBytes(Integer.toHexString(19)),
                            new BleWriteCallback() {
                                @Override
                                public void onWriteSuccess(int current, int total, byte[] justWrite) {

                                    Log.i("Write", "data written");
                                }

                                @Override
                                public void onWriteFailure(BleException exception) {
                                    Log.i("Write", exception.getDescription());
                                }
                            });
                }

                Boolean checkinsertdata = DB.insertData(2, 0, 0, 0.0F, 0, maxForce);
                if(checkinsertdata)
                    Toast.makeText(StrengthTraining.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(StrengthTraining.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
                maxForce = 0;
                break;
            default:
                break;

        }
    }

    private void updateProgressBars() {
        t = new Thread(new Runnable() {
            public void run() {
                while (running) {
                    if (Utils.getCONNECTION_STATUS(0) == 1) {
                        BleManager.getInstance().read(
                                Utils.getBleDevice(0),
                                Utils.getBluetoothGattService(),
                                Utils.getCharacteristicRead(),
                                new BleReadCallback() {
                                    @Override
                                    public void onReadSuccess(byte[] data) {
                                        String s = new String(data);
                                        Log.i("Read", s);
                                        if (!s.equals("")){
                                            try {
                                                progressStatus = (int) Float.parseFloat(s);
                                                if (progressStatus>maxForce)
                                                    maxForce = progressStatus;
                                            } catch (Exception e) {
                                                Log.i("onReadSuccess", e.toString());
                                                progressStatus = 0;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onReadFailure(BleException exception) {
                                        Log.i("Read", exception.getDescription());
                                    }
                                });
                    }
                    if (Utils.getCONNECTION_STATUS(1) == 1) {
                        BleManager.getInstance().read(
                                Utils.getBleDevice(1),
                                Utils.getBluetoothGattService(),
                                Utils.getCharacteristicRead(),
                                new BleReadCallback() {
                                    @Override
                                    public void onReadSuccess(byte[] data) {
                                        String s = new String(data);
                                        Log.i("Read", s);
                                        if (!s.equals("")) {
                                            try {
                                                progressStatus2 = (int) Float.parseFloat(s);
                                                if (progressStatus2>maxForce)
                                                    maxForce = progressStatus2;
                                            } catch (Exception e) {
                                                Log.i("onReadSuccess", e.toString());
                                                progressStatus2 = 0;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onReadFailure(BleException exception) {
                                        Log.i("Read", exception.getDescription());
                                    }
                                });
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(progressStatus+"/"+progressBar.getMax());
                            progressBar2.setProgress(progressStatus2);
                            textView2.setText(progressStatus2+"/"+progressBar2.getMax());
                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        t.start();
    }
}