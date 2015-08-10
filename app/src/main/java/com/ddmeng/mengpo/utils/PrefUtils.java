package com.ddmeng.mengpo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {
    private class PrefFile {
        //you can define different preferences file name here
        public static final String SETTINGS = "settings_preferences";
    }

    public class PrefKey {
        //you can define the preferences keys here
        public static final String USER_NAME = "user_name";
        public static final String APP_FIRST_LAUNCH = "app_first_launch";
    }


    //Provide get/set methods for every key


    public static void setPrefUserName(final Context context, String userName) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);//use default sharedPreferences here
        sp.edit().putString(PrefKey.USER_NAME, userName).commit();
        //commit() is sync
        //apply() is async
    }

    public static String getPrefUserName(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(PrefKey.USER_NAME, null);
    }

    public static boolean isAppFirstLaunched(final Context context) {
        SharedPreferences sp = context.getSharedPreferences(PrefFile.SETTINGS, Context.MODE_PRIVATE);
        return sp.getBoolean(PrefKey.APP_FIRST_LAUNCH, true);
    }

    public static void setAppFirstLaunched(final Context context, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PrefFile.SETTINGS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(PrefKey.APP_FIRST_LAUNCH, value).apply();
    }
}
