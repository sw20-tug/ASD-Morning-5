package com.morning5.vocabularytrainer.database;

import java.util.Comparator;

public class VocabularyData {

    private String id;
    private String word1;
    private String language1;
    private String word2;
    private String language2;

    public VocabularyData(String id, String word1, String language1, String word2, String language2) {
        this.id = id;
        this.word1 = word1;
        this.language1 = language1;
        this.word2 = word2;
        this.language2 = language2;
    }

    public String getId() {
        return id;
    }

    public String getWord1() {
        return word1;
    }

    public String getLanguage1() {
        return language1;
    }

    public String getWord2() {
        return word2;
    }

    public String getLanguage2() {
        return language2;
    }

    public static class FirstWordSorter implements Comparator<VocabularyData>
    {
        public int compare(VocabularyData o1, VocabularyData o2)
        {
            return o1.getWord1().compareToIgnoreCase(o2.getWord1());
        }
    }

    public static class SecondWordSorter implements Comparator<VocabularyData>
    {
        public int compare(VocabularyData o1, VocabularyData o2)
        {
            return o1.getWord2().compareToIgnoreCase(o2.getWord2());
        }
    }
}

