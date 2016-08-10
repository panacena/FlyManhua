package com.recker.flymanhua.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.recker.flymanhua.R;
import com.recker.flymanhua.activites.AboutActivity;
import com.recker.flymanhua.activites.FeedBackActivity;
import com.recker.flymanhua.base.BaseFragment;
import com.recker.flymanhua.cache.LocalCacheUtils;
import com.recker.flymanhua.utils.HttpRequest;
import com.recker.flymanhua.utils.HttpUrl;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by recker on 16/7/9.
 *
 * 我的
 */
public class MeFragment extends BaseFragment {
    @Bind(R.id.tv_cache_size) TextView mTvCacheSize;

    private int versionCode = 0;//版本号
    private String mVerSion;//更新版本
    private String mUpdateInfo;//更新信息
    private String mUpdateUrl;//更新链接
    private View mUpdateView;
    private Dialog mUpdateDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void init() {
        getImageCacheSize();
    }

    /**
     * 检查更新
     */
    @OnClick(R.id.tv_update) void setupUpdate() {
        update();
    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.rel_clear_cache) void setupClearCache() {
        File file = new File(LocalCacheUtils.PATH);
        deleteFilesByDirectory(file);
    }

    /**
     * 意见反馈
     */
    @OnClick(R.id.tv_feekback) void setupFeekback() {
        Intent intent = new Intent(getActivity(), FeedBackActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);
    }

    /**
     * 关于我们
     */
    @OnClick(R.id.tv_about) void setupAbout() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_none);
    }

    /**
     * 获取图片缓存大小
     */
    private void getImageCacheSize() {
        File file = new File(LocalCacheUtils.PATH);
        long totalBlocks = 0;
        if (file != null && file.exists() && file.isDirectory()) {
            for (File item : file.listFiles()) {
                totalBlocks += item.length();
//                debug("size--"+item.length());
            }
        }

        String size = Formatter.formatFileSize(getActivity(), totalBlocks);
        mTvCacheSize.setText(size);
    }

    /**
     * 清除缓存
     * @param directory
     */
    private void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
            getImageCacheSize();
            Toast.makeText(getActivity(), "清除缓存成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void update() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), 0);
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
                    } else {
                        Toast.makeText(getActivity(), "已经是最新版...", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void showUpdateDialog() {
        mUpdateView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update, null);
        mUpdateDialog = new Dialog(getActivity(), R.style.Dialog);
        mUpdateDialog.setContentView(mUpdateView);

        TextView tvInfo = ButterKnife.findById(mUpdateView, R.id.content);
        tvInfo.setText(mUpdateInfo);
        Button btnNo = ButterKnife.findById(mUpdateView, R.id.btn_no);
        Button btnYes = ButterKnife.findById(mUpdateView, R.id.btn_yes);
        setupBtnNoClick(btnNo);
        setupBtnYesClick(btnYes);

        DisplayMetrics metric = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metric);
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

}
