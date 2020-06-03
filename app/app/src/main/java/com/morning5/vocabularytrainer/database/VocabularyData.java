package com.morning5.vocabularytrainer.database;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Comparator;

public class VocabularyData implements Serializable {

    private String id;
    private String word1;
    private String language1;
    private String word2;
    private String language2;
    private String tag;


    public VocabularyData(String id, String word1, String language1, String word2, String language2) {
        this.id = id;
        this.word1 = word1;
        this.language1 = language1;
        this.word2 = word2;
        this.language2 = language2;

    }

    public VocabularyData(String id, String word1, String language1, String word2, String language2, String tag) {
        this.id = id;
        this.word1 = word1;
        this.language1 = language1;
        this.word2 = word2;
        this.language2 = language2;
        this.tag = tag;

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

    public String getTag() { return tag;}

    public JSONObject toJSON() {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", getId());
            jsonObject.put("word1", getWord1());
            jsonObject.put("language1", getLanguage1());
            jsonObject.put("word2", getWord2());
            jsonObject.put("language2", getLanguage2());
            jsonObject.put("tag", getTag());

            return jsonObject;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

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

    public static class TagSorter implements Comparator<VocabularyData>
    {
        public int compare(VocabularyData o1, VocabularyData o2)
        {
            return o1.getTag().compareToIgnoreCase(o2.tag);
        }
    }
}
