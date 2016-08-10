package com.recker.flymanhua.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.recker.flymanhua.R;
import com.recker.flymanhua.activites.NetActivity;
import com.recker.flymanhua.adapters.NewAdapter;
import com.recker.flymanhua.base.BaseFragment;
import com.recker.flymanhua.datas.NewData;
import com.recker.flymanhua.datas.RecortSort;
import com.recker.flymanhua.db.DimensionDAOImpl;
import com.recker.flymanhua.utils.HttpRequest;
import com.recker.flymanhua.utils.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by recker on 16/7/9.
 *
 * 最新漫画
 */
public class NewFragment extends BaseFragment implements NewAdapter.OnItemClickListener {

    @Bind(R.id.recyclerview) RecyclerView mRecylerView;
    @Bind(R.id.progressbar) ProgressBar mProgressBar;

    private List<NewData> listDatas;
    private NewAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_new;
    }

    @Override
    protected void init() {
        new NewAsyncTask().execute();
    }

    private class NewAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String url = HttpUrl.getInstance().getNewUrl();
//            Map<String, String> params = HttpUrl.getInstance().getNewParams();

            return HttpRequest.getInstance().POST(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

//            debug(s);
            getJsonData(s);
        }
    }

    /**
     * 解析Json数据
     * @param s
     */
    private void getJsonData(String s) {
        try {
            JSONObject object = new JSONObject(s);
            boolean isError = object.optBoolean("IsError");

            if (!isError) {
                listDatas = new ArrayList<>();
                object = object.optJSONObject("Return");
                JSONArray array = object.optJSONArray("List");
                for (int i = 0; i < array.length(); i++) {
                    NewData data = new NewData();
                    object = array.optJSONObject(i);
                    data.setId(object.optInt("Id"));
                    data.setSubTitle(object.optString("Title") + "");
                    data.setSort(object.optString("Sort") + "");
                    data.setImgUrl(object.optString("FrontCover") + "");
                    data.setDate(object.optString("RefreshTimeStr") + "");

                    JSONObject object1 = object.optJSONObject("Book");
                    data.setTitle(object1.optString("Title")+"");
                    data.setIntroduce(object1.optString("Explain")+"");

                    listDatas.add(data);
                }

                setupRecyclerView();
                mProgressBar.setVisibility(View.GONE);
                mRecylerView.setVisibility(View.VISIBLE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setupRecyclerView() {
//        addBanner();
        mAdapter = new NewAdapter(getActivity(), listDatas);
        mAdapter.setOnItemClickListener(this);
        mRecylerView.setAdapter(mAdapter);
        mRecylerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecylerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    private void addBanner() {
        NewData data = new NewData();
        data.setBanner(true);
        listDatas.add(data);
    }

    @Override
    public void onItemClick(View view, int position) {
        NewData data = listDatas.get(position);
        Intent intent = new Intent(getActivity(), NetActivity.class);
        intent.putExtra("url", HttpUrl.getInstance().getNetWorkUrl(data.getId()+""));
        intent.putExtra("name", data.getTitle());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);

        addLocalDatabase(data);
    }

    private void addLocalDatabase(NewData newData) {
        RecortSort data = new RecortSort();

        data.setDimensionTitle(newData.getTitle());
        data.setSortId(newData.getId());
        data.setSort(Integer.parseInt(newData.getSort()));
        data.setSortTile(newData.getTitle());
        data.setNetImg(newData.getImgUrl());
        data.setDate(System.currentTimeMillis()+"");

        DimensionDAOImpl.getInstance(getActivity()).insertData(data);
    }


    private void debug(String string) {
        Log.d(NewFragment.class.getSimpleName(), string);
    }
}
