package com.recker.flymanhua.activites;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.recker.flymanhua.R;
import com.recker.flymanhua.adapters.RecordAdapter;
import com.recker.flymanhua.base.BaseActivity;
import com.recker.flymanhua.datas.RecortSort;
import com.recker.flymanhua.db.DimensionDAOImpl;
import com.recker.flymanhua.utils.HttpUrl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;

/**
 * Created by recker on 16/7/13.
 *
 * 历史记录
 */
public class RecordActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.listview) ListView mListView;

    private List<RecortSort> listDatas;
    private RecordAdapter mAdapter;

    private boolean mIsShowClear = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void init() {
        setupToolbar();
        listDatas = DimensionDAOImpl.getInstance(this).findData();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String strCurrentDate = format.format(currentDate);//当前日期
        String insertDate = strCurrentDate;//要插入的日期
        if (listDatas.size() > 0) {
            //判断第一个数据是不是当天，如果是则加入今天，不是则插入第一条数据日期
            Date date = new Date(Long.parseLong(listDatas.get(0).getDate()));
            String strDate = format.format(date);
            if (strCurrentDate.equals(strDate)) {
                RecortSort data = new RecortSort();
                data.setDate("今天");
                data.setTitle(true);
                listDatas.add(0, data);
                strCurrentDate = strDate;
            } else {
                RecortSort data = new RecortSort();
                data.setDate(strDate.substring(5));
                data.setTitle(true);
                listDatas.add(0, data);
                strCurrentDate = strDate;
            }

            for (int i = 1; i < listDatas.size(); i++) {
                date = new Date(Long.parseLong(listDatas.get(i).getDate()));
                strDate = format.format(date);
                if (!strCurrentDate.equals(strDate)) {
                    //如果年份不相等
                    if (!strCurrentDate.substring(0, 5).equals(strDate.substring(0, 5))) {
                        insertDate = strDate;
                    } else {
                        insertDate = strDate.substring(5);
                    }

                    RecortSort data = new RecortSort();
                    data.setDate(insertDate);
                    data.setTitle(true);
                    listDatas.add(i, data);
                    //插入成功
                    strCurrentDate = strDate;
                }
            }
            mAdapter = new RecordAdapter(this, listDatas);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(this);
        } else {
            mIsShowClear = false;
            findViewById(R.id.tv_warn).setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置toolbar
     */
    private void setupToolbar() {
        mToolbar.setTitle("最近浏览");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_record, menu);
        if (!mIsShowClear)
            menu.findItem(R.id.menu_record).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.menu_record) {
            DimensionDAOImpl.getInstance(this).deleteAllData();
            listDatas.clear();
            mAdapter.notifyDataSetChanged();
            Toast.makeText(this, "清除成功...", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        RecortSort data = listDatas.get(i);
        if (!data.isTitle()) {
            Intent intent = new Intent(this, NetActivity.class);
            intent.putExtra("url",  HttpUrl.getInstance().getNetWorkUrl(data.getSortId()+""));
            intent.putExtra("name", data.getSortTile()+"");
            intent.putExtra("id", data.getDimensionId());
            intent.putExtra("dimension_title", data.getDimensionTitle());
//            intent.putExtra("author", data.getAuthor());
            intent.putExtra("record", true);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);
        }
    }
}
