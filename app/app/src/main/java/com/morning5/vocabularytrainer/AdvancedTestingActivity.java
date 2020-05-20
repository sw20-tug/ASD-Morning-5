package com.morning5.vocabularytrainer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.morning5.vocabularytrainer.adapters.AdvancedTestingAdapter;
import com.morning5.vocabularytrainer.adapters.OverviewAdapter;
import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.database.VocabularyData;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class AdvancedTestingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;
    AdvancedTestingAdapter adapter;
    ArrayList<VocabularyData> listWords = new ArrayList<>();
    ListView listView;
    ArrayList<VocabularyData> wordsToTest = new ArrayList<>();
    LinkedHashSet<String> different_languages = new LinkedHashSet<String>();
    HashMap<Integer, String> map_languages;
    HashMap<Integer, String> map_tags;
    LinkedHashSet<String> different_tags = new LinkedHashSet<String>();
    int call_before;

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

        fillList(0,0);
    }

    public void onClickExecuteTests(View v) {
        // ToDo need Test Mode for this one
        // use the list "wordsToTest"

        Intent intent = new Intent(AdvancedTestingActivity.this, TestingModeActivity.class);
        intent.putExtra("WordsToTest", wordsToTest);
        startActivity(intent);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort_filter, menu);
        return true;
    }

    private void printToast(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem item = menu.findItem(R.id.filterLanguage);
        SubMenu sub = item.getSubMenu();

        sub.clear();
        sub.add(0, 0, 0, "Filter OFF");

        map_languages = new HashMap<Integer, String>();
        // iterate over our different languages1
        Iterator<String> itr = different_languages.iterator();
        int i = 1;
        while(itr.hasNext()){
            String lang = itr.next();
            sub.add(0, i, i, "Filter by " + lang);
            map_languages.put(i, lang);
            i++;
        }

        MenuItem item_tag = menu.findItem(R.id.filterTag);
        SubMenu sub_tag = item_tag.getSubMenu();
        sub_tag.clear();
        sub_tag.add(0, 0, 0, "Filter OFF");

        map_tags = new HashMap<Integer, String>();
        // iterate over our different languages1
        itr = different_tags.iterator();
        i = 1;
        while(itr.hasNext()){
            String tag = itr.next();
            sub_tag.add(0, i, i, "Filter by " + tag);
            map_tags.put(i, tag);
            i++;
        }

        return true;
    }

    private Cursor filterVocabulariesByTag(int filter_tag_item_id)
    {
        Cursor cursor;
        String[] arr = new String[1];
        Arrays.fill(arr, map_tags.get(filter_tag_item_id));

        cursor = db.rawQuery("SELECT * FROM " +WordContract.Word.TABLE_NAME+ " WHERE " +WordContract.Word.Tag+ " = ? ", arr);

        return cursor;
    }

    private Cursor filterVocabulariesByLanguage(int filter_language_item_id)
    {
        Cursor cursor;
        String[] arr = new String[2];
        Arrays.fill(arr, map_languages.get(filter_language_item_id));

        cursor = db.rawQuery("SELECT * FROM " +WordContract.Word.TABLE_NAME+ " WHERE " +WordContract.Word.Language1+ " = ? OR " +WordContract.Word.Language2+ " = ?", arr);

        return cursor;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: // Filter_off
                fillList(0, 0);
                break;
            case R.id.sort1:
                printToast("Sorting... A-Z [word1]");
                Collections.sort(listWords, new VocabularyData.FirstWordSorter());
                break;
            case R.id.sort2:
                printToast("Sorting... A-Z [word2]");
                Collections.sort(listWords, new VocabularyData.SecondWordSorter());
                break;
            case R.id.sort3:
                printToast("Sorting... Z-A [word1]");
                Collections.sort(listWords, Collections.reverseOrder(new VocabularyData.FirstWordSorter()));
                break;
            case R.id.sort4:
                printToast("Sorting... Z-A [word2]");
                Collections.sort(listWords, Collections.reverseOrder(new VocabularyData.SecondWordSorter()));
                break;
            case R.id.sortTag1:
                printToast("Sorting... A-Z");
                Collections.sort(listWords, new VocabularyData.TagSorter());
                break;
            case R.id.sortTag2:
                printToast("Sorting... Z-A");
                Collections.sort(listWords, Collections.reverseOrder(new VocabularyData.TagSorter()));
                break;
            default:
                if (map_languages.containsKey(item.getItemId()) && call_before == R.id.filterLanguage) {
                    fillList(item.getItemId(), 1);
                }
                else if(map_tags.containsKey(item.getItemId()) && call_before == R.id.filterTag){
                    fillList(item.getItemId(), 2);

                }
                call_before = item.getItemId();
                return super.onOptionsItemSelected(item);
        }
        adapter.notifyDataSetChanged();
        return true;
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
        if(filter_type == 1) {
            printToast("Filter Language");
            cursor = filterVocabulariesByLanguage(filter_item_id);
        }
        else if(filter_type == 2) {
            printToast("Filter Tag");
            cursor = filterVocabulariesByTag(filter_item_id);
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
        wordsToTest.clear();
        SparseBooleanArray sp = listView.getCheckedItemPositions();
        CheckedTextView checkedTextView = view.findViewById(R.id.av_word2);

        checkedTextView.setChecked(!checkedTextView.isChecked());

        for (int i = 0; i < sp.size(); i++) {
            if (sp.valueAt(i)) {
                wordsToTest.add((VocabularyData) parent.getItemAtPosition(sp.keyAt(i)));
            }

        }
    }
}
