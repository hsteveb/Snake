package com.bignerdranch.android.animations.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    }

    public List<PlayerScore> getScores() {

        mScores.clear();
         mSQLiteDatabase = mDbHelper.getReadableDatabase();

        String[] projection = {DbSchema.DbEntry._ID, DbSchema.DbEntry.NAME, DbSchema.DbEntry.SCORE};

        Cursor cursor = mSQLiteDatabase.query(DbSchema.DbEntry.TABLE, projection, null, null, null, null, null);

        cursor.moveToFirst();
        while(!cursor.isAfterLast())
        {
            mScores.add(new PlayerScore(cursor.getString(cursor.getColumnIndex(DbSchema.DbEntry.NAME)), cursor.getString(cursor.getColumnIndex(DbSchema.DbEntry.SCORE))));
            cursor.moveToNext();
        }

        return mScores;
    }

    public void addPlayerScore(String name, int score)
    {
        mSQLiteDatabase = mDbHelper.getWritableDatabase();


        ContentValues contentValues = new ContentValues();

        contentValues.put(DbSchema.DbEntry.NAME, name);
        contentValues.put(DbSchema.DbEntry.SCORE, Integer.toString(score));

        if(mSQLiteDatabase.insert(DbSchema.DbEntry.TABLE, null, contentValues) != -1);
    }

    public static SnakeDb getInstance(Context context)
    {
        if(sSnakeDb == null)
            sSnakeDb = new SnakeDb(context);

        return sSnakeDb;
    }
}
