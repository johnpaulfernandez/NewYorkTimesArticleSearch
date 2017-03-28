package com.codepath.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.nytimessearch.models.Multimedia.multimediaFromJSONArray;

/**
 * Created by John on 3/18/2017.
 */

public class Article {

    String webUrl;
    String headline;
    String leadParagraph;
    List<Multimedia> multimedia;

    public Article(JSONObject jsonObject) {
        try {
            this.webUrl = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.leadParagraph = jsonObject.getString("lead_paragraph");

            JSONArray multimediaJSONArray = new JSONArray();
            multimediaJSONArray = jsonObject.getJSONArray("multimedia");

            if (multimediaJSONArray.length() > 0)
                this.multimedia = multimediaFromJSONArray(multimediaJSONArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadline() {
        return headline;
    }

    public String getLeadParagraph() {
        return leadParagraph;
    }

    public List<Multimedia> getMultimedia() {
        return multimedia;
    }

    public String getWideImage() {

        for (Multimedia multimedia: getMultimedia()) {

            if (multimedia.getSubtype() != null && multimedia.getSubtype().equalsIgnoreCase("wide"))
                return multimedia.getUrl();
        }

        return "";
    }

    public static ArrayList<Article> articlesFromJSONArray (JSONArray array) {
        ArrayList<Article> articles = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                articles.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return articles;
    }
}
