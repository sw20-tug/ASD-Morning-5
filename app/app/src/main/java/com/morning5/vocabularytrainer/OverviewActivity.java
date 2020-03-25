package com.morning5.vocabularytrainer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.morning5.vocabularytrainer.adapters.OverviewAdapter;
import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.database.VocabularyData;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        sqLiteDatabase = new DbHelper(getBaseContext()).getWritableDatabase();

        listView = findViewById(R.id.list_view_overview);

        showVocabularies();
    }

    private void showVocabularies() {

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_LONG).show();
            return;
        }

        ArrayList<VocabularyData> list = new ArrayList<VocabularyData>();
        OverviewAdapter overviewAdapter = new OverviewAdapter(list, this);

        while (cursor.moveToNext()) {
            VocabularyData vocabularyData = new VocabularyData(cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
            list.add(vocabularyData);
        }

        cursor.close();

        listView.setAdapter(overviewAdapter);
    }
}
