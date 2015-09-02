package com.ddmeng.mengpo.user;

import android.content.Context;

import com.ddmeng.mengpo.utils.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.UsersAPI;

public class UserInfoInteractor {


    public void fetchUserInfo(Context context, Oauth2AccessToken token, final Callback callback) {
        UsersAPI userAPI = new UsersAPI(context, Constants.APP_KEY, token);

        RequestListener listener = new RequestListener() {
            @Override
            public void onComplete(String response) {
                callback.onFetched(response);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                callback.onException(e);
            }

        };

        String uidString = token.getUid();
        userAPI.show(Long.parseLong(uidString), listener);

    }

    public interface Callback {
        void onFetched(String response);

        void onException(Exception e);
    }
}
