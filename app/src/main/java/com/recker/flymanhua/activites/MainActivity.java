package com.recker.flymanhua.activites;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.recker.flymanhua.R;
import com.recker.flymanhua.fragments.ClassifyFragment;
import com.recker.flymanhua.fragments.MeFragment;
import com.recker.flymanhua.fragments.NewFragment;
import com.recker.flymanhua.utils.HttpRequest;
import com.recker.flymanhua.utils.HttpUrl;
import com.umeng.analytics.MobclickAgent;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.viewpager) ViewPager mViewPager;
    @Bind(R.id.tab_container) LinearLayout mTabContainer;
    @Bind(R.id.tab_new) LinearLayout mTabNew;
    @Bind(R.id.tab_classify) LinearLayout mTabClassify;
    @Bind(R.id.tab_me) LinearLayout mTabMe;

    @Bind(R.id.iv_new) ImageView mIvNew;
    @Bind(R.id.tv_new) TextView mTvNew;
    @Bind(R.id.iv_classify) ImageView mIvlassify;
    @Bind(R.id.tv_classify) TextView mTvClassify;
    @Bind(R.id.iv_me) ImageView mIvMe;
    @Bind(R.id.tv_me) TextView mTvMe;

    private MenuItem mMenuRecord;
    private MenuItem mMenuSearch;

    private List<Fragment> mFragments;
    private NewFragment mNewFragment;
    private ClassifyFragment mClassifyFragment;
    private MeFragment mMeFragment;

    private int versionCode = 0;//版本号
    private String mVerSion;//更新版本
    private String mUpdateInfo;//更新信息
    private String mUpdateUrl;//更新链接
    private View mUpdateView;
    private Dialog mUpdateDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mToolbar.setTitle("最新漫画");
        setSupportActionBar(mToolbar);
        setupViewPager();
        mViewPager.setCurrentItem(0);
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mMenuRecord = menu.findItem(R.id.menu_record);
        mMenuSearch = menu.findItem(R.id.menu_search);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_record) {
            Intent intent = new Intent(this, RecordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);
        }
        if (item.getItemId() == R.id.menu_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置ViewPager
     */
    private void setupViewPager() {
        addFragments();

        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        };

        ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(listener);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mViewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void pageSelected(int position) {
        clearTabTextAndImageColor();

        if (position == 2) {
            mMenuRecord.setVisible(false);
            mMenuSearch.setVisible(false);
        } else {
            mMenuRecord.setVisible(true);
            mMenuSearch.setVisible(true);
        }

        switch (position) {
            case 0:
                mToolbar.setTitle("最新漫画");
                mTvNew.setTextColor(getResources().getColor(R.color.colorPrimary));
                mIvNew.setImageResource(R.drawable.manhua_selected);
                break;
            case 1:
                mToolbar.setTitle("分类");
                mTvClassify.setTextColor(getResources().getColor(R.color.colorPrimary));
                mIvlassify.setImageResource(R.drawable.classify_selected);
                break;
            case 2:
                mToolbar.setTitle("我的");
                mTvMe.setTextColor(getResources().getColor(R.color.colorPrimary));
                mIvMe.setImageResource(R.drawable.me_selected);
                break;
            default:
                break;
        }
    }

    /**
     * 添加Fragment
     */
    private void addFragments() {
        mFragments = new ArrayList<>();

        mNewFragment = new NewFragment();
        mClassifyFragment = new ClassifyFragment();
        mMeFragment = new MeFragment();

        mFragments.add(mNewFragment);
        mFragments.add(mClassifyFragment);
        mFragments.add(mMeFragment);
    }

    /**
     * tab#最新漫画点击事件
     */
    @OnClick(R.id.tab_new) void setClickNew() {
        mViewPager.setCurrentItem(0);
    }

    /**
     * tab#漫画分类点击事件
     */
    @OnClick(R.id.tab_classify) void setClickClassify() {
        mViewPager.setCurrentItem(1);
    }

    /**
     * tab#我的点击事件
     */
    @OnClick(R.id.tab_me) void setClickMe() {
        mViewPager.setCurrentItem(2);
    }

    /**
     * 清除tab栏的文本和图片颜色
     */
    private void clearTabTextAndImageColor() {
        mTvNew.setTextColor(getResources().getColor(R.color.secondary_text));
        mTvClassify.setTextColor(getResources().getColor(R.color.secondary_text));
        mTvMe.setTextColor(getResources().getColor(R.color.secondary_text));

        mIvNew.setImageResource(R.drawable.manhua_unselected);
        mIvlassify.setImageResource(R.drawable.classify_unselectd);
        mIvMe.setImageResource(R.drawable.me_unselectd);
    }

    private void update() {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            versionCode = pi.versionCode;

            new UpdateAsyncTask().execute(HttpUrl.getInstance().getUpdateUrl());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class UpdateAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return HttpRequest.getInstance().GET(strings[0], null);
        }

        @Override
        protected void onPostExecute(String s) {
//            Log.d(MainActivity.class.getSimpleName(), s);

            try {
                JSONObject object = new JSONObject(s);
                int code = object.getInt("code");
                if (code == 200) {
                    object = object.getJSONObject("data");
                    if (object.getInt("code") > versionCode) {
                        mVerSion = object.getString("version");
                        mUpdateInfo = object.getString("update_log");
                        mUpdateUrl = object.getString("url");
                        showUpdateDialog();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        mUpdateView = LayoutInflater.from(this).inflate(R.layout.dialog_update, null);
        mUpdateDialog = new Dialog(this, R.style.Dialog);
        mUpdateDialog.setContentView(mUpdateView);

        TextView tvInfo = ButterKnife.findById(mUpdateView, R.id.content);
        tvInfo.setText(mUpdateInfo);
        Button btnNo = ButterKnife.findById(mUpdateView, R.id.btn_no);
        Button btnYes = ButterKnife.findById(mUpdateView, R.id.btn_yes);
        setupBtnNoClick(btnNo);
        setupBtnYesClick(btnYes);

        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = (int)(metric.widthPixels * 0.8);
        WindowManager.LayoutParams lp =  mUpdateDialog.getWindow().getAttributes();
        lp.width = width;
        mUpdateDialog.getWindow().setAttributes(lp);
        mUpdateDialog.show();
    }

    private void setupBtnNoClick(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUpdateDialog.dismiss();
            }
        });
    }

    private void setupBtnYesClick(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(mUpdateUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                mUpdateDialog.dismiss();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
