package com.bignerdranch.android.animations.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bignerdranch.android.animations.Database.DbHelper;
import com.bignerdranch.android.animations.Database.DbSchema;
import com.bignerdranch.android.animations.Model.PlayerScore;

import java.util.ArrayList;
import java.util.List;

/* dummy scores used to check if the leaderboard is working as it should.
 */
public class SnakeDb {

    private List<PlayerScore> mScores;
    private static SnakeDb sSnakeDb;
    private DbHelper mDbHelper;
    private SQLiteDatabase mSQLiteDatabase;


    private SnakeDb(Context context)
    {
        mDbHelper = new DbHelper(context);
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

    public void addPlayerScore(String name, int score)
    {
        mSQLiteDatabase = mDbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DbSchema.DbEntry.NAME, name);
        contentValues.put(DbSchema.DbEntry.SCORE, Integer.toString(score));
    }

    public static SnakeDb getInstance(Context context)
    {
        if(sSnakeDb == null)
            sSnakeDb = new SnakeDb(context);

        return sSnakeDb;
    }
}
