package com.example.reflex_traing_device_3_0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleReadCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.exception.BleException;
import com.google.android.material.navigation.NavigationView;


@SuppressLint("NonConstantResourceId")
public class ReflexTraining extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mp;
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    TextView scoreboard;
    TextView avgRepTim;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    ValuesDatabaseHelper DB;

    int times;
    int currBtn;
    int score;
    long recMills;
    int NUMBER_ITERATIONS = 5;
    double avgTime;
    boolean running = false;
    boolean correctBtnPress = false;
    boolean[] BLOCKINGVALS = {false, false, false};
    boolean isWriting = false;
    boolean isReading = false;
    private int rec_val = 0;
    Context context;

    int mCurrent = 5;
    int mMin = 1;
    int mMax = 100;
    int currDevRead = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex_training);

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
                                Utils.toast(ReflexTraining.this, "Stats Selected");
                                break;
                            case R.id.info:
                                Utils.toast(ReflexTraining.this, "Info Selected");
                                break;
                            case R.id.achievements:
                                Utils.toast(ReflexTraining.this, "Achievements Selected");
                                break;
                            case R.id.menu_reflex:
                                Utils.toast(ReflexTraining.this, "You are here!");
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

        context = getApplicationContext();

        DB = new ValuesDatabaseHelper(this);

        Button stActBtn = this.findViewById(R.id.btnStartActivity);
        Button stopActBtn = this.findViewById(R.id.btnStopActivity);

        //Future change?
        TextView mCurrentReps = findViewById(R.id.currReps);
        SeekBar mSeekbar = findViewById(R.id.sbBarSetReps);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSeekbar.setMin(mMin);
            mSeekbar.setMax(mMax);
        }
        mSeekbar.setProgress(mCurrent);
        mCurrentReps.setText(String.valueOf(mCurrent));
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mCurrent = i;
                mCurrentReps.setText(String.valueOf(mCurrent));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        scoreboard = this.findViewById(R.id.score);
        avgRepTim = this.findViewById(R.id.avgRepTim);

        // BD -> Bass Drum
        Button playBD1 = this.findViewById(R.id.btnBD1);
        Button playBD2 = this.findViewById(R.id.btnBD2);
        Button playBD3 = this.findViewById(R.id.btnBD3);
        Button playBD4 = this.findViewById(R.id.btnBD4);

        stActBtn.setOnClickListener(this);
        stopActBtn.setOnClickListener(this);

        playBD1.setOnClickListener(this);
        playBD2.setOnClickListener(this);
        playBD3.setOnClickListener(this);
        playBD4.setOnClickListener(this);

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

        int localMusicButtonPressed = -1;

        switch (view.getId()) {
            case R.id.btnBD1:
                mp = MediaPlayer.create(this, R.raw.kick2);
                localMusicButtonPressed = 1;
                break;
            case R.id.btnBD2:
                mp = MediaPlayer.create(this, R.raw.snare2);
                localMusicButtonPressed = 2;
                break;
            case R.id.btnBD3:
                mp = MediaPlayer.create(this, R.raw.tom2);
                localMusicButtonPressed = 3;
                break;
            case R.id.btnBD4:
                mp = MediaPlayer.create(this, R.raw.cymbal8);
                localMusicButtonPressed = 4;
                break;
            case R.id.btnStartActivity:
                if(!running) {
                    NUMBER_ITERATIONS = mCurrent;
                    times = NUMBER_ITERATIONS;
                    score = 0;
                    startReflexTraining();
                }
                break;
            case R.id.btnStopActivity:
                times = 1;
                stopReflexTraining();
                break;
            default:
                Toast.makeText(this, "Button Selection Failed", Toast.LENGTH_SHORT).show();

        }

        // Music Button Pressed
        if (localMusicButtonPressed != -1) {

            if (currBtn == localMusicButtonPressed && running) {

                score++;
                correctBtnPress = true;
                avgTime = (avgTime * (NUMBER_ITERATIONS - times) + (2000 - recMills)) / (NUMBER_ITERATIONS - times + 1);
                mCountDownTimer.cancel();
                mCountDownTimer.onFinish();

            }

            if (mp.isPlaying()) {
                mp.release();
            }

            mp.setOnPreparedListener(mediaPlayer -> mp.start());

            mp.setOnCompletionListener(mediaPlayer -> mp.release());

        }
    }

    private void stopReflexTraining() {
        if(running){
            running = false;
            mCountDownTimer.cancel();
            mCountDownTimer.onFinish();
        }
        isReading = false;
    }

    private void startReflexTraining() {

        running = true;

        int min = 1;
        int max = 4;
        int randomValGen = (int) (Math.random() * (max - min + 1) + min);

        currBtn = randomValGen;
        if (Utils.getCONNECTION_STATUS(currBtn-1) == 1) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isWriting = true;
            BleManager.getInstance().write(
                    Utils.getBleDevice(currBtn-1),
                    Utils.getBluetoothGattService(),
                    Utils.getCharacteristicWrite(),
                    Utils.hexStringToBytes(Integer.toHexString(20)),
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            Log.i("Write", String.valueOf(20));
                            isWriting = false;
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            Log.i("Write", exception.getDescription());
                            isWriting = false;
                        }
                    });

        }

        // select correct progress bar
        switch (randomValGen) {
            case 2:
                mProgressBar = findViewById(R.id.pgBar2);
                break;
            case 3:
                mProgressBar = findViewById(R.id.pgBar3);
                break;
            case 4:
                mProgressBar = findViewById(R.id.pgBar4);
                break;
            default:
                mProgressBar = findViewById(R.id.pgBar1);
                break;
        }
        mProgressBar.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.circular_progress_bar_active, null));
        mProgressBar.setProgress(100);
        mCountDownTimer = new CountDownTimer(2000, 20) {

            @Override
            public void onTick(long millisUntilFinished) {

                mProgressBar.setProgress((int) (((double) millisUntilFinished / 2000 * 100)));
                recMills = millisUntilFinished;

                if (Utils.getCONNECTION_STATUS(currDevRead) == 1 && !BLOCKINGVALS[currDevRead] && !isWriting && !isReading) {
                    isReading = true;
                    BleManager.getInstance().read(
                            Utils.getBleDevice(currDevRead),
                            Utils.getBluetoothGattService(),
                            Utils.getCharacteristicRead(),
                            new BleReadCallback() {
                                @Override
                                public void onReadSuccess(byte[] data) {
                                    String s = new String(data);
                                    if (!s.equals("")) {
                                        rec_val = (int) Float.parseFloat(s);
                                        if (!BLOCKINGVALS[currDevRead] && rec_val != 30) {
                                            playSound(rec_val);
                                            if (rec_val == currBtn) {
                                                score++;
                                                correctBtnPress = true;
                                            }
                                            Log.i("Read", s);
                                            BLOCKINGVALS[currDevRead] = true;
                                        } else if (rec_val == 30) {
                                            Log.i("Read", s);
                                            BLOCKINGVALS[currDevRead] = false;
                                            currDevRead++;
                                        }
                                    }
                                    isReading = false;
                                }

                                @Override
                                public void onReadFailure(BleException exception) {
                                    Log.i("Read", exception.getDescription());
                                    isReading = false;
                                }
                            });
                }
                if (Utils.getCONNECTION_STATUS(currDevRead) == 1 && BLOCKINGVALS[currDevRead] && !isWriting && !isReading){
                    isWriting = true;
                    BleManager.getInstance().write(
                            Utils.getBleDevice(currDevRead),
                            Utils.getBluetoothGattService(),
                            Utils.getCharacteristicWrite(),
                            Utils.hexStringToBytes(Integer.toHexString(30)),
                            new BleWriteCallback() {
                                @Override
                                public void onWriteSuccess(int current, int total, byte[] justWrite) {
                                    Log.i("Write30", String.valueOf(30));
                                    BLOCKINGVALS[currDevRead] = false;
                                    isWriting = false;
                                    currDevRead++;
                                }

                                @Override
                                public void onWriteFailure(BleException exception) {
                                    Log.i("Write", exception.getDescription());
                                    isWriting = false;
                                }
                            });
                }
                if (Utils.getCONNECTION_STATUS(currDevRead) == 0 ) {
                    currDevRead++;
                }
                if (currDevRead>3) currDevRead=0;
                if (correctBtnPress && !isWriting && !BLOCKINGVALS[currDevRead]) {
                    avgTime = (avgTime * (NUMBER_ITERATIONS - times) + (2000 - recMills)) / (NUMBER_ITERATIONS - times + 1);
                    mCountDownTimer.cancel();
                    mCountDownTimer.onFinish();
                }

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFinish() {
                if (correctBtnPress) {
                    correctBtnPress = false;
                } else {
                    avgTime = (avgTime * (NUMBER_ITERATIONS - times) + (2000)) / (NUMBER_ITERATIONS - times + 1);
                }
                mProgressBar.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.circular_progress_bar, null));
                mProgressBar.setProgress(100);

                if (times != 1) {
                    times--;

                    startReflexTraining();
                } else {
                    running = false;

                    boolean checkinsertdata = DB.insertData(1, (int) avgTime, score, (float) (score/NUMBER_ITERATIONS), 0, 0);
                    if(checkinsertdata)
                        Toast.makeText(ReflexTraining.this, "New Entry Inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ReflexTraining.this, "New Entry Not Inserted", Toast.LENGTH_SHORT).show();
                    scoreboard.setText("Score: " + score);
                    avgRepTim.setText("Average Response Time: " + (int) avgTime);
                }
            }
        };
        mCountDownTimer.start();

    }

    private void playSound(int rec_val) {

        int localMusicButtonPressed = -1;

        switch (rec_val) {
            case 1:
                mp = MediaPlayer.create(this, R.raw.kick1);
                localMusicButtonPressed = 1;
                break;
            case 2:
                mp = MediaPlayer.create(this, R.raw.snare1);
                localMusicButtonPressed = 2;
                break;
            case 3:
                mp = MediaPlayer.create(this, R.raw.tom1);
                localMusicButtonPressed = 3;
                break;
            case 4:
                mp = MediaPlayer.create(this, R.raw.cymbal8);
                localMusicButtonPressed = 4;
                break;
            default:
                Toast.makeText(this, "Sound Select Failed", Toast.LENGTH_SHORT).show();
        }

        // Music Played
        if (localMusicButtonPressed != -1) {
            if (mp.isPlaying()) {
                mp.release();
            }
            mp.setOnPreparedListener(mediaPlayer -> mp.start());
            mp.setOnCompletionListener(mediaPlayer -> mp.release());
        }

    }

}