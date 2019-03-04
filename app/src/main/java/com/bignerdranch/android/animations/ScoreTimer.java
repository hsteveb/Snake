package com.bignerdranch.android.animations;

import android.content.Context;
import android.nfc.Tag;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;

import java.sql.Time;

public class ScoreTimer implements Runnable {

    private static final String TAG = "ScoreTimer";

    private long initialTime, time;
    private boolean mContinue;

    private FragmentActivity mActivity;
    private SumTime mSumTime;
    private TextView mTime;
    private Thread mThread;


    /* Initializes all the objects that we need to make the timer work*/
    public ScoreTimer(TextView time, FragmentActivity activity)
    {

        mSumTime = new SumTime();
        mTime = time;
        mActivity = activity;
        mContinue = false;

        initialTime = this.time = 0;
        initialTime = 0;
    }

    /* Timer runs here where if the initialTime is 0 then you start the timer from the beginning,
       but if you paused the game and you wanted to restart it then it will restart from where ever it stopped*/

    @Override
    public void run() {
        if(!mContinue)
        {
            mContinue = true;
            while(mContinue)
            {
                if(initialTime == 0)
                {
                    initialTime = SystemClock.uptimeMillis();
                    //time = initialTime;
                }
                else
                    time = SystemClock.uptimeMillis() - initialTime;
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        SumTime time1 = new SumTime();
                        time1.addminute(time);
                        mTime.setText(time1.toString());
                        Log.d(TAG, "" + time);
                    }
                });
                SystemClock.sleep(1000);
            }
        }

    }

    public void stop()
    {
        mContinue = false;
    }

    /* Creates the thread that is needed to start the timer*/
    public void start()
    {
        mThread = new Thread(this);
        mThread.start();
        Log.d(TAG, "here");
    }
}
