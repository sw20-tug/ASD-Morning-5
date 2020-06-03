package com.morning5.vocabularytrainer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.LocaleList;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.morning5.vocabularytrainer.database.BackupHelper;
import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.dto.WordContract;
import com.morning5.vocabularytrainer.wrappers.ContextLocalWrapper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShareActivity extends AppCompatActivity {

    SQLiteDatabase db;
    Map<String, String> vocabWords = new HashMap<>();
    BackupHelper backupHelper;

    private static final String LANG = "lang";
    private static String languageCode = "";

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

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        LocaleList localeList = configuration.getLocales();
        languageCode = localeList.get(0).toString();
    }

    public void onButtonClickAllWords(View v) {
        backupHelper.exportData("share");
        String myFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/share.json";
        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(myFilePath);

        if (fileWithinMyDir.exists()) {
            intentShareFile.setType("application/json");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(ShareActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", fileWithinMyDir));

            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }
    }

    public void onButtonClickSomeWords(View v) {
        Intent intent = new Intent(ShareActivity.this, SelectWordsShareActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(LANG, languageCode);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ContextLocalWrapper.wrap(newBase, languageCode));
    }
}
