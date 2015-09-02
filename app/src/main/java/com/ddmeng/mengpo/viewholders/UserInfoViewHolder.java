package com.ddmeng.mengpo.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddmeng.mengpo.R;
import com.ddmeng.mengpo.utils.LogUtils;
import com.sina.weibo.sdk.openapi.models.User;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class UserInfoViewHolder extends BaseViewHolder<User> {

    public static final String LOG_TAG = "User";


    private Callback callback;
    @InjectView(R.id.login_button)
    TextView loginButton;
    @InjectView(R.id.user_icon)
    ImageView userIcon;
    @InjectView(R.id.user_name)
    TextView userName;

    public UserInfoViewHolder(View itemView, Callback callback) {
        super(itemView);
        this.callback = callback;
        ButterKnife.inject(this, itemView);
    }

    @OnClick(R.id.login_button)
    void onLoginButtonClicked() {
        callback.onLoginButtonClicked();
    }

    @Override
    public void populate(User user) {
        LogUtils.i(LOG_TAG, "populate user: " + user);

        loginButton.setVisibility(View.GONE);
        userIcon.setVisibility(View.VISIBLE);
        userName.setVisibility(View.VISIBLE);

        userName.setText(user.screen_name);
    }


    public void showLoginButton() {
        userIcon.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
    }


    public interface Callback {
        void onLoginButtonClicked();
    }
}
