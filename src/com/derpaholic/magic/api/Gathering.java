package com.derpaholic.magic.api;

import com.derpaholic.magic.misc.Constants;
import com.derpaholic.magic.misc.Utility;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;


public class Gathering {

    private Gathering() {
    }

    /**
     * Will always return "multiverseid" as "multiverse_id"
     */

    public static HashMap<String, Object> getFieldsFromCard(String card, String... fields) {
        if(!Utility.isRegexValid("[0-9]+", card))
            return null;

        JsonObject json = Utility.getJsonFromURL(Constants.GATHER_CARD + card);
        JsonObject temp = new JsonObject();

        try {
            temp.add(Constants.MID, json.get(Constants.GATHER_DEFAULT).getAsJsonObject().get(Constants.MID_GATH));
        } catch(Exception e) {
            temp.add(Constants.MID, new JsonPrimitive(Constants.NO_DATA));
        }

        for(String field : fields) {
            try {
                if(json.get(Constants.GATHER_DEFAULT).getAsJsonObject().get(field).isJsonArray())
                    temp.add(field, json.getAsJsonObject(Constants.GATHER_DEFAULT).getAsJsonArray(field));
                else
                    temp.add(field, json.getAsJsonObject(Constants.GATHER_DEFAULT).get(field));
            } catch(Exception e) {
                temp.add(field, new JsonPrimitive(Constants.NO_DATA));
            }
        }

        return Utility.jsonObjectToHashMap(temp);
    }

    public static JsonObject getFieldsFromCards(JsonObject jobj, String card, String... fields) {
        Utility.initJsonObject(jobj);
        Utility.addMapToJsonObject(getFieldsFromCard(card, fields), jobj);

        return jobj;
    }
}
