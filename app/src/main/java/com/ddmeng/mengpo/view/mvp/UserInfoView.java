package com.ddmeng.mengpo.view.mvp;

import android.content.Context;

import com.sina.weibo.sdk.openapi.models.User;

public interface UserInfoView {

    Context getContext();

    void showUserInfo(User user);

    void showFailedInfo(String info);
}
