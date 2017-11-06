package com.derpaholic.magic.misc;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Utility {

    private Utility() {
    }

    public static JsonObject getJsonFromURL(String url) {
        try {
            HttpURLConnection request = (HttpURLConnection) new URL(url).openConnection();
            request.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
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


    public static void initJsonObject(JsonObject jsoObj) {
        if(jsoObj == null) {
            jsoObj = new JsonObject();
            jsoObj.add(Constants.CARDS_ID, new JsonArray());
        } else if(jsoObj.get(Constants.CARDS_ID) == null)
            jsoObj.add(Constants.CARDS_ID, new JsonArray());
    }

    public static void addMapToJsonObject(HashMap<String, String> t, JsonObject jobj) {
        JsonObject obj = Utility.getObjectFromArray(Constants.MID, t.get(Constants.MID), jobj.get(Constants.CARDS_ID).getAsJsonArray());
        JsonObject jsoObj;

        /*
         * https://img.devrant.com/devrant/rant/r_464533_gMBvP.jpg
         */
        try {
            jsoObj = new JsonParser().parse(t.toString()).getAsJsonObject();
        } catch(Exception e) {
            jsoObj = new JsonParser().parse(Constants.GSON.toJson(t)).getAsJsonObject();
        }
        /*
         * WHY!!!!!!!......
         */

        if(obj != null) {
            for(Map.Entry entry : jsoObj.entrySet()) {
                try {
                    if(jsoObj.get(entry.getKey().toString()).isJsonArray())
                        obj.add(entry.getKey().toString(), jsoObj.get(entry.getKey().toString()).getAsJsonArray());
                    else if(jsoObj.get(entry.getKey().toString()).isJsonObject())
                        obj.add(entry.getKey().toString(), jsoObj.get(entry.getKey().toString()).getAsJsonObject());
                    else
                        obj.add(entry.getKey().toString(), jsoObj.get(entry.getKey().toString()).getAsJsonPrimitive());
                } catch(Exception e) {
                    System.out.println(entry.getKey().toString());
                    System.out.println(jsoObj.get(entry.getKey().toString()));
                }
            }
        } else {
            jobj.getAsJsonArray(Constants.CARDS_ID).add(jsoObj);
        }
    }

}
