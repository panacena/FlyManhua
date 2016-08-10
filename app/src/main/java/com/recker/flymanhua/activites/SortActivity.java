package com.recker.flymanhua.activites;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.recker.flymanhua.R;
import com.recker.flymanhua.adapters.SortAdapter;
import com.recker.flymanhua.base.BaseActivity;
import com.recker.flymanhua.cache.ImageLoader;
import com.recker.flymanhua.datas.RecortSort;
import com.recker.flymanhua.datas.SortData;
import com.recker.flymanhua.db.DimensionDAOImpl;
import com.recker.flymanhua.utils.HttpRequest;
import com.recker.flymanhua.utils.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by recker on 16/7/13.
 *
 * 漫画话数
 */
public class SortActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.header_bg) ImageView mIvHeaderBg;
    @Bind(R.id.scrollView) NestedScrollView mScrollView;
    @Bind(R.id.gridview) GridView mGridView;

    private List<SortData> listDatas = new ArrayList<>();
    private SortAdapter mAdapter;

    private int mId = 0;
    private String mName;
    private String mAuthor;

    private String mHeaderBgImgUrl;
    private boolean isLoading = false;
    private int mPage = 0;
    private int mAddClearItem = 0;//添加了几个空白item

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sort;
    }

    @Override
    protected void init() {
        mId = getIntent().getIntExtra("id", 0);
        mName = getIntent().getStringExtra("title");
        mAuthor = getIntent().getStringExtra("author");

        setupToolbar();
        setupGridView();
        setupScrollViewTouch();

        new SortAsyncTask().execute();
    }

    private void setupToolbar() {
        mToolbarLayout.setExpandedTitleColor(0xFFFFFFFF);
        mToolbarLayout.setCollapsedTitleTextColor(0xFFFFFFFF);

        mToolbar.setTitle(mName);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupGridView() {
        mAdapter = new SortAdapter(this, listDatas);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private class SortAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url = HttpUrl.getInstance().getSortUrl(mId, mPage);
            return HttpRequest.getInstance().POST(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            getJsonDatas(s);
        }
    }

    private void getJsonDatas(String s) {
        try {
            JSONObject object = new JSONObject(s);
            object = object.getJSONObject("Return");
            JSONArray array = object.getJSONArray("List");

            object = object.getJSONObject("ParentItem");
            mHeaderBgImgUrl = object.getString("FrontCover");
//            Glide.with(this).load(mHeaderBgImgUrl).into(mIvHeaderBg);
            ImageLoader.getInstance().disPlay(mIvHeaderBg, mHeaderBgImgUrl);

            for (int i = 0; i < array.length(); i++) {
                SortData sort = new SortData();
                object = array.getJSONObject(i);

                sort.setId(object.getInt("Id"));
                sort.setTitle(object.getString("Title"));
                sort.setSort(object.getInt("Sort"));
                sort.setDate(object.getString("RefreshTimeStr"));
                sort.setImgUrl(object.getString("FrontCover"));

                listDatas.add(sort);
            }
            if (listDatas.size() % 3 == 2) {
                SortData sort = new SortData();
                listDatas.add(sort);
                mAddClearItem = 1;
            } else if (listDatas.size() % 3 == 1) {
                SortData sort = new SortData();
                listDatas.add(sort);
                SortData sort1 = new SortData();
                listDatas.add(sort1);
                mAddClearItem = 2;
            }
            mAdapter.notifyDataSetChanged();
            isLoading = false;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setupScrollViewTouch() {
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    int scrollY = view.getScrollY();
                    int height = view.getHeight();
                    int scrollViewMeasuredHeight = mScrollView.getChildAt(0).getMeasuredHeight();

                    if ((scrollY + height) == scrollViewMeasuredHeight) {
//                        debug("滑动到了底部。。。");
                        onLoadMore();
                    }

                }

                return false;
            }
        });
    }


    /**
     * 加载更多
     */
    private void onLoadMore() {
        if (!isLoading) {
            isLoading = true;
            mPage++;
            new SortAsyncTask().execute();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        SortData sort = listDatas.get(i);
        if (sort.getTitle() == null)
            return;

        Intent intent = new Intent(this, NetActivity.class);
        intent.putExtra("url",  HttpUrl.getInstance().getNetWorkUrl(sort.getId()+""));
        intent.putExtra("name", sort.getTitle()+"");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);

        addLocalDatabase(sort);
    }

    /**
     * 添加本地数据
     */
    private void addLocalDatabase(SortData sort) {
        RecortSort data = new RecortSort();

        data.setDimensionId(mId);
        data.setDimensionTitle(mName);
        data.setAuthor(mAuthor);
        data.setSortId(sort.getId());
        data.setSort(sort.getSort());
        data.setSortTile(sort.getTitle());
        data.setNetImg(sort.getImgUrl());
        data.setDate(System.currentTimeMillis()+"");

        DimensionDAOImpl.getInstance(this).insertData(data);
    }

    private void debug(String str) {
        Log.d(SortActivity.class.getSimpleName(), str);
    }
}
