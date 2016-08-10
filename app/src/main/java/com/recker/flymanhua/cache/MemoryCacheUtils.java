package com.recker.flymanhua.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by recker on 16/6/17.
 *
 * 内存缓存
 */
public class MemoryCacheUtils {

    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCacheUtils() {
        //得到手机最大内存的1/8，即超过指定内存，则开始回收
        long maxMemory = Runtime.getRuntime().maxMemory()/8;
        mMemoryCache = new LruCache<String, Bitmap>((int)maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    /**
     * 从内存中获取Bitmap
     * @param url
     * @return
     */
    public Bitmap getBitmarFromMemory(String url) {
        return mMemoryCache.get(url);
    }

    /**
     * 往内存中写入Bitmap
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory(String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }

}
