package com.morning5.vocabularytrainer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
    ArrayList<VocabularyData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        sqLiteDatabase = new DbHelper(getBaseContext()).getWritableDatabase();

        listView = findViewById(R.id.list_view_overview);

        showVocabularies();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(OverviewActivity.this, EditWordActivity.class);

                VocabularyData d = list.get(position);

                // transfer to the other activity
                intent.putExtra("GET_ID", d.getId());
                intent.putExtra("GET_LANG1", d.getLanguage1());
                intent.putExtra("GET_LANG2", d.getLanguage2());
                intent.putExtra("GET_WORD1", d.getWord1());
                intent.putExtra("GET_WORD2", d.getWord2());

                Toast.makeText(getApplicationContext(),"Redirecting you to edit element with id: "+id,Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });


    }

    private void showVocabularies() {

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_LONG).show();
            return;
        }

        list = new ArrayList<VocabularyData>();
        OverviewAdapter overviewAdapter = new OverviewAdapter(list, this);

        while (cursor.moveToNext()) {
            VocabularyData vocabularyData = new VocabularyData(cursor.getString(0), cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4));
            list.add(vocabularyData);
        }

        cursor.close();

        listView.setAdapter(overviewAdapter);
    }
}
