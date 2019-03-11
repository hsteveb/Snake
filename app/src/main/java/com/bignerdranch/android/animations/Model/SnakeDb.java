package com.bignerdranch.android.animations.Model;

import com.bignerdranch.android.animations.Model.PlayerScore;

import java.util.ArrayList;
import java.util.List;

/* dummy scores used to check if the leaderboard is working as it should.
 */
public class SnakeDb {

    private List<PlayerScore> mScores;


    public SnakeDb()
    {
        mScores = new ArrayList<>();

        for(int i = 0; i < 100; i++)
        {
            PlayerScore score = new PlayerScore("ABC", "1:00:00");
            mScores.add(score);
        }
    }

    public List<PlayerScore> getScores() {
        return mScores;
    }
}
