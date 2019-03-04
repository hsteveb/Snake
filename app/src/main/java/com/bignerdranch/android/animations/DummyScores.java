package com.bignerdranch.android.animations;

import com.bignerdranch.android.animations.Model.Score;

import java.util.ArrayList;
import java.util.List;

/* dummy scores used to check if the leaderboard is working as it should.
 */
public class DummyScores {

    private List<Score> mScores;


    public DummyScores()
    {
        mScores = new ArrayList<>();

        for(int i = 0; i < 100; i++)
        {
            Score score = new Score("ABC", "1:00:00");
            mScores.add(score);
        }
    }

    public List<Score> getScores() {
        return mScores;
    }
}
