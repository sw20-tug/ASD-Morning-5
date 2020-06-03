package com.morning5.vocabularytrainer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;
import com.morning5.vocabularytrainer.wrappers.ContextLocalWrapper;

public class EditWordActivity extends AppCompatActivity {


    SQLiteDatabase db;

    EditText inputWordLang1;
    EditText inputWordLang2;

    Button buttonSubmitChange;
    Button buttonBackOverview;

    private static final String LANG = "lang";
    private static String languageCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        String d_word1 = getIntent().getStringExtra("GET_WORD1");
        String d_word2 = getIntent().getStringExtra("GET_WORD2");

        inputWordLang1 = (EditText) findViewById(R.id.inputWordLang1);
        inputWordLang1.setHint(d_word1);

        inputWordLang2 = (EditText) findViewById(R.id.inputWordLang2);
        inputWordLang2.setHint(d_word2);
        buttonSubmitChange = (Button) findViewById(R.id.buttonSubmitChange);
        buttonBackOverview = (Button) findViewById(R.id.buttonBackOverview);
        db = new DbHelper(getBaseContext()).getWritableDatabase();

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        LocaleList localeList = configuration.getLocales();
        languageCode = localeList.get(0).toString();

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
                db.update(WordContract.Word.TABLE_NAME, args, "_id=" + d_id, null);
                Toast.makeText(getApplicationContext(), "Updated!", Toast.LENGTH_SHORT).show();
            }
        });


        buttonBackOverview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditWordActivity.this, OverviewActivity.class);
                EditWordActivity.this.startActivity(intent);
            }
        });
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