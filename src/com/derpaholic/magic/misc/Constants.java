package com.derpaholic.magic.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Constants {

    public final static String NO_DATA = "¯\\_(ツ)_/¯";


    public static final String GATHER_API = "https://api.magicthegathering.io/v1/";
    public static final String GATHER_CARD = GATHER_API + "cards/";
    public static final String GATHER_DEFAULT = "card";


    public static final String SCRYFALL_URL = "https://api.scryfall.com/";
    public static final String SCRYFALL_CARD = SCRYFALL_URL + "cards/";

    public static final String PRINTING_ID = "printings";


    public static final String MID = "multiverse_id";
    public static final String MID_GATH = "multiverseid";
    public static final String PROMO_REGEX = "p...:[0-9]+";
    public static final String CARDS_ID = "cards";


    public static final Gson GSON_PRETTY = new GsonBuilder().serializeNulls().disableHtmlEscaping().serializeSpecialFloatingPointValues().setPrettyPrinting().create();
    public static final Gson GSON = new GsonBuilder().serializeNulls().disableHtmlEscaping().serializeSpecialFloatingPointValues().create();
}
