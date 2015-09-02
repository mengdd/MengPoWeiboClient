package com.ddmeng.mengpo.user;

import android.text.TextUtils;

import com.ddmeng.mengpo.utils.LogUtils;
import com.ddmeng.mengpo.utils.TokenUtils;
import com.ddmeng.mengpo.view.mvp.UserInfoView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.models.User;

public class UserInfoPresenter implements UserInfoInteractor.Callback {
    public static final String LOG_TAG = "User";
    private UserInfoView mView;
    private UserInfoInteractor mInteractor;

    public UserInfoPresenter(UserInfoView mView) {
        this.mView = mView;
        mInteractor = new UserInfoInteractor();
    }

    public void getUserInfo() {
        LogUtils.i(LOG_TAG, "presenter: getUserInfo");
        Oauth2AccessToken token = TokenUtils.getToken(mView.getContext());
        if (token.isSessionValid()) {
            mInteractor.fetchUserInfo(mView.getContext(), token, this);
        } else {
            LogUtils.e(LOG_TAG, "token session is invalid!");
        }

    }

    @Override
    public void onFetched(String response) {
        LogUtils.i(LOG_TAG, "presenter: getUserInfo");
        if (!TextUtils.isEmpty(response)) {
            LogUtils.i(LOG_TAG, "response: " + response);
            // 调用 User#parse 将JSON串解析成User对象
            User user = User.parse(response);
            if (user != null) {
                mView.showUserInfo(user);
            } else {
                mView.showFailedInfo(response);
            }
        } else {
            LogUtils.e(LOG_TAG, "response is empty");
        }
    }

    @Override
    public void onException(Exception e) {
        LogUtils.e(LOG_TAG, "get user info occur exception: " + e);
    }
}
