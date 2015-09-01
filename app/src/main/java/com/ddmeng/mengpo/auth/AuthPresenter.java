package com.ddmeng.mengpo.auth;

import android.content.Intent;
import android.os.Bundle;

import com.ddmeng.mengpo.utils.LogUtils;
import com.ddmeng.mengpo.utils.PrefUtils;
import com.ddmeng.mengpo.view.mvp.AuthView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class AuthPresenter implements AuthInteractor.AuthorizeCallback {

    public static final String LOG_TAG = "Auth";

    private AuthView mView;
    private AuthInteractor mInteractor;

    private Oauth2AccessToken mAccessToken;


    public AuthPresenter(AuthView view) {
        this.mView = view;
        mInteractor = new AuthInteractor();
    }

    public void authorize() {
        LogUtils.i(LOG_TAG, "presenter: --authorize--");
        mInteractor.authorize(mView.getActivity(), this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.i(LOG_TAG, "presenter: --onActivityResult-- requestCode: " + requestCode + ", resultCode: " + resultCode);
        mInteractor.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onComplete(Bundle bundle) {
        LogUtils.i(LOG_TAG, "onComplete");
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
        if (mAccessToken.isSessionValid()) {

            // 保存 Token 到 SharedPreferences
            PrefUtils.saveAccessToken(mView.getActivity(), mAccessToken);

            mView.onAuthSuccess();
        } else {
            // 以下几种情况，您会收到 Code：
            // 1. 当您未在平台上注册的应用程序的包名与签名时；
            // 2. 当您注册的应用程序包名与签名不正确时；
            // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
            mView.onAuthFailed(bundle);
        }
    }

    @Override
    public void onException(Exception e) {
        mView.onAuthException(e);
    }

    @Override
    public void onCanceled() {
        mView.onAuthCanceled();

    }
}
