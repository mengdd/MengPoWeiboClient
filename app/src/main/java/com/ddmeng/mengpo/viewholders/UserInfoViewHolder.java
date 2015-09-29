package com.ddmeng.mengpo.viewholders;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddmeng.mengpo.R;
import com.ddmeng.mengpo.utils.LogUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
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
    @InjectView(R.id.user_description)
    TextView userDescription;
    @InjectView(R.id.statuses_count)
    TextView statusesCount;
    @InjectView(R.id.friends_count)
    TextView friendsCount;
    @InjectView(R.id.followers_count)
    TextView followersCount;

    ImageLoader mImageLoader;

    public UserInfoViewHolder(View itemView, Callback callback) {
        super(itemView);
        this.callback = callback;
        ButterKnife.inject(this, itemView);
        mImageLoader = ImageLoader.getInstance();
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
        userDescription.setText(user.description);
        mImageLoader.displayImage(user.profile_image_url, userIcon);

        Context context = itemView.getContext();
        statusesCount.setText(context.getString(R.string.statuses_count, user.statuses_count));
        friendsCount.setText(context.getString(R.string.friends_count, user.friends_count));
        followersCount.setText(context.getString(R.string.followers_count, user.followers_count));
    }


    public void showLoginButton() {
        userIcon.setVisibility(View.GONE);
        userName.setVisibility(View.GONE);
    }


    public interface Callback {
        void onLoginButtonClicked();
    }
}
