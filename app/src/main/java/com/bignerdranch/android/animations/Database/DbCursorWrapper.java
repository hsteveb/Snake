package com.bignerdranch.android.animations.Database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.bignerdranch.android.animations.Model.Score;

import java.util.List;
import java.util.UUID;

/**
 * Created by hector on 3/1/17.
 */

public class DbCursorWrapper extends CursorWrapper {

    public DbCursorWrapper(Cursor cursor) {
        super(cursor);
    }


    public Score getScore()
    {
        String name = getString(getColumnIndex(DbSchema.DbEntry.NAME));
        String score = getString(getColumnIndex(DbSchema.DbEntry.SCORE));
        return new Score(name, score);
    }
}
