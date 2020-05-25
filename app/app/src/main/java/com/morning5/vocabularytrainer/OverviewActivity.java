package com.morning5.vocabularytrainer;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class OverviewActivity extends AppCompatActivity {

    SQLiteDatabase sqLiteDatabase;
    ListView listView;
    ArrayList<VocabularyData> list = new ArrayList<VocabularyData>();;
    LinkedHashSet<String> different_languages = new LinkedHashSet<String>();
    HashMap<Integer, String> map_languages;
    HashMap<Integer, String> map_tags;
    LinkedHashSet<String> different_tags = new LinkedHashSet<String>();
    int call_before;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        sqLiteDatabase = new DbHelper(getBaseContext()).getWritableDatabase();

        listView = findViewById(R.id.list_view_overview);

        showVocabularies(0, 0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(OverviewActivity.this, EditWordActivity.class);

                VocabularyData d = list.get(position);

                // transfer to the other activity
                intent.putExtra("GET_ID", d.getId());
                intent.putExtra("GET_LANG1", d.getLanguage1());
                intent.putExtra("GET_LANG2", d.getLanguage2());
                intent.putExtra("GET_WORD1", d.getWord1());
                intent.putExtra("GET_WORD2", d.getWord2());
                intent.putExtra("GET_TAG", d.getTag());

                Toast.makeText(getApplicationContext(),"Redirecting you to edit element with id: "+id,Toast.LENGTH_SHORT).show();

                startActivity(intent);
            }
        });
    }

    // Option Menu -> for filtering/sorting
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort_filter, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem item = menu.findItem(R.id.filterLanguage);
        SubMenu sub = item.getSubMenu();

        sub.clear();
        sub.add(0, 0, 0, R.string.FilterOff);

        map_languages = new HashMap<Integer, String>();
        // iterate over our different languages1
        Iterator<String> itr = different_languages.iterator();
        int i = 1;
        while(itr.hasNext()){
            String lang = itr.next();
            sub.add(0, i, i, getResources().getString(R.string.FilterBy) + " " + lang);
            map_languages.put(i, lang);
            i++;
        }

        MenuItem item_tag = menu.findItem(R.id.filterTag);
        SubMenu sub_tag = item_tag.getSubMenu();
        sub_tag.clear();
        sub_tag.add(0, 0, 0, R.string.FilterOff);

        map_tags = new HashMap<Integer, String>();
        // iterate over our different languages1
        itr = different_tags.iterator();
        i = 1;
        while(itr.hasNext()){
            String tag = itr.next();
            sub_tag.add(0, i, i, getResources().getString(R.string.FilterBy) + " " + tag);
            map_tags.put(i, tag);
            i++;
        }

        return true;
    }

    // check which option is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 0: // Filter_off
                showVocabularies(0, 0);
                break;
            case R.id.sort1:
                printToast("Sorting... A-Z [word1]");
                Collections.sort(list, new VocabularyData.FirstWordSorter());
                break;
            case R.id.sort2:
                printToast("Sorting... A-Z [word2]");
                Collections.sort(list, new VocabularyData.SecondWordSorter());
                break;
           case R.id.sort3:
                printToast("Sorting... Z-A [word1]");
                Collections.sort(list, Collections.reverseOrder(new VocabularyData.FirstWordSorter()));
                break;
            case R.id.sort4:
                printToast("Sorting... Z-A [word2]");
                Collections.sort(list, Collections.reverseOrder(new VocabularyData.SecondWordSorter()));
                break;
            case R.id.sortTag1:
                printToast("Sorting... A-Z");
                Collections.sort(list, new VocabularyData.TagSorter());
                break;
            case R.id.sortTag2:
                printToast("Sorting... Z-A");
                Collections.sort(list, Collections.reverseOrder(new VocabularyData.TagSorter()));
                break;
            default:
                if (map_languages.containsKey(item.getItemId()) && call_before == R.id.filterLanguage) {
                    showVocabularies(item.getItemId(), 1);
                }
                else if(map_tags.containsKey(item.getItemId()) && call_before == R.id.filterTag){
                    showVocabularies(item.getItemId(), 2);

            }
                call_before = item.getItemId();
                return super.onOptionsItemSelected(item);
        }
        updateOverview();
        return true;
    }

    private void printToast(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }

    private void updateOverview()
    {
        OverviewAdapter overviewAdapter = new OverviewAdapter(list, this);
        listView.setAdapter(overviewAdapter);
    }

    private Cursor filterVocabulariesByLanguage(int filter_language_item_id)
    {
        Cursor cursor;
        String[] arr = new String[2];
        Arrays.fill(arr, map_languages.get(filter_language_item_id));

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +WordContract.Word.TABLE_NAME+ " WHERE " +WordContract.Word.Language1+ " = ? OR " +WordContract.Word.Language2+ " = ?", arr);

        return cursor;
    }

    private Cursor filterVocabulariesByTag(int filter_tag_item_id)
    {
        Cursor cursor;
        String[] arr = new String[1];
        Arrays.fill(arr, map_tags.get(filter_tag_item_id));

        cursor = sqLiteDatabase.rawQuery("SELECT * FROM " +WordContract.Word.TABLE_NAME+ " WHERE " +WordContract.Word.Tag+ " = ? ", arr);

        return cursor;
    }

    private void showVocabularies(int filter_item_id, int filter_type) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME, null);
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

        list = new ArrayList<VocabularyData>();
        OverviewAdapter overviewAdapter = new OverviewAdapter(list, this);

        different_languages = new LinkedHashSet<String>();
        different_tags = new LinkedHashSet<String>();
        while (cursor.moveToNext()) {
            VocabularyData vocabularyData = new VocabularyData(cursor.getString(cursor.getColumnIndex(WordContract.Word._ID)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Word1)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)),cursor.getString(cursor.getColumnIndex(WordContract.Word.Word2)),cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Tag)));
            list.add(vocabularyData);

            // adding just first language to the different languages ?
            different_languages.add(cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)));
            different_languages.add(cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)));
            different_tags.add(cursor.getString(cursor.getColumnIndex(WordContract.Word.Tag)));

        }

        cursor.close();
        listView.setAdapter(overviewAdapter);
    }
}
