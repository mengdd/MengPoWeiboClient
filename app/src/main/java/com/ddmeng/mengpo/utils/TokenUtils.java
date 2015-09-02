package com.ddmeng.mengpo.utils;

import android.content.Context;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class TokenUtils {

    public static boolean isTokenValid(Context context) {
        Oauth2AccessToken token = getToken(context);
        return token.isSessionValid();
    }

    public static Oauth2AccessToken getToken(Context context) {
        Oauth2AccessToken accessToken = PrefUtils.getAccessToken(context);
        return accessToken;
    }

    public static void saveToken(Context context, Oauth2AccessToken token) {
        PrefUtils.saveAccessToken(context, token);
    }
}
