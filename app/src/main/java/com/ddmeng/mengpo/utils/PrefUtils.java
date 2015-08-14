package com.ddmeng.mengpo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class PrefUtils {
    private class PrefName {
        public static final String ACCOUNT = "weibo_auth";
        public static final String SETTINGS = "settings_preferences";
    }

    public class PrefKey {

        public static final String KEY_UID = "uid";
        public static final String KEY_ACCESS_TOKEN = "access_token";
        public static final String KEY_EXPIRES_IN = "expires_in";
        public static final String KEY_REFRESH_TOKEN = "refresh_token";

        public static final String USER_NAME = "user_name";
        public static final String APP_FIRST_LAUNCH = "app_first_launch";
    }


    //Provide get/set methods for every key

    //AccessToken
    public static void saveAccessToken(final Context context, Oauth2AccessToken token) {
        SharedPreferences pref = context.getSharedPreferences(PrefName.ACCOUNT, Context.MODE_APPEND);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PrefKey.KEY_UID, token.getUid());
        editor.putString(PrefKey.KEY_ACCESS_TOKEN, token.getToken());
        editor.putString(PrefKey.KEY_REFRESH_TOKEN, token.getRefreshToken());
        editor.putLong(PrefKey.KEY_EXPIRES_IN, token.getExpiresTime());
        editor.commit();

    }

    public static Oauth2AccessToken getAccessToken(final Context context) {
        Oauth2AccessToken token = new Oauth2AccessToken();
        SharedPreferences pref = context.getSharedPreferences(PrefName.ACCOUNT, Context.MODE_APPEND);
        token.setUid(pref.getString(PrefKey.KEY_UID, ""));
        token.setToken(pref.getString(PrefKey.KEY_ACCESS_TOKEN, ""));
        token.setRefreshToken(pref.getString(PrefKey.KEY_REFRESH_TOKEN, ""));
        token.setExpiresTime(pref.getLong(PrefKey.KEY_EXPIRES_IN, 0));
        return token;
    }

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
        SharedPreferences sp = context.getSharedPreferences(PrefName.SETTINGS, Context.MODE_PRIVATE);
        return sp.getBoolean(PrefKey.APP_FIRST_LAUNCH, true);
    }

    public static void setAppFirstLaunched(final Context context, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PrefName.SETTINGS, Context.MODE_PRIVATE);
        sp.edit().putBoolean(PrefKey.APP_FIRST_LAUNCH, value).apply();
    }
}
