package com.recker.flymanhua.activites;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.recker.flymanhua.R;
import com.recker.flymanhua.base.BaseActivity;
import com.recker.flymanhua.utils.HttpRequest;
import com.recker.flymanhua.utils.HttpUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by recker on 16/7/13.
 *
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.et_content) EditText etContent;
    @Bind(R.id.et_contact) EditText etContact;
    @Bind(R.id.tv_number) TextView tvNumber;

    private String mContent;
    private String mInfo;
    private String mDate;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feekback;
    }

    @Override
    protected void init() {
        setupToolbar();
        setupEditText();
    }

    /**
     * 设置toolbar
     */
    private void setupToolbar() {
        mToolbar.setTitle("意见反馈");
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar == null) return;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void setupEditText() {
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, final int start, final int before, final int count) {

                tvNumber.post(new Runnable() {
                    @Override
                    public void run() {
                        tvNumber.setText((400 - (start + count)) + "");
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_feedback, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.menu_send) {
            addData();
        }
        return super.onOptionsItemSelected(item);
    }

    private void addData() {
        mContent = etContent.getText().toString();
        mInfo = etContact.getText().toString();
        if (mContent.length() < 1) {
            toast("亲，内容不能为空哦");
            return;
        }

        if (mInfo.length() < 1) {
            toast("请留下你的常用邮箱吧");
            return;
        }

        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=new Date();
        mDate = dateFormater.format(date);


        new UploadAsyncTask().execute();

    }

    private class UploadAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {

            String url = HttpUrl.getInstance().getFeekback();
            Map<String, String> params = HttpUrl.getInstance().getFeekbackParams(mContent, mInfo, mDate);

            return HttpRequest.getInstance().POST(url, params);
        }

        @Override
        protected void onPostExecute(String s) {
            debug(s);

            try {
                JSONObject object = new JSONObject(s);
                int code = object.getInt("code");

                if (code == 200) {
                    toast("反馈成功啦，我们会及时处理的");
                    finish();
                } else {
                    toast("反馈失败啦...");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void toast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void debug(String str) {
        Log.d(FeedBackActivity.class.getSimpleName(), str);
    }

}
