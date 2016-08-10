package com.recker.flymanhua.cache;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by recker on 16/6/17.
 */
public class MD5Uitls {

    private static MD5Uitls instance;
    private MD5Uitls(){}

    public static MD5Uitls getInstance() {
        if (instance == null) {
            synchronized (MD5Uitls.class) {
                if (instance == null) {
                    instance = new MD5Uitls();
                }
            }
        }
        return instance;
    }

    public String getMd5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuffer sb = new StringBuffer(hash.length*2);
            for (Byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(b & 0xFF));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
