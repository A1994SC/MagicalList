package com.derpaholic.magic.api;

import com.derpaholic.magic.misc.Constants;
import com.derpaholic.magic.misc.Utility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Scryfall {

    private static final String REGEX_REMOVE = "^(http(s|.{0}):\\/\\/)?scryfall\\.com\\/saved\\/share(.{0}|.json|.text)\\?id=";
    private static final String REGEX_VALIDATE = "^(http(s|.{0}):\\/\\/)?scryfall\\.com\\/saved\\/share(.{0}|.json|.text)\\?id=(.{3,4}\\:\\d{1,3}\\/?)+";
    private static final String CARD_FORMAT_REG = ".{3,4}\\:\\d{1,3}\\/?";

    private Scryfall() {
    }

    public static ArrayList<String> getListFromURL(String url) {
        if(!Utility.isRegexValid(REGEX_VALIDATE, url))
            return null;

        return new ArrayList<>(Arrays.asList(url.replaceAll(REGEX_REMOVE, "").split("/")));
    }

    /**
     * Will always at least return the 'multiverse_id'
     */
    public static HashMap<String, String> getFieldsFromCard(String card, String... fields) {
        if(!card.matches(CARD_FORMAT_REG)) {
            return null;
        }

        HashMap<String, String> list = new HashMap<>();

        String mod_card = card.toLowerCase();

        if(Utility.isRegexValid(Constants.PROMO_REGEX, mod_card)) {
            mod_card = mod_card.substring(1);
        }

        JsonObject json = Utility.getJsonFromURL(Constants.SCRYFALL_CARD + mod_card.replace(':', '/'));

        try {
            list.put(Constants.MID, json.get(Constants.MID).getAsString());
        } catch(NullPointerException e) {
            list.put(Constants.MID, Constants.NO_DATA);
        }

        for(String field : fields) {
            try {
                list.put(field, json.get(field).getAsString());
            } catch(NullPointerException e) {
                list.put(field, Constants.NO_DATA);
            }
        }

        return list;
    }

    public static JsonObject getFieldsFromCard(JsonObject jobj, String card, String... fields) {
        if(jobj == null) {
            jobj = new JsonObject();
            jobj.add(Constants.CARDS_ID, new JsonArray());
        } else if(jobj.get(Constants.CARDS_ID) == null)
            jobj.add(Constants.CARDS_ID, new JsonArray());

        HashMap<String, String> t = getFieldsFromCard(card, fields);

        JsonObject obj = Utility.getObjectFromArray(Constants.MID, t.get(Constants.MID), jobj.get(Constants.CARDS_ID).getAsJsonArray());
        if(obj != null) {
            try {
                for(String key : t.keySet())
                    obj.add(key, new JsonPrimitive(t.get(key)));
            } catch(Exception e) {
                System.out.println(card);
            }
        } else {
            JsonObject hold = new JsonObject();
            try {
                for(String key : t.keySet())
                    hold.add(key, new JsonPrimitive(t.get(key)));
                jobj.getAsJsonArray(Constants.CARDS_ID).add(hold);
            } catch(Exception e) {
                System.err.println(card);
                e.printStackTrace();
            }

        }

        return jobj;
    }


}
