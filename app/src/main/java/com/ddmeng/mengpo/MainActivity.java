package com.ddmeng.mengpo;

import android.app.FragmentTransaction;
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

import com.ddmeng.mengpo.fragments.MainContentListFragment;
import com.ddmeng.mengpo.utils.Constants;
import com.ddmeng.mengpo.utils.LogUtils;
import com.ddmeng.mengpo.utils.PrefUtils;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity implements MainContentListFragment.ContentListCallback {
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
    private SsoHandler mSsoHandler;
    private Oauth2AccessToken mAccessToken;
    private WeiboAuthListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initToolbar();
        initDrawerMenu();
        showMainListFragment();

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
        // 创建微博实例
        //mWeiboAuth = new WeiboAuth(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        if (null == mSsoHandler) {
            AuthInfo authInfo = new AuthInfo(this, Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
            mSsoHandler = new SsoHandler(MainActivity.this, authInfo);
            initAuthListener();
        }

        LogUtils.i(LOG_TAG, "--authorize--");
        mSsoHandler.authorize(mAuthListener);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mSsoHandler) {
            LogUtils.i(LOG_TAG, "authorizeCallBack, data: " + data);
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void initAuthListener() {
        mAuthListener = new WeiboAuthListener() {
            @Override
            public void onComplete(Bundle bundle) {
                LogUtils.i(LOG_TAG, "onComplete");
                // 从 Bundle 中解析 Token
                mAccessToken = Oauth2AccessToken.parseAccessToken(bundle);
                //从这里获取用户输入的 电话号码信息
                String phoneNum = mAccessToken.getPhoneNum();
                if (mAccessToken.isSessionValid()) {

                    // 保存 Token 到 SharedPreferences
                    PrefUtils.saveAccessToken(MainActivity.this, mAccessToken);
                    Toast.makeText(MainActivity.this,
                            R.string.account_auth_success, Toast.LENGTH_SHORT).show();
                } else {
                    // 以下几种情况，您会收到 Code：
                    // 1. 当您未在平台上注册的应用程序的包名与签名时；
                    // 2. 当您注册的应用程序包名与签名不正确时；
                    // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                    String code = bundle.getString("code");
                    String message = getString(R.string.account_auth_failed);
                    if (!TextUtils.isEmpty(code)) {
                        message = message + "\nObtained the code: " + code;
                    }
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onWeiboException(WeiboException e) {
                LogUtils.i(LOG_TAG, "onWeiboException: " + e);
                Toast.makeText(MainActivity.this,
                        "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                LogUtils.i(LOG_TAG, "onCancel");
                Toast.makeText(MainActivity.this,
                        R.string.account_auth_canceled, Toast.LENGTH_LONG).show();
            }
        };
    }
}
