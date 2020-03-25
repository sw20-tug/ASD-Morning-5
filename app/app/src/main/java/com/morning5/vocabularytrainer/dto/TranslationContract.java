package com.morning5.vocabularytrainer.dto;

import android.provider.BaseColumns;

public class TranslationContract {
    private TranslationContract() {
    }

    public class Translation implements BaseColumns {
        public static final String TABLE_NAME = "Translation";
        public static final String Origin = "Origin";
        public static final String Translation = "Translation";
    }
}
