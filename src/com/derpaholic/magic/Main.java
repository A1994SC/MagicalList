package com.derpaholic.magic;

import com.derpaholic.magic.api.Gathering;
import com.derpaholic.magic.api.Scryfall;
import com.derpaholic.magic.misc.Constants;
import com.derpaholic.magic.misc.Debug;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    private static final String url = "https://scryfall.com/saved/share?id=bfz:11/aer:74/ema:218/soi:270/c16:289/xln:220/c16:293/c17:213/zen:47/shm:258/c16:316/c15:54/c16:273/pca:128/ths:225/mma:193/ktk:216/c16:259/nph:144/dgm:85/m15:226/bfz:206/isd:235/mm3:35/gtc:154/gtc:231/rav:56/rtr:45/hou:40/gtc:189/m15:76/mm3:47/mma:65/emn:75/lrw:92/isd:236/bfz:55/psoi:108/shm:167/pca:97/ddi:8/nph:163/c16:27/c15:90/shm:159/c13:197/rav:83/pca:86/rav:60/rav:217/rav:105/shm:158/ktk:34/nph:42/pca:94/som:33/mma:63";

    public static void main(String... args) {
        ArrayList<String> t = Scryfall.getListFromURL(url);

        JsonObject json = new JsonObject();
        int i, j;

        if(Debug.isDebug())
            System.out.println("Talking to Scryfall");
        if(t != null)
            for(String temp : t)
                Scryfall.getFieldsFromCard(json, temp, "usd", "name");
        if(Debug.isDebug())
            System.out.println("Done talking to Scryfall");

        JsonArray ary = json.getAsJsonArray(Constants.CARDS_ID);

        if(Debug.isDebug())
            System.out.println("Talking to Gather");
        for(i = 0; i < ary.size(); i++) {
            Gathering.getFieldsFromCards(json, ary.get(i).getAsJsonObject().get(Constants.MID).getAsString(), "printings");
        }

        if(Debug.isDebug())
            System.out.println("Done talking to Gather");

        if(Debug.isDebug())
            System.out.println("Processing sets");
        JsonArray jAry = json.getAsJsonArray(Constants.CARDS_ID);
        HashMap<String, ArrayList<JsonObject>> setList = new HashMap<>();
        JsonArray temp;


        for(i = 0; i < jAry.size(); i++) {
            temp = jAry.get(i).getAsJsonObject().get(Constants.PRINTING_ID).getAsJsonArray();
            for(j = 0; j < temp.size(); j++) {
                if(setList.containsKey(temp.get(j).getAsString()))
                    setList.get(temp.get(j).getAsString()).add(jAry.get(i).getAsJsonObject());
                else {
                    setList.put(temp.get(j).getAsString(), new ArrayList<>());
                    setList.get(temp.get(j).getAsString()).add(jAry.get(i).getAsJsonObject());
                }
            }
        }

        if(Debug.isDebug())
            System.out.println("Done processing sets");

        for(String t1 : setList.keySet())
            System.out.println(t1);

//        for(String t1 : setList.keySet())
//            for(JsonObject t2: setList.get(t1))
//                System.out.println(t1 + "\t:\t" + Constants.GSON.toJson(t2));
    }
}
