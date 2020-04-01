package com.morning5.vocabularytrainer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudyInterfaceActivity extends AppCompatActivity {
    SQLiteDatabase db;
    Spinner leftSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_interface);

        db = new DbHelper(getBaseContext()).getReadableDatabase();

        leftSpinner = findViewById(R.id.spinner_language_left);
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

        fillList();
    }

    public void fillList() {
        ArrayList<String> list = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);

        String lang = leftSpinner.getSelectedItem().toString();
        Toast.makeText(this, lang, Toast.LENGTH_LONG).show();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_LONG).show();
            return;
        }

        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(WordContract.Word.Word1)));
        }

        cursor.close();

        ((ListView) findViewById(R.id.list_view_study_interface)).setAdapter(adapter);
    }
}
