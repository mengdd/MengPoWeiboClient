package com.ddmeng.mengpo.view.mvp;

import android.app.Activity;
import android.os.Bundle;

public interface AuthView {

    void initAuthInfoView();

    Activity getActivity();

    void onAuthSuccess();

    void onAuthFailed(Bundle bundle);

    void onAuthException(Exception e);

    void onAuthCanceled();
}
