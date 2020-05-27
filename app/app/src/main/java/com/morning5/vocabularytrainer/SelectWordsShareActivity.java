package com.morning5.vocabularytrainer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.morning5.vocabularytrainer.adapters.AdvancedTestingAdapter;
import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.database.VocabularyData;
import com.morning5.vocabularytrainer.dto.WordContract;

import org.json.JSONArray;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static com.morning5.vocabularytrainer.BackupActivity.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_DATA;

public class SelectWordsShareActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;
    AdvancedTestingAdapter adapter;
    ArrayList<VocabularyData> listWords = new ArrayList<>();
    ListView listView;
    ArrayList<VocabularyData> wordsToShare = new ArrayList<>();
    LinkedHashSet<String> different_languages = new LinkedHashSet<String>();
    LinkedHashSet<String> different_tags = new LinkedHashSet<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_words);

        db = new DbHelper(getBaseContext()).getReadableDatabase();

        listView = findViewById(R.id.listview_words_to_share);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setItemsCanFocus(false);
        listView.setOnItemClickListener(this);

        adapter = new AdvancedTestingAdapter(getBaseContext(), listWords);

        listView.setAdapter(adapter);

        fillList(0,0);
    }

    public void onClickShareSelectedWords(View v) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_DATA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/share.json";

            FileOutputStream fos = new FileOutputStream(path);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
            JSONArray jsonArray = new JSONArray();

            for(VocabularyData word : wordsToShare) {
                jsonArray.put(word.toJSON());
            }

            outputStreamWriter.write(jsonArray.toString());

            outputStreamWriter.flush();
            outputStreamWriter.close();

            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            File fileWithinMyDir = new File(path);

            if (fileWithinMyDir.exists()) {
                intentShareFile.setType("application/json");
                intentShareFile.putExtra(Intent.EXTRA_STREAM, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", fileWithinMyDir));

                startActivity(Intent.createChooser(intentShareFile, "Share File"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printToast(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    public void fillList(int filter_item_id, int filter_type) {
        different_languages = new LinkedHashSet<String>();
        different_tags = new LinkedHashSet<String>();
        listWords.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);

        if (cursor.getCount() == 0) {
            printToast("No data found!");
            return;
        }

        while (cursor.moveToNext()) {
            VocabularyData vocabularyData = new VocabularyData(
                    cursor.getString(cursor.getColumnIndex(WordContract.Word._ID)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Word1)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Word2)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)),
                    cursor.getString(cursor.getColumnIndex(WordContract.Word.Tag)));
            listWords.add(vocabularyData);

            different_languages.add(cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)));
            different_languages.add(cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)));
            different_tags.add(cursor.getString(cursor.getColumnIndex(WordContract.Word.Tag)));
        }

        cursor.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        wordsToShare.clear();
        SparseBooleanArray sp = listView.getCheckedItemPositions();
        CheckedTextView checkedTextView = view.findViewById(R.id.av_word2);

        checkedTextView.setChecked(!checkedTextView.isChecked());

        for (int i = 0; i < sp.size(); i++) {
            if (sp.valueAt(i)) {
                wordsToShare.add((VocabularyData) parent.getItemAtPosition(sp.keyAt(i)));
            }
        }
    }
}
