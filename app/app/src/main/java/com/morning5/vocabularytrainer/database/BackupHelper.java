package com.morning5.vocabularytrainer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.widget.Toast;

import com.morning5.vocabularytrainer.dto.WordContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class BackupHelper {

    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public BackupHelper(Context context) {
        this.context = context;
        this.sqLiteDatabase = new DbHelper(context).getWritableDatabase();
    }

    public void exportData() {

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            Toast.makeText(context, "No data found!", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            context.getApplicationContext();

            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/backup.json";

            FileOutputStream fos = new FileOutputStream(path);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            JSONArray jsonArray = new JSONArray();

            while (cursor.moveToNext()) {
                VocabularyData vocabularyData = new VocabularyData(cursor.getString(cursor.getColumnIndex(WordContract.Word._ID)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Word1)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Word2)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Tag)));
                jsonArray.put(vocabularyData.toJSON());
            }

            outputStreamWriter.write(jsonArray.toString());

            outputStreamWriter.flush();
            outputStreamWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importData(String path) {
        try {
            JSONArray jsonArray = new JSONArray(readJsonFile(path));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);


                ContentValues wordValues = new ContentValues();
                wordValues.put(WordContract.Word.Word1, jsonObject.getString("word1"));
                wordValues.put(WordContract.Word.Language1, jsonObject.getString("language1"));
                wordValues.put(WordContract.Word.Word2, jsonObject.getString("word2"));
                wordValues.put(WordContract.Word.Language2, jsonObject.getString("language2"));
                wordValues.put(WordContract.Word.Tag, jsonObject.getString("tag"));

                long wordId = sqLiteDatabase.insert(WordContract.Word.TABLE_NAME, null, wordValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String readJsonFile(String path) {

        String json;

        try {
            FileInputStream inputStream = new FileInputStream(path);

            int size = inputStream.available();

            byte[] buffer = new byte[size];

            inputStream.read(buffer);

            inputStream.close();

            json = new String(buffer, StandardCharsets.UTF_8);

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
