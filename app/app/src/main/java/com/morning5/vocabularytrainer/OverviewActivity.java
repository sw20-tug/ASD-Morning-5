package com.morning5.vocabularytrainer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.TranslationContract;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Intent intent = getIntent();

        sqLiteDatabase = new DbHelper(getBaseContext()).getWritableDatabase();

        listView = findViewById(R.id.list_view_overview);

        showVocabularies();
    }

    private void showVocabularies() {

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            showMessage("Error", "Nothing found");
        }

        StringBuilder stringBuilder = new StringBuilder();

        ArrayList<String> list = new ArrayList<String>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);

        while (cursor.moveToNext()) {
            /*stringBuilder.append("Id :").append(cursor.getString(0)).append("\n");
            stringBuilder.append("Word :").append(cursor.getString(1)).append("\n");
            stringBuilder.append("Language :").append(cursor.getString(2)).append("\n");*/

            list.add(cursor.getString(1));
        }

        //showMessage("Data", stringBuilder.toString());

        cursor.close();

        listView.setAdapter(adapter);
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
