package com.example.service.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MixUtil {

    private final static SimpleDateFormat sTimeFormat;
    private final static Gson sGson;

    static {
        sTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.Z", Locale.getDefault());
        sGson = new GsonBuilder().setLenient().disableHtmlEscaping().create();
    }

    public MixUtil() {


    }

    public static SimpleDateFormat getTimeFormat() {
        return sTimeFormat;
    }

    public static Gson getGson() {
        return sGson;
    }
}
