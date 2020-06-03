package com.morning5.vocabularytrainer;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;
import com.morning5.vocabularytrainer.wrappers.ContextLocalWrapper;

public class AddWordActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Snackbar snackbar_success;
    Snackbar snackbar_failure;

    private static final String LANG = "lang";
    private static String languageCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        // Gets the data repository in write mode

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        LocaleList localeList = configuration.getLocales();
        languageCode = localeList.get(0).toString();

        db = new DbHelper(getBaseContext()).getWritableDatabase();

        View myAddWordLayout = findViewById(R.id.myAddWordLayout);
        snackbar_success = Snackbar.make(myAddWordLayout, R.string.snackbar_success, Snackbar.LENGTH_LONG);
        snackbar_failure = Snackbar.make(myAddWordLayout, R.string.snackbar_fail, Snackbar.LENGTH_LONG);
    }

    public void onButtonClickAddWord(View v) {
        // Get focus and hide keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String english = ((TextView) findViewById(R.id.editText_english)).getText().toString();
        String german = ((TextView) findViewById(R.id.editText_german)).getText().toString();
        String tag = ((TextView) findViewById(R.id.editText_tag)).getText().toString();

        // Create a new map of values, where column names are the keys
        ContentValues wordValues = new ContentValues();
        wordValues.put(WordContract.Word.Word1, german);
        wordValues.put(WordContract.Word.Language1, "German");
        wordValues.put(WordContract.Word.Word2, english);
        wordValues.put(WordContract.Word.Language2, "English");
        wordValues.put(WordContract.Word.Tag, tag);

        // Insert the new row, returning the primary key value of the new row
        long wordId = db.insert(WordContract.Word.TABLE_NAME, null, wordValues);
        System.out.println("[USER ASDM5] wordId " + wordId);

        snackbar_success.show();

        Cursor c = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        c.moveToFirst();
        do {
            System.out.println("[USER ASDM5] " + c.getString(c.getColumnIndex(WordContract.Word.Language1)) + " " + c.getString(c.getColumnIndex(WordContract.Word.Word1)));
            System.out.println("[USER ASDM5] " + c.getString(c.getColumnIndex(WordContract.Word.Language2)) + " " + c.getString(c.getColumnIndex(WordContract.Word.Word2)));
        } while (c.moveToNext());

        c.close();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(LANG, languageCode);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextLocalWrapper.wrap(newBase, languageCode));
    }
}
