package com.recker.flymanhua.activites;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.recker.flymanhua.R;
import com.recker.flymanhua.adapters.ClassifyAdapter;
import com.recker.flymanhua.adapters.SearchLocalAdapter;
import com.recker.flymanhua.base.BaseActivity;
import com.recker.flymanhua.datas.ClassifyData;
import com.recker.flymanhua.datas.SearchData;
import com.recker.flymanhua.db.DBSearchDAOImpl;
import com.recker.flymanhua.utils.HttpRequest;
import com.recker.flymanhua.utils.HttpUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by recker on 16/7/13.
 *
 * 查找
 */
public class SearchActivity extends BaseActivity implements
        ClassifyAdapter.OnItemClickListener, SearchLocalAdapter.OnRefreshDataListener{

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.et_search) EditText mEtSearch;
    @Bind(R.id.recyclerview) RecyclerView mRecylerView;
    @Bind(R.id.progressbar) ProgressBar mProgressBar;
    @Bind(R.id.listview) ListView mListView;

    private ClassifyAdapter mAdapter;
    private List<ClassifyData> listDatas = new ArrayList<>();

    private SearchLocalAdapter mLocalAdapter;
    private List<SearchData> mLocalListDtas;

    private String mSearchStr;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void init() {
        setupToolbar();
        setupListView();
        setupRecyclerView();
        setupEtActionListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    /**
     * 设置toolbar
     */
    private void setupToolbar() {
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupListView() {
        mLocalListDtas = DBSearchDAOImpl.getInstance(this).getAllData();
        mLocalAdapter = new SearchLocalAdapter(this, mLocalListDtas);
        mLocalAdapter.setOnRefreshDataListener(this);
        mListView.setAdapter(mLocalAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mEtSearch.setText(mLocalListDtas.get(i).getInfo());
                lodingSearch(true);
            }
        });
    }

    @Override
    public void onRefresh(int id) {
        DBSearchDAOImpl.getInstance(this).deletedOneData(id);
        mLocalListDtas.clear();
        mLocalListDtas.addAll(DBSearchDAOImpl.getInstance(this).getAllData());
        mLocalAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        mAdapter = new ClassifyAdapter(this, listDatas);
        mAdapter.setOnItemClickListener(this);
        mRecylerView.setAdapter(mAdapter);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);
    }

    @Override
    public void onItemClick(View view, int position) {
        ClassifyData data = listDatas.get(position);
        Intent intent = new Intent(this, SortActivity.class);
        intent.putExtra("id", data.getId());
        intent.putExtra("title", data.getTitle());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);
    }

    private void setupEtActionListener() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {

                if (actionId== EditorInfo.IME_ACTION_SEND
                        ||(event!=null&&event.getKeyCode()== KeyEvent.KEYCODE_ENTER)) {
                    lodingSearch(false);
                }

                return false;
            }
        });
    }

    private void lodingSearch(boolean record) {
        if (mEtSearch.getText().toString().length() < 1) {
            toast("您什么都没输入");
        } else {
            mSearchStr = mEtSearch.getText().toString();
            new SearchAsyncTask().execute();
            mProgressBar.setVisibility(View.VISIBLE);
            if (!record)
                addLocalSearchData(mSearchStr);
        }
    }

    /**
     * 添加查询记录
     * @param str
     */
    private void addLocalSearchData(String str) {
        SearchData data = new SearchData();

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        String strDate = dateFormater.format(date);

        data.setInfo(str);
        data.setDate(strDate);

        DBSearchDAOImpl.getInstance(this).insertData(data);
    }



    private class SearchAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            String url = HttpUrl.getInstance().getSearchUrl(mSearchStr);
            return HttpRequest.getInstance().POST(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            analyzeJsonDatas(s);
        }
    }

    private void analyzeJsonDatas(String s) {
        mProgressBar.setVisibility(View.GONE);
        mRecylerView.setVisibility(View.VISIBLE);
        mListView.setVisibility(View.GONE);

        try {
            JSONObject object = new JSONObject(s);
            object = object.getJSONObject("Return");
            JSONArray array = object.getJSONArray("List");

            if (array.length() <= 0) {
                toast("什么都没找到");
                return;
            } else {
                listDatas.clear();
            }

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }

        if (id == R.id.menu_search) {
            lodingSearch(false);
        }

        return super.onOptionsItemSelected(item);
    }
}
