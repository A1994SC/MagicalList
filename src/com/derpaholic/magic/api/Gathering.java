package com.derpaholic.magic.api;

import com.derpaholic.magic.misc.Constants;
import com.derpaholic.magic.misc.Debug;
import com.derpaholic.magic.misc.Utility;
import com.google.gson.*;
import jdk.nashorn.internal.ir.debug.JSONWriter;

import java.util.HashMap;
import java.util.Map;


public class Gathering {

    private Gathering() {
    }

    /**
     * Will always return "multiverseid" as "multiverse_id"
     */

    public static HashMap<String, String> getFieldsFromCard(String card, String... fields) {
        if(!Utility.isRegexValid("[0-9]+", card))
            return null;

        HashMap<String, String> list = new HashMap<>();

        JsonObject json = Utility.getJsonFromURL(Constants.GATHER_CARD + card);

        try {
            list.put(Constants.MID, json.get(Constants.GATHER_DEFAULT).getAsJsonObject().get(Constants.MID_GATH).getAsString());
        } catch(Exception e) {
            list.put(Constants.MID, Constants.NO_DATA);
        }

        for(String field : fields) {
            try {
                if(json.get(Constants.GATHER_DEFAULT).getAsJsonObject().get(field).isJsonArray())
                    list.put(field, Constants.GSON.toJson(json.getAsJsonObject(Constants.GATHER_DEFAULT).getAsJsonArray(field)));
                else
                    list.put(field, json.getAsJsonObject(Constants.GATHER_DEFAULT).get(field).getAsString());
            } catch(Exception e) {
                list.put(field, Constants.NO_DATA);
            }
        }

        return list;
    }

    public static JsonObject getFieldsFromCards(JsonObject jobj, String card, String... fields) {
        Utility.initJsonObject(jobj);
        Utility.addMapToJsonObject(getFieldsFromCard(card, fields), jobj);

        return jobj;
    }
}
