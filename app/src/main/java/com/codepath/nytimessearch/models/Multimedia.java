package com.codepath.nytimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by John on 3/27/2017.
 */

public class Multimedia {

    private String url;
    private String subtype;

    public Multimedia(JSONObject jsonObject) {
        try {
            this.url = "http://www.nytimes.com/" + jsonObject.getString("url");
            this.subtype = jsonObject.getString("subtype");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }

    public String getSubtype() {
        return subtype;
    }

    public static ArrayList<Multimedia> multimediaFromJSONArray (JSONArray array) {
        ArrayList<Multimedia> multimedia = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                multimedia.add(new Multimedia(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return multimedia;
    }
}
