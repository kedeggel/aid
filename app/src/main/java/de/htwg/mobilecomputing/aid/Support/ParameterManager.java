package de.htwg.mobilecomputing.aid.Support;

import android.content.Context;
import android.content.SharedPreferences;

public class ParameterManager {
    private SharedPreferences sharedPreferences;

    private static final String TOKEN = "device_token";
    private static final String REGISTRATION = "registered";

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences("de.htwg.mobilecomputing.aid", Context.MODE_PRIVATE);
    }

    public static String getToken(Context context) {
        return getPreferences(context).getString(TOKEN, null);
    }

    public static void setToken(Context context, String input) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putString(TOKEN, input);
        editor.apply();
    }

    public static boolean isRegistered(Context context) {
        return getPreferences(context).getBoolean(REGISTRATION, false);
    }

    public static void setRegistered(Context context, boolean input) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(REGISTRATION, input);
        editor.apply();
    }
}
