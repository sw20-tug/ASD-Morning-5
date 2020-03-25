package com.morning5.vocabularytrainer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.morning5.vocabularytrainer.dto.TranslationContract;
import com.morning5.vocabularytrainer.dto.WordContract;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VocabularyTrainer.db";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + WordContract.Word.TABLE_NAME;

    private static final String SQL_CREATE_ENTRIES_WORD =
            "CREATE TABLE " + WordContract.Word.TABLE_NAME + " (" +
                    WordContract.Word._ID + " INTEGER PRIMARY KEY," +
                    WordContract.Word.Word + " TEXT," +
                    WordContract.Word.Language + " TEXT)";

    private static final String SQL_CREATE_ENTRIES_TRANSLATION =
            "CREATE TABLE " + TranslationContract.Translation.TABLE_NAME + " (" +
                    TranslationContract.Translation._ID + " INTEGER PRIMARY KEY," +
                    TranslationContract.Translation.Origin + " INTEGER," +
                    TranslationContract.Translation.Translation + " INTEGER)";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES_WORD);
        db.execSQL(SQL_CREATE_ENTRIES_TRANSLATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
