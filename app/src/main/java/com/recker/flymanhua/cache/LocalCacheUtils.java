package com.recker.flymanhua.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by recker on 16/6/17.
 *
 * 本地缓存
 */
public class LocalCacheUtils {
    public static final String PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/flymanhua/cache/img";

    /**
     * 从本地读取图片
     * @param url
     * @return
     */
    public Bitmap getBitmapFromLocal(String url) {
        String fileName = null;

        try {
            fileName = MD5Uitls.getInstance().getMd5(url);
            File file = new File(PATH, fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            return bitmap;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * 从网络获取图片后，保存至本地缓存
     * @param url
     * @param bitmap
     */
    public void setBitmapToLocal(String url, Bitmap bitmap) {
        try {
            String fileName = MD5Uitls.getInstance().getMd5(url);
            File file = new File(PATH, fileName);

            //通过得到文件的父文件,判断父文件是否存在
            File parentFile = file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }
            //把图片保存至本地
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
