package com.morning5.vocabularytrainer.database;

import org.json.JSONException;
import org.json.JSONObject;

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

    public JSONObject toJSON() {

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", getId());
            jsonObject.put("word1", getWord1());
            jsonObject.put("language1", getLanguage1());
            jsonObject.put("word2", getWord2());
            jsonObject.put("language2", getLanguage2());

            return jsonObject;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
