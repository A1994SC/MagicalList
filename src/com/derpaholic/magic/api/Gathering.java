package com.derpaholic.magic.api;

import com.derpaholic.magic.Constants;
import com.derpaholic.magic.Debug;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.HashMap;


public class Gathering {

    private Gathering() {
    }

    public static HashMap<String, String> getFieldsFromCard(String card, String arg, String... fields) {
        if(!Utility.isRegexValid("[0-9]+", card))
            return null;

        HashMap<String, String> list = new HashMap<>();

        JsonObject json = Utility.getJsonFromURL(Constants.GATHER_CARD + card);

        System.out.println(Constants.GATHER_CARD + card);

        try {
            list.put(arg, json.get("cards").getAsJsonObject().get(arg).getAsString());
        } catch(Exception e) {
            list.put(arg, Constants.NO_DATA);
        }

        for(String field : fields) {
            try {
                list.put(field, json.get("cards").getAsJsonObject().get(field).getAsString());
            } catch(Exception e) {
                list.put(field, Constants.NO_DATA);
            }
            if(Debug.isDebug())
                System.out.println(list.get(field));
        }

        return list;
    }

    public static JsonObject getFieldsFromCards(JsonObject jobj, String card, String arg, String... fields) {
        if(jobj == null) {
            jobj = new JsonObject();
            jobj.add(Constants.CARDS_ID, new JsonArray());
        } else if(jobj.get(Constants.CARDS_ID) == null)
            jobj.add(Constants.CARDS_ID, new JsonArray());

        HashMap<String, String> t = getFieldsFromCard(card, arg, fields);

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
                jobj.getAsJsonArray(Constants.MID).add(hold);
            } catch(Exception e) {
                System.err.println(card);
                e.printStackTrace();
            }

        }

        return jobj;
    }
}
