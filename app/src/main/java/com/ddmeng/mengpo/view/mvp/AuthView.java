package com.ddmeng.mengpo.view.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public interface AuthView {
    Activity getActivity();

    void onAuthSuccess();

    void onAuthFailed(Bundle bundle);

    void onAuthException(Exception e);

    void onAuthCanceled();
}
