package com.recker.flymanhua.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.recker.flymanhua.R;
import com.recker.flymanhua.activites.SortActivity;
import com.recker.flymanhua.adapters.ClassifyAdapter;
import com.recker.flymanhua.base.BaseFragment;
import com.recker.flymanhua.datas.ClassifyData;
import com.recker.flymanhua.utils.HttpRequest;
import com.recker.flymanhua.utils.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by recker on 16/7/10.
 *
 * 漫画分类#内容
 */
public class ClassifyContentFragment extends BaseFragment implements ClassifyAdapter.OnItemClickListener {

    @Bind(R.id.refresh) SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recyclerview) RecyclerView mRecyclerView;
    @Bind(R.id.progressbar) ProgressBar mProgressBar;

    private List<ClassifyData> listDatas = new ArrayList<>();
    private ClassifyAdapter mAdapter;
    private ClassifyData mMoreData;//加载更多数据

    private boolean mIsMoreLoading = false;//是否正在加载更多
    private boolean mIsLoadFinished = false;//是否加载完成
    private int mPage = 0;
    private int mType = 0;//漫画类型 4为热血漫画 2为国产漫画 3为鼠绘漫画

    public void setType(int type) {
        mType = type;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classify_content;
    }

    @Override
    protected void init() {
        setupRefreshColor();
        setupRecyclerView();
        new DimensionAsyncTask().execute();
    }

    /**
     * 设置刷新控件颜色
     */
    private void setupRefreshColor() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
    }

    /**
     * 设置列表数据
     */
    private void setupRecyclerView() {
        mAdapter = new ClassifyAdapter(getActivity(), listDatas);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        addRefreshLisener();
        addRecyclerViewListener();
    }

    @Override
    public void onItemClick(View view, int position) {
        ClassifyData data = listDatas.get(position);
        Intent intent = new Intent(getActivity(), SortActivity.class);
        intent.putExtra("id", data.getId());
        intent.putExtra("title", data.getTitle());
        intent.putExtra("author", data.getAuthor());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);
    }

    /**
     * 异步请求数据
     */
    private class DimensionAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url = HttpUrl.getInstance().getTypeUrl(mType, mPage);
            return HttpRequest.getInstance().POST(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            analyzeJsonDatas(s);
        }

    }

    /**
     * 解析JSON数据
     * @param s
     */
    private void analyzeJsonDatas(String s) {
        mProgressBar.setVisibility(View.GONE);
        mSwipeRefreshLayout.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        if (mIsMoreLoading) {
            listDatas.remove(mMoreData);
        }
        mIsMoreLoading = false;

        try {
            JSONObject object = new JSONObject(s);
            object = object.getJSONObject("Return");
            JSONArray array = object.getJSONArray("List");

            for (int i = 0; i < array.length(); i++) {
                object = array.getJSONObject(i);
                ClassifyData data = new ClassifyData();

                data.setId(object.getInt("Id"));
                data.setTitle(object.getString("Title"));
                data.setSubtitle(object.getString("Explain"));
                data.setDate(object.getString("RefreshTimeStr"));
                data.setAuthor(object.getString("Author"));
                data.setImgUrl(object.getString("FrontCover"));

                JSONObject object1 = object.getJSONObject("LastChapter");
                data.setSort(object1.getInt("Sort"));

                listDatas.add(data);
            }
            mAdapter.notifyDataSetChanged();
            if (array.length() <= 0) {
                mIsLoadFinished = true;
                toast("没有更多漫画啦...");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加刷新监听
     */
    private void addRefreshLisener() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 0;
                listDatas.clear();
                mIsLoadFinished = false;
                new DimensionAsyncTask().execute();
            }
        });
    }

    /**
     * 为RecyclerView添加滑动监听
     */
    private void addRecyclerViewListener() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager lm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                int onLastPosition = lm.findLastVisibleItemPosition();

                if (onLastPosition+ 1 == mAdapter.getItemCount() && dy > 0 && !mIsMoreLoading) {
                    mIsMoreLoading = true;
                    if (!mIsLoadFinished) {
                        mPage++;
                        if (mMoreData == null)
                            mMoreData = new ClassifyData();
                        mMoreData.setLoading(true);
                        listDatas.add(mMoreData);
                        new DimensionAsyncTask().execute();
                    }
                }
            }
        });
    }

    private void toast(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
    }

    private void debug(String str) {
        Log.d(ClassifyContentFragment.class.getSimpleName(), str);
    }

}
