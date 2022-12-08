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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;

public class StrengthTraining extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar progressBar;
    private int progressStatus = 0;
    private TextView textView;
    private ProgressBar progressBar2;
    private int progressStatus2 = 0;
    private TextView textView2;
    private int tests = 0;
    private boolean running = false;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strength_training);

        progressBar = (ProgressBar) findViewById(R.id.strPb);
        textView = findViewById(R.id.text_view_1);
        progressBar2 = (ProgressBar) findViewById(R.id.strPb2);
        textView2 = findViewById(R.id.text_view_2);

        Button stActBtn = this.findViewById(R.id.startBtn);
        Button stopActBtn = this.findViewById(R.id.endBtn);

        stActBtn.setOnClickListener(this);
        stopActBtn.setOnClickListener(this);



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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startBtn:
                if (Utils.getCONNECTION_STATUS() == 1) {
                    BleManager.getInstance().write(
                            Utils.getBleDevice(0),
                            Utils.getBluetoothGattService(),
                            Utils.getCharacteristicWrite(),
                            Utils.intToByteArray(18),
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
                if (Utils.getCONNECTION_STATUS() == 1) {
                    BleManager.getInstance().write(
                            Utils.getBleDevice(0),
                            Utils.getBluetoothGattService(),
                            Utils.getCharacteristicWrite(),
                            Utils.intToByteArray(19),
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
                running = false;
                break;
            default:
                break;

        }
    }

    private void updateProgressBars() {
        new Thread(new Runnable() {
            public void run() {
                while (running) {
                    if (Utils.getCONNECTION_STATUS() == 1) {
                        BleManager.getInstance().read(
                                Utils.getBleDevice(0),
                                Utils.getBluetoothGattService(),
                                Utils.getCharacteristicRead(),
                                new BleReadCallback() {
                                    @Override
                                    public void onReadSuccess(byte[] data) {
                                        String s = new String(data);
                                        Log.i("Read", s);
                                        if((int) Float.parseFloat(s)%10 == 1)
                                            progressStatus = (int) Float.parseFloat(s)/10;
                                        if((int) Float.parseFloat(s)%10 == 2)
                                            progressStatus2 = (int) Float.parseFloat(s)/10;
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
        }).start();
    }
}