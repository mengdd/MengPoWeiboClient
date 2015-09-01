package com.ddmeng.mengpo.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ddmeng.mengpo.utils.Constants;
import com.ddmeng.mengpo.utils.LogUtils;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class AuthInteractor {
    public static final String LOG_TAG = "Auth";

    private SsoHandler mSsoHandler;

    public void authorize(Activity activity, final AuthorizeCallback callback) {

        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        if (null == mSsoHandler) {
            AuthInfo authInfo = new AuthInfo(activity, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
            mSsoHandler = new SsoHandler(activity, authInfo);
        }

        LogUtils.i(LOG_TAG, "--authorize--");
        WeiboAuthListener listener = new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                callback.onComplete(bundle);
            }

            @Override
            public void onWeiboException(WeiboException e) {
                callback.onException(e);
            }

            @Override
            public void onCancel() {
                callback.onCanceled();
            }
        };

        mSsoHandler.authorize(listener);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mSsoHandler) {
            LogUtils.i(LOG_TAG, "authorizeCallBack, data: " + data);
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    public interface AuthorizeCallback {
        void onComplete(Bundle bundle);

        void onException(Exception e);

        void onCanceled();
    }
}
