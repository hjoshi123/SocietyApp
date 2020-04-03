package com.hacks.societyapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

class CookieUtils {
    static HashSet<String> getCookies(Context context) {
        SharedPreferences mcpPreferences = context.getSharedPreferences("cookie",
                Context.MODE_PRIVATE);
        return (HashSet<String>) mcpPreferences.getStringSet("cookies", new HashSet<String>());
    }

    static boolean setCookies(Context context, HashSet<String> cookies) {
        SharedPreferences mcpPreferences = context.getSharedPreferences("cookie",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mcpPreferences.edit();
        return editor.putStringSet("cookies", cookies).commit();
    }
}
