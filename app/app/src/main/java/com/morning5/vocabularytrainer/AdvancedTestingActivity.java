package com.morning5.vocabularytrainer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.morning5.vocabularytrainer.adapters.AdvancedTestingAdapter;
import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.database.VocabularyData;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;

public class AdvancedTestingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;
    AdvancedTestingAdapter adapter;
    ArrayList<VocabularyData> listWords = new ArrayList<>();
    ListView listView;
    ArrayList<VocabularyData> wordsToTest = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_testing);

        db = new DbHelper(getBaseContext()).getReadableDatabase();

        listView = findViewById(R.id.listview_test);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);
        listView.setOnItemClickListener(this);

        adapter = new AdvancedTestingAdapter(getBaseContext(), listWords);

        listView.setAdapter(adapter);

        fillList();
    }

    public void onClickExecuteTests(View v) {
        // ToDo need Test Mode for this one
        // use the list "wordsToTest"
    }

    public void fillList() {
        listWords.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            VocabularyData vocabularyData = new VocabularyData(
                    cursor.getString(cursor.getColumnIndex(WordContract.Word._ID)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Word1)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Word2)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Tag)));
            listWords.add(vocabularyData);
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        wordsToTest.clear();
        SparseBooleanArray sp = listView.getCheckedItemPositions();
        CheckedTextView checkedTextView = view.findViewById(R.id.av_word2);

        checkedTextView.setChecked(!checkedTextView.isChecked());

        for (int i = 0; i < sp.size(); i++) {
            if (sp.valueAt(i)) {
                wordsToTest.add((VocabularyData) parent.getItemAtPosition(position));
            }
        }
    }
}
