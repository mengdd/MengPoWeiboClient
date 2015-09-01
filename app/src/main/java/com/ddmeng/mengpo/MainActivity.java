package com.ddmeng.mengpo;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.ddmeng.mengpo.auth.AuthPresenter;
import com.ddmeng.mengpo.fragments.MainContentListFragment;
import com.ddmeng.mengpo.utils.Constants;
import com.ddmeng.mengpo.utils.LogUtils;
import com.ddmeng.mengpo.utils.PrefUtils;
import com.ddmeng.mengpo.view.mvp.AuthView;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity
        implements MainContentListFragment.ContentListCallback, AuthView {
    private static final String LOG_TAG = "MainActivity";

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawer_menu_list)
    ListView mDrawerList;
    @InjectView(R.id.content_container)
    FrameLayout mContentContainer;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;

    private AuthPresenter mAuthPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initToolbar();
        initDrawerMenu();
        showMainListFragment();

        mAuthPresenter = new AuthPresenter(this);

    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void initDrawerMenu() {
        String[] drawerMenuItems = getResources().getStringArray(R.array.drawer_menu_list);
        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_menu_list_item, drawerMenuItems));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "item click: " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_menu_open,  /* "open drawer" description */
                R.string.drawer_menu_closed  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void showMainListFragment() {
        MainContentListFragment mainContentListFragment = MainContentListFragment.newInstance("aaa", "bbb");
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content_container, mainContentListFragment, MainContentListFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public void refresh() {

    }

    @OnClick(R.id.login_button)
    void doLogin(View view) {
        mAuthPresenter.authorize();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mAuthPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onAuthSuccess() {
        Toast.makeText(MainActivity.this,
                R.string.account_auth_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthFailed(Bundle bundle) {
        String code = bundle.getString("code");
        String message = getString(R.string.account_auth_failed);
        if (!TextUtils.isEmpty(code)) {
            message = message + "\nObtained the code: " + code;
        }
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthException(Exception e) {
        LogUtils.i(LOG_TAG, "onWeiboException: " + e);
        Toast.makeText(MainActivity.this,
                "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onAuthCanceled() {
        LogUtils.i(LOG_TAG, "onCancel");
        Toast.makeText(MainActivity.this,
                R.string.account_auth_canceled, Toast.LENGTH_LONG).show();
    }
}
