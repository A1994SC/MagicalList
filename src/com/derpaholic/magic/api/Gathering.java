package com.derpaholic.magic.api;

import com.derpaholic.magic.misc.Constants;
import com.derpaholic.magic.misc.Debug;
import com.derpaholic.magic.misc.Utility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;


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
                System.out.println();
                if(json.get(Constants.GATHER_DEFAULT).getAsJsonObject().get(field).isJsonArray())
                    list.put(field, Constants.GSON.toJson(json.getAsJsonObject(Constants.GATHER_DEFAULT).getAsJsonArray(field)));
                else
                    list.put(field, json.getAsJsonObject(Constants.GATHER_DEFAULT).get(field).getAsString());
            } catch(Exception e) {
                list.put(field, Constants.NO_DATA);
            }
            if(Debug.isDebug())
                System.out.println(list.get(field));
        }

        return list;
    }

    public static JsonObject getFieldsFromCards(JsonObject jobj, String card, String... fields) {
        if(jobj == null) {
            jobj = new JsonObject();
            jobj.add(Constants.CARDS_ID, new JsonArray());
        } else if(jobj.get(Constants.CARDS_ID) == null)
            jobj.add(Constants.CARDS_ID, new JsonArray());

        HashMap<String, String> t = getFieldsFromCard(card, fields);

        JsonObject obj = Utility.getObjectFromArray(Constants.MID, t.get(Constants.MID), jobj.get("cards").getAsJsonArray());
        if(obj != null) {
            try {
                for(String key : t.keySet())
                    obj.add(key, new JsonParser().parse(t.get(fields)));
            } catch(Exception e) {
                System.out.println(card);
            }
        } else {
            JsonObject hold = new JsonObject();
            try {
                for(String key : t.keySet())
                    hold.add(key, new JsonParser().parse(t.get(fields)));
                jobj.get("cards").getAsJsonArray().add(hold);
            } catch(Exception e) {
                System.err.println(card);
                e.printStackTrace();
            }

        }

        return jobj;
    }
}
