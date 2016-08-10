package com.recker.flymanhua.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.recker.flymanhua.R;


/**
 * Created by recker on 16/6/17.
 */
public class ImageLoader {
    private static ImageLoader instance;
    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    private ImageLoader() {
        mMemoryCacheUtils = new MemoryCacheUtils();
        mLocalCacheUtils = new LocalCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
    }

    public static ImageLoader getInstance() {
        if (instance == null) {
            synchronized (ImageLoader.class) {
                if (instance == null) {
                    instance = new ImageLoader();
                }
            }
        }
        return instance;
    }

    public void disPlay(ImageView iv, String url) {
        iv.setImageResource(R.drawable.ic_defalut_two);
        Bitmap bitmap;

        //获取内存缓存
        bitmap = mMemoryCacheUtils.getBitmarFromMemory(url);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
//            debug("从内存中获取图片");
            return;
        }

        //获取本地缓存
        bitmap = mLocalCacheUtils.getBitmapFromLocal(url);
        if (bitmap != null) {
            iv.setImageBitmap(bitmap);
//            debug("从本地获取图片");
            return;
        }

        //从网络获取图片，并缓存
        mNetCacheUtils.getBitmapFromNet(iv, url);
    }

    private void debug(String str) {
        Log.d(ImageLoader.class.getSimpleName(), str);
    }
}
