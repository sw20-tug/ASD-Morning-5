package com.morning5.vocabularytrainer.dto;

import android.provider.BaseColumns;

public final class WordContract {
    private WordContract() {
    }

    public static class Word implements BaseColumns {
        public static final String TABLE_NAME = "Word";
        public static final String Word = "Word";
        public static final String Language = "Language";
    }
}
