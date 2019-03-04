package com.bignerdranch.android.animations.Database;

import android.provider.BaseColumns;

/**
 * Created by hector on 2/28/17.
 */

public class DbSchema {

    private DbSchema() {}

    public static class DbEntry implements BaseColumns
    {
        public static final String TABLE = "highscore";
        public static final String NAME = "name";
        public static final String SCORE = "score";
    }
}
