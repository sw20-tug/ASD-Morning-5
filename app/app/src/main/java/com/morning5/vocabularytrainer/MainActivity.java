package com.morning5.vocabularytrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button button_overview = findViewById(R.id.btn_overview);

        button_overview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        db = new DbHelper(getBaseContext()).getWritableDatabase();
        addWords();
    }

    protected void addWords()
    {
        // Create a new map of values, where column names are the keys
        ContentValues germanValues = new ContentValues();
        germanValues.put(WordContract.Word.Word, "german");
        germanValues.put(WordContract.Word.Language, "German");

        ContentValues englishValues = new ContentValues();
        englishValues.put(WordContract.Word.Word, "english");
        englishValues.put(WordContract.Word.Language, "English");

        // Insert the new row, returning the primary key value of the new row
        long germanId = db.insert(WordContract.Word.TABLE_NAME, null, germanValues);
        long englishId = db.insert(WordContract.Word.TABLE_NAME, null, englishValues);
    }
}
