package com.derpaholic.magic.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

public class Utility {

    private Utility() {
    }

    public static JsonObject getJsonFromURL(String url) {
        try {
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
            request.connect();

            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));

            return root.getAsJsonObject();
        } catch(Exception e) {
            return null;
        }
    }

    public static boolean isRegexValid(String regex, String test) {
        return Pattern.compile(regex).matcher(test).find();
    }

    public static boolean isValueInArray(String key, String val, JsonArray ary) {
        JsonObject obj;
        for(int i = 0; i < ary.size(); i++) {
            obj = ary.get(i).getAsJsonObject();
            if(obj.get(key) != null)
                if(obj.get(key).getAsString().equalsIgnoreCase(val))
                    return true;
        }
        return false;
    }

    public static JsonObject getObjectFromArray(String key, String val, JsonArray ary) {
        if(isValueInArray(key, val, ary)) {
            JsonObject obj;
            for(int i = 0; i < ary.size(); i++) {
                obj = ary.get(i).getAsJsonObject();
                if(obj.get(key) != null)
                    if(obj.get(key).getAsString().equalsIgnoreCase(val))
                        return obj;
            }
        }
        return null;
    }
}
