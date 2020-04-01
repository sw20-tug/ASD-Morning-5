package com.morning5.vocabularytrainer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyInterfaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SQLiteDatabase db;
    Spinner leftSpinner;
    List<String> listWords = new ArrayList<>();
    HashMap<String, String> words = new HashMap<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_interface);

        db = new DbHelper(getBaseContext()).getReadableDatabase();

        leftSpinner = findViewById(R.id.spinner_language_left);
        final ListView listView = findViewById(R.id.list_view_study_interface);

        leftSpinner.setOnItemSelectedListener(this);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listWords);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) listView.getItemAtPosition(position);
                String translation = words.get(item);

                ((TextView) findViewById(R.id.text_view_study_interface)).setText(translation);
            }
        });

        setSpinners();
    }

    public void setSpinners() {
        List<String> listSpinnerLeft = new ArrayList<>();
        List<String> listSpinnerRight = new ArrayList<>();

        Map<String, String> languageRelations = new HashMap<>();

        ArrayAdapter<String> adapterLeft = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listSpinnerLeft);
        ArrayAdapter<String> adapterRight = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listSpinnerRight);

        Cursor cursor = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()) {
            languageRelations.put(cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)));
        }

        cursor.close();

        for (Map.Entry<String, String> entry : languageRelations.entrySet()) {
            listSpinnerLeft.add(entry.getKey());
            listSpinnerLeft.add(entry.getValue());

            listSpinnerRight.add(entry.getValue());
            listSpinnerRight.add(entry.getKey());
        }

        Spinner rightSpinner = findViewById(R.id.spinner_language_right);

        adapterLeft.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leftSpinner.setAdapter(adapterLeft);

        adapterRight.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rightSpinner.setAdapter(adapterRight);
    }

    public void fillList() {
        words.clear();
        String lang = leftSpinner.getSelectedItem().toString();
        Toast.makeText(this, lang, Toast.LENGTH_LONG).show();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()) {
            String lang1 = cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1));
            String word1 = cursor.getString(cursor.getColumnIndex(WordContract.Word.Word1));
            String word2 = cursor.getString(cursor.getColumnIndex(WordContract.Word.Word2));

            if (lang.equals(lang1)) {
                words.put(word1, word2);
            } else {
                words.put(word2, word1);
            }
        }

        cursor.close();

        listWords.clear();
        listWords.addAll(words.keySet());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        fillList();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
