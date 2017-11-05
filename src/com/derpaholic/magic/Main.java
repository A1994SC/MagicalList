package com.derpaholic.magic;

import com.derpaholic.magic.api.Gathering;
import com.derpaholic.magic.api.Scryfall;
import com.derpaholic.magic.misc.Constants;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Main {

    private static final String url = "https://scryfall.com/saved/share.text?id=psoi:108/shm:167/m14:68";

    public static void main(String... args) {
        ArrayList<String> t = Scryfall.getListFromURL(url);

        JsonObject json = new JsonObject();

        if(t != null)
            for(String temp : t)
                Scryfall.getFieldsFromCard(json, temp, "usd", "name");

        JsonArray ary = json.getAsJsonArray(Constants.CARDS_ID);

        for(int i = 0; i < ary.size(); i++) {
            Gathering.getFieldsFromCards(json, ary.get(i).getAsJsonObject().get(Constants.MID).getAsString(), "printings");
        }

        System.out.println(Constants.GSON_PRETTY.toJson(json));
    }
}
