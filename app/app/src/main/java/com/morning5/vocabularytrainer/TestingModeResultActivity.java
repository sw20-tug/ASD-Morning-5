package com.morning5.vocabularytrainer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.morning5.vocabularytrainer.database.VocabularyData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class TestingModeResultActivity extends AppCompatActivity {

    TextView textView_accuracy;
    TextView textView_time;
    TextView[] textViewArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_mode_result);

        textView_accuracy = findViewById(R.id.textView_accuracy);
        textView_time = findViewById(R.id.textView_time);

        textViewArray = new TextView[3];
        int text_view_id;

        for (int i = 0; i < 3; i++) {
            text_view_id = getResources().getIdentifier("textView_" + i, "id", getPackageName());
            textViewArray[i] = (TextView) findViewById(text_view_id);
        }

        Intent intent = getIntent();

        HashMap<VocabularyData, Integer> testing_hash_map = (HashMap<VocabularyData, Integer>) intent.getSerializableExtra("testing_hash_map");
        double time = intent.getDoubleExtra("time", 0);

        textView_time.setText("Your time: " + String.valueOf(time));

        Map.Entry<VocabularyData, Integer> entry = testing_hash_map.entrySet().iterator().next();

        VocabularyData vocabularyData = entry.getKey();
        Integer value = entry.getValue();

        calculateAccuracy(testing_hash_map);
        showWorstVocabularies(testing_hash_map);
    }

    private void calculateAccuracy(HashMap<VocabularyData, Integer> hashMap) {

        int try_sum = 0;
        double accuracy = 0.0;

        for (Map.Entry<VocabularyData, Integer> vocabularyDataIntegerEntry : hashMap.entrySet()) {
            try_sum += (int) ((Map.Entry) vocabularyDataIntegerEntry).getValue();
        }

        accuracy = ((double) hashMap.size() / try_sum) * 100;

        textView_accuracy.setText("Your accuracy: " + String.format(Locale.getDefault(), "%.2f", accuracy));
    }

    private void showWorstVocabularies(HashMap<VocabularyData, Integer> hashMap) {

        List<VocabularyData> sorted_list = hashMap.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.naturalOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        int offset = 3;

        if (sorted_list.size() < 3) {
            offset = sorted_list.size();
        }

        List<VocabularyData> worst_vocabularies = new ArrayList<VocabularyData>();
        worst_vocabularies = sorted_list.subList(sorted_list.size()-offset, sorted_list.size());

        for (int i = offset-1; i >= 0; i--) {
            textViewArray[i].setText(worst_vocabularies.get((offset-1)-i).getWord1());
        }
    }
}
