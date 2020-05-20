package com.morning5.vocabularytrainer.dto;

import android.provider.BaseColumns;

public final class WordContract {
    private WordContract() {
    }

    public static class Word implements BaseColumns {
        public static final String TABLE_NAME = "Word";
        public static final String Word1 = "WordOne";
        public static final String Language1 = "LanguageOne";
        public static final String Word2 = "WordTwo";
        public static final String Language2 = "LanguageTwo";
        public static final String Tag = "Tag";

    }
}
