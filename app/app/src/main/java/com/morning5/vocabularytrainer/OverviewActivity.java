package com.morning5.vocabularytrainer;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.morning5.vocabularytrainer.adapters.OverviewAdapter;
import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.database.VocabularyData;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;
import java.util.Collections;

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

        showVocabularies(0);

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

    // Option Menu -> for filtering/sorting
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort_filter, menu);
        return true;
    }

    // check which option is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort1:
                printToast("Sorting... A-Z [word1]");
                // sort A-Z word1
                Collections.sort(list, new VocabularyData.FirstWordSorter());
                updateOverview();
                return true;
            case R.id.sort2:
                printToast("Sorting... A-Z [word2]");
                // sort A-Z word2
                Collections.sort(list, new VocabularyData.SecondWordSorter());
                updateOverview();
                return true;
            case R.id.filter_off:
                printToast("Filtering... show all again");
                showVocabularies(0);
                return true;
            case R.id.filter_english:
                printToast("Filtering... show all with LANG1: English");
                showVocabularies(1);
                return true;
            case R.id.filter_german:
                printToast("Filtering... show all with LANG1: German");
                showVocabularies(2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void printToast(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    private void updateOverview()
    {
        OverviewAdapter overviewAdapter = new OverviewAdapter(list, this);
        listView.setAdapter(overviewAdapter);
    }

    private Cursor filterVocabulariesByLanguage(int filter_language1)
    {
        Cursor cursor;
        switch (filter_language1)
        {
            case 1: // filtered by English
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +WordContract.Word.TABLE_NAME+ " WHERE " +WordContract.Word.Language1+ " = ?",
                        new String[]{"English"});
                break;
            case 2: // filtered by German
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +WordContract.Word.TABLE_NAME+ " WHERE " +WordContract.Word.Language1+ " = ?",
                        new String[]{"German"});
                break;
            default: // no filter, all vocabularies shown
                cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);
                break;
        }
        return cursor;
    }

    private void showVocabularies(int filter_language1) {

        Cursor cursor = filterVocabulariesByLanguage(filter_language1);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_LONG).show();
            return;
        }

        list = new ArrayList<VocabularyData>();
        OverviewAdapter overviewAdapter = new OverviewAdapter(list, this);

        while (cursor.moveToNext()) {
            VocabularyData vocabularyData = new VocabularyData(cursor.getString(cursor.getColumnIndex(WordContract.Word._ID)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Word1)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)),cursor.getString(cursor.getColumnIndex(WordContract.Word.Word2)),cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)));
            list.add(vocabularyData);
        }

        cursor.close();

        listView.setAdapter(overviewAdapter);
    }
}
