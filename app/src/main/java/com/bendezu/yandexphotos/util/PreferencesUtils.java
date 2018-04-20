package com.bendezu.yandexphotos.util;

import android.content.Context;

import com.bendezu.yandexphotos.R;

public class PreferencesUtils {

    private static final String TOKEN_PREF_KEY = "token";

    public static String getAccessToken(Context context) {
        return context.getSharedPreferences(TOKEN_PREF_KEY,Context.MODE_PRIVATE).
                getString(context.getString(R.string.saved_access_token_key), null);
    }

    public static void setAccessToken(String token, Context context){
        context.getSharedPreferences(TOKEN_PREF_KEY, Context.MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.saved_access_token_key), token)
                .apply();
    }
}
