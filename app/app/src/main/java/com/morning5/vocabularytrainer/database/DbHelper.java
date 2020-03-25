package com.morning5.vocabularytrainer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.morning5.vocabularytrainer.dto.WordContract;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VocabularyTrainer.db";

    public static final String SQL_DELETE_WORD =
            "DROP TABLE IF EXISTS " + WordContract.Word.TABLE_NAME;

    public static final String SQL_CREATE_ENTRIES_WORD =
            "CREATE TABLE " + WordContract.Word.TABLE_NAME + "(" +
                    WordContract.Word._ID + " INTEGER primary key, " +
                    WordContract.Word.Word1 + " TEXT, " +
                    WordContract.Word.Language1 + " TEXT, " +
                    WordContract.Word.Word2 + " TEXT, " +
                    WordContract.Word.Language2 + " TEXT" +
                    ")";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_WORD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_WORD);
        onCreate(db);
    }
}
