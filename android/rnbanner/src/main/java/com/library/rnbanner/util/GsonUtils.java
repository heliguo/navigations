package com.library.rnbanner.util;

import com.google.gson.Gson;

public class GsonUtils {

    private static final Gson GSON = new Gson();

    public static Gson gson(){
        return GSON;
    }
}
