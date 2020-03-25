package com.morning5.vocabularytrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;

public class EditWordActivity extends AppCompatActivity {
    SQLiteDatabase db;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        db = new DbHelper(getBaseContext()).getWritableDatabase();

        //ListView listView = (ListView) findViewById(R.id.list_view_overview);

        listView = (ListView)findViewById(R.id.listview_edit);
        ArrayList<String> arrayList = new ArrayList<String>();
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);


        // Fetch data from database
        Cursor cursor = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            showMessage("Error", "Nothing found");
        }

        StringBuilder stringBuilder = new StringBuilder();
        int i = 1;

        ContentValues cv = new ContentValues();
        cursor.moveToNext();
        arrayList.add(cursor.getString(1));

        cv.put(WordContract.Word._ID, 1);
        cv.put(WordContract.Word.Word, "UPDATED_WORD"+i);
        cv.put(WordContract.Word.Language, "UPDATED_LANG"+i);
        //db.update(WordContract.Word.TABLE_NAME, cv, cursor.getString(0) + "= ",  { String.valueOf() });
       /* while (cursor.moveToNext()) {
            cv.put(WordContract.Word._ID, i);
            cv.put(WordContract.Word.Word, "UPDATED_WORD"+i);
            cv.put(WordContract.Word.Language, "UPDATED_LANG"+i);

            //db.update(WordContract.Word.TABLE_NAME, cv, cursor.getString(0) + "=" + i, null);
            arrayList.add(cursor.getString(1));
            //stringBuilder.append("Id :").append(cursor.getString(0)).append("\n");
            //stringBuilder.append("Word :").append(cursor.getString(1)).append("\n");
            //stringBuilder.append("Language :").append(cursor.getString(2)).append("\n");

            //list.add(cursor.getString(0));
            i++;
        }*/
        //db.update(WordContract.Word.TABLE_NAME, cv, cursor.getString(0) + "=" + i, null);

        //cv.put();
        //WordContract.Word._ID

        //arrayList.add("Hallo");
        //arrayList.add("Translation");
        //showVocabularies();
        listView.setAdapter(arrayAdapter);
        //ArrayAdapter adapter = new ArrayAdapter<String>(this, , list);
    }


    private void showVocabularies() {

        Cursor cursor = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            showMessage("Error", "Nothing found");
        }

        StringBuilder stringBuilder = new StringBuilder();

        while (cursor.moveToNext()) {
            stringBuilder.append("Id :").append(cursor.getString(0)).append("\n");
            stringBuilder.append("Word :").append(cursor.getString(1)).append("\n");
            stringBuilder.append("Language :").append(cursor.getString(2)).append("\n");
            //list.add(cursor.getString(0));
        }

        showMessage("Data", stringBuilder.toString());

        cursor.close();
    }

    private void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }
}