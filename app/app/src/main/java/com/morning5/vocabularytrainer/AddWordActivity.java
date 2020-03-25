package com.morning5.vocabularytrainer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.TranslationContract;
import com.morning5.vocabularytrainer.dto.WordContract;

public class AddWordActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Snackbar snackbar_success;
    Snackbar snackbar_failure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        // Gets the data repository in write mode

        db = new DbHelper(getBaseContext()).getWritableDatabase();

        View myAddWordLayout = findViewById(R.id.myAddWordLayout);
        snackbar_success = Snackbar.make(myAddWordLayout, R.string.snackbar_success, Snackbar.LENGTH_LONG);
        snackbar_failure = Snackbar.make(myAddWordLayout, R.string.snackbar_fail, Snackbar.LENGTH_LONG);
    }

    public void onButtonClickAddWord(View v) {
        // Get focus and hide keyboard
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        String english = ((TextView) findViewById(R.id.editText_english)).getText().toString();
        String german = ((TextView) findViewById(R.id.editText_german)).getText().toString();

        // Create a new map of values, where column names are the keys
        ContentValues germanValues = new ContentValues();
        germanValues.put(WordContract.Word.Word, german);
        germanValues.put(WordContract.Word.Language, "German");

        ContentValues englishValues = new ContentValues();
        englishValues.put(WordContract.Word.Word, english);
        englishValues.put(WordContract.Word.Language, "English");

        // Insert the new row, returning the primary key value of the new row
        long germanId = db.insert(WordContract.Word.TABLE_NAME, null, germanValues);
        long englishId = db.insert(WordContract.Word.TABLE_NAME, null, englishValues);
        System.out.println("[USER ASDM5] germanId " + germanId);
        System.out.println("[USER ASDM5] englishId " + englishId);

        ContentValues translationValues = new ContentValues();
        translationValues.put(TranslationContract.Translation.Origin, englishId);
        translationValues.put(TranslationContract.Translation.Translation, germanId);

        long translationId = db.insert(TranslationContract.Translation.TABLE_NAME, null, translationValues);

        snackbar_success.show();
        System.out.println("[USER ASDM5] TranslationId " + translationId);
        Cursor c = db.rawQuery("SELECT * FROM " + TranslationContract.Translation.TABLE_NAME, null);

        c.moveToFirst();
        do
        {
            System.out.println("[USER ASDM5] Origin " + c.getString(c.getColumnIndex("Origin")));
            System.out.println("[USER ASDM5] Translation " + c.getString(c.getColumnIndex("Translation")));
        } while (c.moveToNext());

        c.close();
    }
}
