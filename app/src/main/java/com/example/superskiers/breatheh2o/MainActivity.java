package com.example.superskiers.breatheh2o;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 6000;


    private Animation anim1;
    private Animation beta;

    private ProgressBar progressBarCircle;

    private TextView mTextViewCountDown;
    private TextView mTextView;
    private TextView mTextViewAction;

    private Button mButtonStartPause;
    private Button mButtonReset;
    private ImageView imageView;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;
    //For accurate time keeping when changing state
    private long mEndTime;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Method call to initialize the views
        initViews();

        //Check whether we're recreating a previously destroyed instance
        if (savedInstanceState != null) {
            //Restore value of members from saved state

        }

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });
        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    //startTimer initializes the abstract class count down timer object
    private void startTimer() {
        //mEndtime uses the System clock to ensure the timer is precise when changing state
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mTextViewAction.startAnimation(anim1);

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            //This method updates the timer for each second
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                setmTextViewCountDown();
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));
            }


            //OnFinish notifies us when countdown has stopped
            @Override
            public void onFinish() {
                mTimerRunning = false;
                mButtonStartPause.setText("Start");
                mTextViewAction.setText("HOLD");
                mTextViewAction.startAnimation(beta);
                mButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();
        mTimerRunning = true;
        updateButtons();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        mButtonStartPause.setText("Start");
        updateButtons();
    }

    private void resetTimer() {
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        setProgressBarCircle();
        updateButtons();
    }

    private void setProgressBarCircle() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        progressBarCircle.setMax((int) mTimeLeftInMillis / 1000);
        progressBarCircle.setProgress((int) mTimeLeftInMillis / 1000);

    }

    private void setmTextViewCountDown () {
        int minutes = (int) (mTimeLeftInMillis / 1000) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),
                "%02d:%02d", minutes, seconds);
        mTextViewCountDown.setText(timeLeftFormatted);
    }

    //This method is used to update buttons when the activity changes
    private void updateButtons() {
        if (mTimerRunning) {
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mButtonStartPause.setText("Start");
            mButtonReset.setVisibility(View.VISIBLE);
            //1000 is equal to 1 second
            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }
            if (mTimeLeftInMillis < START_TIME_IN_MILLIS) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    //onSaveInstanceState is used to save variables when screen is rotated.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
        //Keys and values to be saved
        outState.putLong("millisLeft", mTimeLeftInMillis);
        outState.putBoolean("timerRunning", mTimerRunning);
        outState.putLong("endTime", mEndTime);
        outState.putLong("endTime", mEndTime);

        }


    //This method is called after onCreate IF there is a savedInstanceState
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //Call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        //Restore state member from saved instance
        mTimeLeftInMillis = savedInstanceState.getLong("millisLeft");
        mTimerRunning = savedInstanceState.getBoolean("timerRunning");
        setProgressBarCircle();
        updateButtons();

        if (mTimerRunning) {
            mTextViewAction.startAnimation(anim1);
            mEndTime = savedInstanceState.getLong("endTime");
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            startTimer();
        }
    }
    private void initViews() {
        progressBarCircle = findViewById(R.id.progressBarCircle);
        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.white_background);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonStartPause = findViewById(R.id.center_button);
        mButtonReset = findViewById(R.id.right_button);
        mTextView = findViewById(R.id.text_view);
        mTextViewAction = findViewById(R.id.text_view_action);
        anim1 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        beta = AnimationUtils.loadAnimation(this, R.anim.beta);
    }


}



