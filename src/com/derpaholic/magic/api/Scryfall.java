package com.derpaholic.magic.api;

import com.derpaholic.magic.misc.Constants;
import com.derpaholic.magic.misc.Utility;
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
    public static HashMap<String, Object> getFieldsFromCard(String card, String... fields) {
        if(!card.matches(CARD_FORMAT_REG))
            return null;

        String mod_card = card.toLowerCase();

        if(Utility.isRegexValid(Constants.PROMO_REGEX, mod_card)) {
            mod_card = mod_card.substring(1);
        }

        JsonObject json = Utility.getJsonFromURL(Constants.SCRYFALL_CARD + mod_card.replace(':', '/'));
        JsonObject temp = new JsonObject();

        try {
            temp.add(Constants.MID, json.get(Constants.MID));
        } catch(NullPointerException e) {
            temp.add(Constants.MID, new JsonPrimitive(Constants.NO_DATA));
        }


        for(String field : fields) {
            try {
                if(json.get(field).isJsonArray())
                    temp.add(field, json.getAsJsonArray(field));
                else
                    temp.add(field, json.get(field));
            } catch(NullPointerException e) {
                temp.add(field, new JsonPrimitive(Constants.NO_DATA));
            }
        }

        return Utility.jsonObjectToHashMap(temp);
    }

    public static JsonObject getFieldsFromCard(JsonObject jobj, String card, String... fields) {
        Utility.initJsonObject(jobj);
        Utility.addMapToJsonObject(getFieldsFromCard(card, fields), jobj);

        return jobj;
    }


}
