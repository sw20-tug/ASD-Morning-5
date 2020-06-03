package com.morning5.vocabularytrainer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.morning5.vocabularytrainer.wrappers.ContextLocalWrapper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    private static final String LANG = "lang";
    private static String languageCode = "en";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_overview = findViewById(R.id.button_overview);
        Button button_add_word = findViewById(R.id.button_add_word);
        Button button_backup = findViewById(R.id.button_backup);
        Button button_change_language_EN = findViewById(R.id.button_change_language_EN);
        Button button_change_language_DE = findViewById(R.id.button_change_language_DE);
        Button button_change_language_FR = findViewById(R.id.button_change_language_FR);
        Button button_testing_mode = findViewById(R.id.button_testing_mode);
        Button button_share = findViewById(R.id.button_share);

        button_overview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        button_add_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddWordActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        button_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BackupActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        button_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShareActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        button_change_language_DE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageCode = "de";
                setAppLocale(languageCode);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        button_change_language_FR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageCode = "fr";
                setAppLocale(languageCode);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        button_change_language_EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                languageCode = "en";
                setAppLocale(languageCode);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        button_testing_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestingModeActivity.class);
                MainActivity.this.startActivity(intent);
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

    public void onButtonClickAdvancedTesting(View v) {
        Intent intent = new Intent(MainActivity.this, AdvancedTestingActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void onButtonClickStudyInterface(View v) {
        Intent intent = new Intent(MainActivity.this, StudyInterfaceActivity.class);
        MainActivity.this.startActivity(intent);




        //db = new DbHelper(getBaseContext()).getWritableDatabase();
    }

    private void setAppLocale(String localeCode) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }
}