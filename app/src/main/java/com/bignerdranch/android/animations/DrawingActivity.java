package com.bignerdranch.android.animations;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class DrawingActivity extends AppCompatActivity {

    private static final String TAG = "DrawingActivity";
    private FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        mFrameLayout = findViewById(R.id.fragment_container);

        FragmentManager manager = getSupportFragmentManager();

        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if(fragment == null)
        {
            fragment = new MainMenuFragment();
            manager.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }

    public void setBackground(int color)
    {
        mFrameLayout.setBackgroundColor(getResources().getColor(color));
    }


    @Override
    public void onBackPressed() {
        Log.d(TAG, "testing this back press");
        super.onBackPressed();

    }
}
