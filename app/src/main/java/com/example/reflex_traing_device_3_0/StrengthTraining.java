package com.example.reflex_traing_device_3_0;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.example.reflex_traing_device_3_0.operation.OperationActivity;

public class StrengthTraining extends AppCompatActivity {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private int tests = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_training);

        progressBar = (ProgressBar) findViewById(R.id.strPb);
        textView = findViewById(R.id.text_view_1);

        new Thread(new Runnable() {
            public void run() {
                while (tests++ < 100) {
                    if (Utils.getCONNECTION_STATUS() == 1) {
                        BleManager.getInstance().read(
                                Utils.getBleDevice(),
                                Utils.getBluetoothGattService(),
                                Utils.getCharacteristicRead(),
                                new BleReadCallback() {
                                    @Override
                                    public void onReadSuccess(byte[] data) {
                                        String s = new String(data);
                                        Log.i("Read", s);
                                        progressStatus = (int) Float.parseFloat(s);
                                    }

                                    @Override
                                    public void onReadFailure(BleException exception) {
                                        Log.i("Read", exception.getDescription());
                                    }
                                });
                    }
                    else
                    {
                        Log.i("Strength Training", "No Devices Available");
                    }
                    handler.post(new Runnable() {
                        public void run() {
                            progressBar.setProgress(progressStatus);
                            textView.setText(progressStatus+"/"+progressBar.getMax());
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
        }).start();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Strength Training");
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
                toReflexTrainingActivity();
                return true;
            case R.id.menu_strength:
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
}