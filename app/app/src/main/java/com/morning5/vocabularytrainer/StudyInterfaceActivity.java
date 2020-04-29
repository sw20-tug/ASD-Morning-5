package com.morning5.vocabularytrainer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudyInterfaceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    SQLiteDatabase db;
    Spinner leftSpinner;
    Spinner rightSpinner;

    List<String> listWords = new ArrayList<>();
    List<String> listSpinnerRight = new ArrayList<>();
    Map<String, String> languageRelations = new HashMap<>();
    HashMap<String, String> words = new HashMap<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapterRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_interface);

        db = new DbHelper(getBaseContext()).getReadableDatabase();

        leftSpinner = findViewById(R.id.spinner_language_left);
        rightSpinner = findViewById(R.id.spinner_language_right);

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

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(translation);

                final AlertDialog alert = builder.create();
                alert.show();

                // Hide after some seconds
                final Handler handler = new Handler();
                final Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (alert.isShowing()) {
                            alert.dismiss();
                        }
                    }
                };

                alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        handler.removeCallbacks(runnable);
                    }
                });

                // Dismiss dialog after 10 seconds
                handler.postDelayed(runnable, 10000);
            }
        });

        setSpinners();
    }

    public void setSpinners() {
        List<String> listSpinnerLeft = new ArrayList<>();

        languageRelations = new HashMap<>();

        ArrayAdapter<String> adapterLeft = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listSpinnerLeft);
        adapterRight = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listSpinnerRight);

        Cursor cursor = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()) {
            String lang1 = cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1));
            String lang2 = cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2));

            languageRelations.put(lang1, lang2);
            languageRelations.put(lang2, lang1);
        }

        cursor.close();

        for (Map.Entry<String, String> entry : languageRelations.entrySet()) {
            listSpinnerLeft.add(entry.getKey());
            listSpinnerRight.add(entry.getValue());
        }

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
        Collections.sort(listWords);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        fillList();
        updateSpinnerRightSide();
    }

    private void updateSpinnerRightSide() {
        final String lang = leftSpinner.getSelectedItem().toString();

        listSpinnerRight.clear();
        listSpinnerRight.addAll(languageRelations.keySet().stream().filter(x -> !x.equals(lang)).collect(Collectors
                .toList()));

        adapterRight.notifyDataSetChanged();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
