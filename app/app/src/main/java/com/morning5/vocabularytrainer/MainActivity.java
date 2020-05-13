package com.morning5.vocabularytrainer;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.morning5.vocabularytrainer.database.DbHelper;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;


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

        button_change_language_EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAppLocale("en");
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

    public void onButtonClickAdvancedTesting(View v) {
        Intent intent = new Intent(MainActivity.this, AdvancedTestingActivity.class);
        MainActivity.this.startActivity(intent);
    }

    public void onButtonClickStudyInterface(View v) {
        Intent intent = new Intent(MainActivity.this, StudyInterfaceActivity.class);
        MainActivity.this.startActivity(intent);




        //db = new DbHelper(getBaseContext()).getWritableDatabase();
    }

    @SuppressWarnings("deprecation")
    private void setAppLocale(String localeCode) {
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(localeCode.toLowerCase()));
        res.updateConfiguration(conf, dm);
    }
}
