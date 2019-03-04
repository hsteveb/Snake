package com.bignerdranch.android.animations;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bignerdranch.android.animations.Model.Score;

/*The UI part of the game is found in this fragment where the buttons are controlled.
 */
public class DrawingFragment extends Fragment implements View.OnClickListener, InfoInterface{

    private static final String TAG = "DrawingFragment";
    private boolean mStartPause;
    private int score;

    private Button mStartButton, mRestartButton;
    private DrawingView mDrawingView;
    private FrameLayout mFrameLayout;
    private TextView mTimerScoreTextView;
    private ObjectAnimator mStartButtonAnimator, mScoreTextViewAnimator, mRestartButtonAnimator, mPauseBackgroundAnimator;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_drawing_fragment, container, false);

        mDrawingView = v.findViewById(R.id.drawing_view);
        mFrameLayout = v.findViewById(R.id.activity_drawing_fragment_framelayout);
        mStartButton = v.findViewById(R.id.drawing_fragment_start_resume_button);
        mRestartButton = v.findViewById(R.id.drawing_fragment_pause_restart_button);
        mTimerScoreTextView = v.findViewById(R.id.drawing_fragment_time_text_view);
        mStartButton.setOnClickListener(this);
        mRestartButton.setOnClickListener(this);
        mDrawingView.setupitem(mRestartButton, this);

        return v;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.drawing_fragment_start_resume_button:
                if(mStartButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.start)))
                {

                    mStartButtonAnimator = ObjectAnimator.ofFloat(mStartButton, "alpha", 1, 0);
                    mStartButtonAnimator.addListener(new AnimatorListenerAdapter() {

                        int countdown = 5;

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            super.onAnimationRepeat(animation);
                            Log.d(TAG, "onRepeatAnimation");
                            mStartButton.setText(countdown + "");
                            --countdown;
                            //countdown--;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            Log.d(TAG, "testing animation");
                            mDrawingView.startgame();

                            mScoreTextViewAnimator = ObjectAnimator.ofFloat(mTimerScoreTextView, "alpha", 0 , 1);
                            mScoreTextViewAnimator.setDuration(500);
                            mScoreTextViewAnimator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mTimerScoreTextView.setVisibility(View.VISIBLE);
                                }
                            });
                            mScoreTextViewAnimator.start();

                            mRestartButtonAnimator = ObjectAnimator.ofFloat(mRestartButton, "alpha", 0 , 1);
                            mRestartButtonAnimator.setDuration(500);
                            mRestartButtonAnimator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    super.onAnimationStart(animation);
                                    mRestartButton.setAlpha(0);
                                    mRestartButton.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    //mRestartButton.setVisibility(View.VISIBLE);
                                }
                            });
                            mRestartButtonAnimator.start();

                            mStartButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    mStartButtonAnimator.setDuration(1000);
                    mStartButtonAnimator.setRepeatCount(5);
                    mStartButtonAnimator.setRepeatMode(ValueAnimator.RESTART);
                    mStartButtonAnimator.start();

                }
                else
                {
                    mPauseBackgroundAnimator = ObjectAnimator.ofFloat(mFrameLayout, "alpha", (float) .5, 0);
                    mPauseBackgroundAnimator.setDuration(750);
                    mPauseBackgroundAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationEnd(animation);
                            mRestartButtonAnimator = ObjectAnimator.ofFloat(mRestartButton, "alpha", 1, 0);
                            mRestartButtonAnimator.addListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationRepeat(Animator animation) {
                                    super.onAnimationRepeat(animation);
                                    mRestartButton.setText(R.string.pause);
                                }
                            });
                            mRestartButtonAnimator.setDuration(375);
                            mRestartButtonAnimator.setRepeatCount(1);
                            mRestartButtonAnimator.setRepeatMode(ValueAnimator.REVERSE);
                            mRestartButtonAnimator.start();
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mDrawingView.resumegame();
                            mStartButton.setVisibility(View.INVISIBLE);
                        }
                    });

                    mStartButtonAnimator = ObjectAnimator.ofFloat(mStartButton, "alpha", 0);
                    mStartButtonAnimator.setDuration(750);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(mPauseBackgroundAnimator, mStartButtonAnimator);
                    animatorSet.start();
                }
                break;
            case R.id.drawing_fragment_pause_restart_button:
                if(mRestartButton.getText().toString().equalsIgnoreCase(getResources().getString(R.string.pause)))
                {
                    mDrawingView.stopgame();

                    mStartButton.setText(R.string.resume);
                    mStartButton.setAlpha(0);
                    mStartButton.setVisibility(View.VISIBLE);

                    mFrameLayout.setAlpha(0);
                    mFrameLayout.setBackgroundColor(getResources().getColor(android.R.color.black));

                    mPauseBackgroundAnimator = ObjectAnimator.ofFloat(mFrameLayout, "alpha",(float) .5);
                    mPauseBackgroundAnimator.setDuration(750);

                    mRestartButtonAnimator = ObjectAnimator.ofFloat(mRestartButton, "alpha", 1, 0);
                    mRestartButtonAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationRepeat(Animator animation) {
                            super.onAnimationRepeat(animation);
                            Log.d(TAG, "testing repeat");
                            mRestartButton.setText(R.string.restart);
                        }
                    });
                    mRestartButtonAnimator.setDuration(375);
                    mRestartButtonAnimator.setRepeatCount(1);
                    mRestartButtonAnimator.setRepeatMode(ValueAnimator.REVERSE);
                    mRestartButtonAnimator.start();

                    mStartButtonAnimator = ObjectAnimator.ofFloat(mStartButton, "alpha", 1);
                    mStartButtonAnimator.setDuration(750);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(mPauseBackgroundAnimator, mStartButtonAnimator, mRestartButtonAnimator);
                    animatorSet.start();
                }
                else
                {
                    mStartButton.setVisibility(View.INVISIBLE);
                    mFrameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    mFrameLayout.setAlpha(1);
                    mRestartButton.setText(R.string.pause);
                    mDrawingView.startgame();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void info(int score) {

        this.score = score;
        mTimerScoreTextView.setText(String.format("Score: %d", score));

    }

    @Override
    public void reset() {
        mRestartButton.setText(R.string.restart);
        if(score > 0)
            saveHighScore(score);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        if(mDrawingView.playing())
            mDrawingView.stopgame();
    }


    private void saveHighScore(int score)
    {
        new ScoreDialogFragment().show(getFragmentManager(), null);
    }
}
