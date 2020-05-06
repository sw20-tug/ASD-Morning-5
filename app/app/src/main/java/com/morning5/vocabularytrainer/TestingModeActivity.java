package com.morning5.vocabularytrainer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.morning5.vocabularytrainer.database.DbHelper;
import com.morning5.vocabularytrainer.database.VocabularyData;
import com.morning5.vocabularytrainer.dto.WordContract;

import java.util.ArrayList;
import java.util.HashMap;

public class TestingModeActivity extends AppCompatActivity {

    SQLiteDatabase db;
    ArrayList<VocabularyData> testing_words_list;
    TextView textView_testing_mode_word_and_language1;
    TextView textView_testing_mode_translate_to_language2;
    EditText editText_input_word;
    String input_word;
    int score;
    boolean game_won = false;
    HashMap<VocabularyData, Integer> map_try_counter; // counts how many tries till right

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_mode);
        textView_testing_mode_word_and_language1 = (TextView)findViewById(R.id.textView_testing_mode_word_and_language1);
        textView_testing_mode_translate_to_language2 = (TextView)findViewById(R.id.textView_testing_mode_translate_to_language2);
        score = 0;
        db = new DbHelper(getBaseContext()).getWritableDatabase();
        //Button button_change_language_EN = findViewById(R.id.button_change_language_EN);
        getVocabularies();
        showNextWordToGuess();

        Button buttonSubmitTestingWord = (Button)findViewById(R.id.buttonSubmitTestingWord);

        buttonSubmitTestingWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!game_won){
                    editText_input_word = (EditText)findViewById(R.id.inputTestingWord);
                    input_word = editText_input_word.getText().toString();
                    game_won = checkInput(input_word);
                }

                if(game_won)
                {
                    // start new activity
                    printToast("Congratulations! King of Vocabulary! ;)");
                    return;
                }
            }

        });
    }

    private void printToast(String msg)
    {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT).show();
    }
    private void showNextWordToGuess()
    {
        String show_word_to_guess = ""+testing_words_list.get(0).getWord1()+ " ("+testing_words_list.get(0).getLanguage1()+")";
        textView_testing_mode_word_and_language1.setText(show_word_to_guess);
        String translate_to = "Translate to: " +testing_words_list.get(0).getLanguage2();
        textView_testing_mode_translate_to_language2.setText(translate_to);
    }

    private boolean checkInput(String input_word)
    {
        printToast("old size: "+testing_words_list.size());
        VocabularyData current_vocabulary = testing_words_list.get(0);
        String current_word_solution = current_vocabulary.getWord2();
        int value = map_try_counter.get(current_vocabulary);

        if (input_word.equalsIgnoreCase(current_word_solution))
        {
            printToast("You are right!");
            testing_words_list.remove(0);
            if (!testing_words_list.isEmpty()) {
                showNextWordToGuess();
            }
        } else {
            if (current_vocabulary != testing_words_list.get(testing_words_list.size()-1))
                testing_words_list.add(current_vocabulary);

            printToast("Eww... Try again!");
        }
        printToast("new size: "+testing_words_list.size());
        map_try_counter.put(current_vocabulary, ++value);

        return testing_words_list.isEmpty();
    }

    private void getVocabularies() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + WordContract.Word.TABLE_NAME + " ORDER BY RANDOM() LIMIT 10", null);

        if (cursor.getCount() == 0) {
            printToast("No data found!");
            return;
        }

        testing_words_list = new ArrayList<VocabularyData>();
        map_try_counter = new HashMap<VocabularyData, Integer>();
        while (cursor.moveToNext()) {
            VocabularyData vocabularyData = new VocabularyData(cursor.getString(cursor.getColumnIndex(WordContract.Word._ID)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Word1)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)),cursor.getString(cursor.getColumnIndex(WordContract.Word.Word2)),cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)));

            //VocabularyData vocabularyData = new VocabularyData(cursor.getString(cursor.getColumnIndex(WordContract.Word._ID)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Word1)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)),cursor.getString(cursor.getColumnIndex(WordContract.Word.Word2)),cursor.getString(cursor.getColumnIndex(WordContract.Word.Language2)), cursor.getString(cursor.getColumnIndex(WordContract.Word.Tag)));

            testing_words_list.add(vocabularyData);
            map_try_counter.put(vocabularyData, 0);
            // adding just first language to the different languages ?
           /* different_languages.add(cursor.getString(cursor.getColumnIndex(WordContract.Word.Language1)));*/

        }

        cursor.close();

    }
}
