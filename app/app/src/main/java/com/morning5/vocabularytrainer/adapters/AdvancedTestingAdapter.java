package com.morning5.vocabularytrainer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.morning5.vocabularytrainer.R;
import com.morning5.vocabularytrainer.database.VocabularyData;

import java.util.ArrayList;

public class AdvancedTestingAdapter extends ArrayAdapter<VocabularyData> {
    private static class ViewHolder {
        TextView txt_word_1;
        CheckedTextView txt_word_2;
        TextView txt_tag;
    }

    public AdvancedTestingAdapter(Context context, ArrayList<VocabularyData> arrayList) {
        super(context, android.R.layout.simple_list_item_multiple_choice, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        VocabularyData dataModel = getItem(position);
        ViewHolder viewHolder = new ViewHolder();

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.advanced_testing_view, parent, false);
        viewHolder.txt_word_1 = convertView.findViewById(R.id.av_word1);
        viewHolder.txt_word_2 = convertView.findViewById(R.id.av_word2);
        viewHolder.txt_tag = convertView.findViewById(R.id.av_tag);

        if (((ListView) parent).isItemChecked(position)) {
            viewHolder.txt_word_2.setChecked(true);
        } else {
            viewHolder.txt_word_2.setChecked(false);
        }

        convertView.setTag(viewHolder);

        viewHolder.txt_word_1.setText(dataModel.getWord1());
        viewHolder.txt_word_2.setText(dataModel.getWord2());
        viewHolder.txt_tag.setText(String.format("Tags: %s", dataModel.getTag()));

        return convertView;
    }
}
