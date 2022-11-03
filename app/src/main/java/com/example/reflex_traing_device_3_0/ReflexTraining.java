package com.example.reflex_traing_device_3_0;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;



public class ReflexTraining extends AppCompatActivity implements View.OnClickListener {


    MediaPlayer mp;
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    TextView scoreboard;
    TextView avgRepTim;
    int times;
    int currBtn;
    int score;
    long recMills;
    int NUMBER_ITERATIONS = 5;
    double avgTime;
    boolean running = false;
    boolean correctBtnPress = false;

    int mCurrent = 5;
    int mMin = 1;
    int mMax = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reflex_training);

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
        Button playBD5 = this.findViewById(R.id.btnBD5);
        Button playBD6 = this.findViewById(R.id.btnBD6);

        stActBtn.setOnClickListener(this);
        stopActBtn.setOnClickListener(this);

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

        int localMusicButtonPressed = -1;

        switch (view.getId()) {
            case R.id.btnBD1:
                mp = MediaPlayer.create(this, R.raw.bdl1a);
                localMusicButtonPressed = 1;
                break;
            case R.id.btnBD2:
                mp = MediaPlayer.create(this, R.raw.bdl2a);
                localMusicButtonPressed = 2;
                break;
            case R.id.btnBD3:
                mp = MediaPlayer.create(this, R.raw.bdl3a);
                localMusicButtonPressed = 3;
                break;
            case R.id.btnBD4:
                mp = MediaPlayer.create(this, R.raw.bdl4a);
                localMusicButtonPressed = 4;
                break;
            case R.id.btnBD5:
                mp = MediaPlayer.create(this, R.raw.bdl5a);
                localMusicButtonPressed = 5;
                break;
            case R.id.btnBD6:
                mp = MediaPlayer.create(this, R.raw.bdl6a);
                localMusicButtonPressed = 6;
                break;
            case  R.id.btnStartActivity:
                NUMBER_ITERATIONS = mCurrent;
                times=NUMBER_ITERATIONS;
                score=0;
                startReflexTraining();
                break;
            case  R.id.btnStopActivity:
                times=1;
                stopReflexTraining();
                break;
            default:
                Toast.makeText(this, "Button Selection Failed", Toast.LENGTH_SHORT).show();

        }

        // Music Button Pressed
        if (localMusicButtonPressed != -1) {

            if (currBtn == localMusicButtonPressed  && running) {

                score++;
                correctBtnPress = true;
                avgTime = (avgTime*(NUMBER_ITERATIONS-times)+(2000-recMills))/(NUMBER_ITERATIONS-times+1);
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
        running = false;
        correctBtnPress = true;
        mCountDownTimer.cancel();
        mCountDownTimer.onFinish();
    }

    private void startReflexTraining() {

        running = true;
        int min = 1;
        int max = 6;
        int selPbar = (int)(Math.random()*(max-min+1)+min);
        currBtn = selPbar;
        switch (selPbar) {
            case 2:
                mProgressBar=findViewById(R.id.pgBar2);
                break;
            case 3:
                mProgressBar=findViewById(R.id.pgBar3);
                break;
            case 4:
                mProgressBar=findViewById(R.id.pgBar4);
                break;
            case 5:
                mProgressBar=findViewById(R.id.pgBar5);
                break;
            case 6:
                mProgressBar=findViewById(R.id.pgBar6);
                break;
            default:
                mProgressBar=findViewById(R.id.pgBar1);
                break;
        }
        mProgressBar.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.circular_progress_bar_active, null));
        mProgressBar.setProgress(100);
        mCountDownTimer=new CountDownTimer(2000,10) {

            @Override
            public void onTick(long millisUntilFinished) {
                mProgressBar.setProgress((int)(((double)millisUntilFinished/2000*100)));
                recMills = millisUntilFinished;
            }
            @Override
            public void onFinish() {
                //Do what you want
                if (correctBtnPress) {
                    correctBtnPress = false;
                }
                else {
                    avgTime = (avgTime*(NUMBER_ITERATIONS-times)+(2000))/(NUMBER_ITERATIONS-times+1);
                }
                mProgressBar.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.circular_progress_bar, null));
                mProgressBar.setProgress(100);
                if (times != 1) {
                    times--;
                    startReflexTraining();
                }
                else {
                    running = false;
                    scoreboard.setText("Score: "+score);
                    avgRepTim.setText("Average Response Time: "+(int)avgTime);
                }
            }
        };
        mCountDownTimer.start();
    }

}