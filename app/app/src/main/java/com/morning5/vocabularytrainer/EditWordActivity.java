package com.morning5.vocabularytrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;
import java.util.Locale;

public class EditWordActivity extends AppCompatActivity {


    SQLiteDatabase db;

    EditText inputWordLang1;
    EditText inputWordLang2;

    Button buttonSubmitChange;
    Button buttonBackOverview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        Button button_change_language_EN = findViewById(R.id.button_change_language_EN);
        Button button_change_language_DE = findViewById(R.id.button_change_language_DE);
        Button button_change_language_FR = findViewById(R.id.button_change_language_FR);

        String d_word1 = getIntent().getStringExtra("GET_WORD1");
        String d_word2 = getIntent().getStringExtra("GET_WORD2");

        inputWordLang1 = (EditText) findViewById(R.id.inputWordLang1);
        inputWordLang1.setHint(d_word1);

        inputWordLang2 = (EditText) findViewById(R.id.inputWordLang2);
        inputWordLang2.setHint(d_word2);
        buttonSubmitChange = (Button) findViewById(R.id.buttonSubmitChange);
        buttonBackOverview = (Button) findViewById(R.id.buttonBackOverview);
        db = new DbHelper(getBaseContext()).getWritableDatabase();





        buttonSubmitChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String new_word1 = inputWordLang1.getText().toString();
                String new_word2 = inputWordLang2.getText().toString();

                String d_id = getIntent().getStringExtra("GET_ID");

                //Toast.makeText(getApplicationContext(),"Hello you called id " + d_id + " and have new words "+new_word1 + " " +new_word2,Toast.LENGTH_SHORT).show();

                // Update the database
                ContentValues args = new ContentValues();
                args.put(WordContract.Word.Word1, new_word1);
                args.put(WordContract.Word.Word2, new_word2);
                db.update(WordContract.Word.TABLE_NAME, args, "_id="+d_id, null);
                Toast.makeText(getApplicationContext(),"Updated!", Toast.LENGTH_SHORT).show();
            }
        });




        buttonBackOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditWordActivity.this, OverviewActivity.class);
                EditWordActivity.this.startActivity(intent);
            }
        });

        button_change_language_EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppLocale("en");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        button_change_language_DE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppLocale("de");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        button_change_language_FR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppLocale("fr");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });


    }
    @SuppressWarnings("deprecation")
    private void setAppLocale(String localeCode){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf, dm);

}

}