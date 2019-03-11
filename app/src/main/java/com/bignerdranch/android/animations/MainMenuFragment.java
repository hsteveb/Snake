package com.bignerdranch.android.animations;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/* The main menu contains all the paths to the game including gameplay, settings, scores, and maybe even more.

 */

public class MainMenuFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MainMenuFragment";
    private Button mPlay, mLeaderboard, mSettings;

    private boolean once;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        once = false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_menu_fragment, container, false);

        //Log.d(TAG, "making");
        mPlay = v.findViewById(R.id.play_button);
        mLeaderboard = v.findViewById(R.id.leaderboard);
        mSettings = v.findViewById(R.id.settings);

        mPlay.setOnClickListener(this);
        mLeaderboard.setOnClickListener(this);
        mSettings.setOnClickListener(this);

        return v;
    }


    /*Onclick function deals in creating the fragment that we choose, where are options are
        *PlayFragment
        *LeaderboardFragment
        *SettingsFragments

        Once is used here to make sure that multiple fragments are not created
     */
    @Override
    public void onClick(View view) {

        if(!once)
        {
            once = true;
            switch(view.getId())
            {
                case R.id.play_button:
                    ((MainActivity)getActivity()).setBackground(R.color.darkGreen3);
                    buttonAnimation(mPlay, new PlayFragment());
                    break;
                case R.id.leaderboard:
                    ((MainActivity)getActivity()).setBackground(R.color.darkBlue3);
                    buttonAnimation(mLeaderboard, new LeaderboardFragment());
                    break;
                case R.id.settings:
                    buttonAnimation(mSettings, new SettingsFragment());
                    break;
                default:
                    break;
            }
        }

    }

    /*This animation is used to help do a smooth transition from one fragment to the other*/
    private void buttonAnimation(Button mbutton, final Fragment fragment)
    {
        ObjectAnimator colorAnim = ObjectAnimator.ofInt(mbutton, "textColor", getResources().getColor(R.color.white), Color.TRANSPARENT);
        colorAnim.setDuration(200);
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(1);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                getFragmentManager().beginTransaction()
                        .setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.fade_in, R.animator.fade_out)
                        .replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });
        colorAnim.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        once = false;
    }
}
