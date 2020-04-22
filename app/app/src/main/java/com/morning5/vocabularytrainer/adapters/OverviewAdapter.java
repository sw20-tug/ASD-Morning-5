package com.morning5.vocabularytrainer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.morning5.vocabularytrainer.R;
import com.morning5.vocabularytrainer.database.VocabularyData;

import java.util.ArrayList;

public class OverviewAdapter extends ArrayAdapter<VocabularyData> implements View.OnClickListener {

    private ArrayList<VocabularyData> arrayList;
    private Context context;

    private static class ViewHolder {
        TextView txt_word_1;
        TextView txt_word_2;
        TextView tag;
    }

    public OverviewAdapter(ArrayList<VocabularyData> arrayList, Context context) {
        super(context, R.layout.overview_row_item, arrayList);
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        VocabularyData dataModel = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.overview_row_item, parent, false);
            viewHolder.txt_word_1 = (TextView) convertView.findViewById(R.id.word1);
            viewHolder.txt_word_2 = (TextView) convertView.findViewById(R.id.word2);
            viewHolder.tag = (TextView) convertView.findViewById(R.id.tag);

            result = convertView;

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txt_word_1.setText(dataModel.getWord1());
        viewHolder.txt_word_2.setText(dataModel.getWord2());
        viewHolder.tag.setText(dataModel.getTag());
        viewHolder.txt_word_1.setOnClickListener(this);

        return result;
    }
}


