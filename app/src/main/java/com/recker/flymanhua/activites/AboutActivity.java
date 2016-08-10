package com.recker.flymanhua.activites;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.recker.flymanhua.R;
import com.recker.flymanhua.base.BaseActivity;

import java.net.HttpURLConnection;

import butterknife.Bind;

/**
 * Created by recker on 16/7/13.
 */
public class AboutActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected void init() {
        setupToolbar();
    }

    /**
     * 设置toolbar
     */
    private void setupToolbar() {
        mToolbar.setTitle("关于我们");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
