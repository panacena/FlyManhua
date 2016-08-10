package com.recker.flymanhua.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by recker on 16/6/17.
 */
public class NetCacheUtils {

    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        this.mLocalCacheUtils = localCacheUtils;
        this.mMemoryCacheUtils = memoryCacheUtils;
    }

    /**
     * 从网络中下载图片
     * @param iv
     * @param url
     */
    public void getBitmapFromNet(ImageView iv, String url) {
        new BitmapAsyncTask().execute(iv, url);
    }

    private class BitmapAsyncTask extends AsyncTask<Object, Void, Bitmap> {

        private ImageView mImage;
        private String mUrl;

        @Override
        protected Bitmap doInBackground(Object... objects) {
            mImage = (ImageView) objects[0];
            mUrl = (String) objects[1];

            return downloadBitmap(mUrl);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mImage.setImageBitmap(bitmap);

                //从网络下载图片缓存到本地
                mLocalCacheUtils.setBitmapToLocal(mUrl, bitmap);
                //从网络下载图片缓存到内存
                mMemoryCacheUtils.setBitmapToMemory(mUrl, bitmap);
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection conn  = null;

        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
