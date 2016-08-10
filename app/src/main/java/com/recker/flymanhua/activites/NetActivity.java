package com.recker.flymanhua.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;


import com.recker.flymanhua.R;
import com.recker.flymanhua.base.BaseActivity;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by recker on 16/3/21.
 */
public class NetActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.webview) WebView mWebView;
    @Bind(R.id.progressbar) ProgressBar mProgressBar;

    private String mName;
    private String mUrl;

    private int mId = 0;
    private String mDimensionName;
    private String mAuthor;
    private boolean mIsRecord = false;//是否是记录

    private float mFirstDownPosition = 0;//第一次按压位置
    private boolean isToolbarShow = true;//toolbar是否显示

    @Override
    protected int getLayoutId() {
        return R.layout.activity_net;
    }

    @Override
    protected void init() {
        mUrl = getIntent().getStringExtra("url");
        mName = getIntent().getStringExtra("name");
        mIsRecord = getIntent().getBooleanExtra("record", false);
        mId = getIntent().getIntExtra("id", 0);
        mDimensionName = getIntent().getStringExtra("dimension_title");
        mAuthor = getIntent().getStringExtra("author");

        setToolbar();
        setWebView();
        setupWebViewTouchEvent();
    }


    private void setToolbar() {
        mToolbar.setTitle(mName);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setWebView() {
        //支持javascript
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setProgress(100);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_net, menu);
        if (!mIsRecord || mId == 0)
            menu.findItem(R.id.menu_grid).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.menu_grid) {
            Intent intent = new Intent(this, SortActivity.class);
            intent.putExtra("id", mId);
            intent.putExtra("title", mDimensionName);
            intent.putExtra("author", mAuthor);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);

        }

        return super.onOptionsItemSelected(item);
    }

    private void setupWebViewTouchEvent() {
        mWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mFirstDownPosition = event.getRawY();
                }

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float y = event.getRawY();
                    if (y - mFirstDownPosition > mToolbar.getHeight() && !isToolbarShow) {
                        //下滑
                        showToolbar();
                    } else if (y - mFirstDownPosition < -mToolbar.getHeight() && isToolbarShow) {
                        //上滑
                        hideToolbar();
                    }
                }
                return false;
            }
        });
    }

    private void showToolbar() {
        //为组件开始动画
        mToolbar.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator());
        mWebView.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator());
        if (mProgressBar.isShown()) {//判断ProgressBar是否存在
            mProgressBar.animate().translationY(0).setInterpolator(new AccelerateDecelerateInterpolator());
        }

        final ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mWebView.getLayoutParams();
        lp.height = mWebView.getHeight()-mToolbar.getHeight();
        mWebView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mWebView.setLayoutParams(lp);
            }
        }, 300);

        isToolbarShow = true;
    }

    private void hideToolbar() {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mWebView.getLayoutParams();
        lp.height = mWebView.getHeight()+mToolbar.getHeight();
        mWebView.setLayoutParams(lp);

        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator());
        mWebView.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateDecelerateInterpolator());
        if (mProgressBar.isShown()) {
            mProgressBar.animate().translationY(-mToolbar.getHeight())
                    .setInterpolator(new AccelerateDecelerateInterpolator());
        }

        isToolbarShow = false;
    }


    private void debug(String str) {
        Log.d(NetActivity.class.getSimpleName(), str);
    }

}
