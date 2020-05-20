package com.morning5.vocabularytrainer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.database.Cursor;
import com.morning5.vocabularytrainer.database.BackupHelper;
import com.morning5.vocabularytrainer.database.VocabularyData;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;

public class ShareActivity extends AppCompatActivity {

    SQLiteDatabase db;

    Map<String, String> vocabWords = new HashMap<>();

    BackupHelper backupHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        backupHelper = new BackupHelper(getApplicationContext());

        db = new DbHelper(getBaseContext()).getReadableDatabase();

        vocabWords = new HashMap<>();
        Cursor c = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (c.getCount() == 0) {
            Toast.makeText(this, "No data found!", Toast.LENGTH_LONG).show();
            return;
        }

        while (c.moveToNext()) {
            String word1 = c.getString(c.getColumnIndex(WordContract.Word.Word1));
            String word2 = c.getString(c.getColumnIndex(WordContract.Word.Word2));

            vocabWords.put(word1, word2);


        }

        c.close();


        Button button_all_vocabulary = (Button) findViewById(R.id.button_all_vocabulary);
        button_all_vocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backupHelper.exportData("share");
                String myFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/share.json";
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                File fileWithinMyDir = new File(myFilePath);

                if(fileWithinMyDir.exists()) {
                    intentShareFile.setType("application/json");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ShareActivity.this,BuildConfig.APPLICATION_ID+ ".fileprovider", fileWithinMyDir));


                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                }
            }
        });
    }
}