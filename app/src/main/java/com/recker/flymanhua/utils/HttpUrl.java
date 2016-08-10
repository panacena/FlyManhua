package com.recker.flymanhua.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by recker on 16/7/9.
 */
public class HttpUrl {
    private static HttpUrl instance;
    private String HOST_NAME = "http://121.42.143.190/manhua/";

    private String update = HOST_NAME + "update.php";//检查版本更新
    private String feekback = HOST_NAME + "feekback.php";//反馈信息

    private HttpUrl(){}

    public static HttpUrl getInstance() {
        if (instance == null) {
            synchronized (HttpUrl.class) {
                if (instance == null) {
                    instance = new HttpUrl();
                }
            }
        }
        return instance;
    }

    public String getUpdateUrl() {
        return update;
    }

    public String getFeekback() {
        return feekback;
    }

    /**
     * 获取漫画分类url POST
     *
     * @param type 4为热血漫画 2为国产漫画 3为鼠绘漫画
     * @param page
     * @return
     */
    public String getTypeUrl(int type, int page) {
        return "http://www.ishuhui.net/ComicBooks/GetAllBook?ClassifyId="+type
                +"&Size=10&PageIndex="+page;
    }


    /**
     * 漫画分类
     * @param id
     * @return
     */
    public String getSortUrl(int id, int page) {
        return "http://www.ishuhui.net/ComicBooks/GetChapterList?id="+id+"&PageIndex="+page;
    }

    /**
     * 获取最新漫画
     * @return
     */
    public String getNewUrl() {
        return "http://www.ishuhui.net/ComicBooks/GetLastChapterForBookIds?idJson=" +
                "%5B13%2C3%2C5%2C45%2C22%2C12%2C21%2C2%2C10%2C8%2C48%2C15%2C4%2C14%2C65%5D";
    }

    /**
     * 获取对应的网页
     * @param id
     * @return
     */
    public String getNetWorkUrl(String id) {
        return "http://www.ishuhui.net/ComicBooks/ReadComicBooksToIsoV1/"+id+".html";
    }

    /**
     * 查询漫画
     * @param str
     * @return
     */
    public String getSearchUrl(String str) {
        return "http://www.ishuhui.net/ComicBooks/GetAllBook?Title="+getUtf8String(str);
    }

    /**
     * 获取反馈参数
     * @return
     */
    public Map<String, String> getFeekbackParams(String content, String email, String date) {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        params.put("info", email);
        params.put("date", date);

        return params;
    }

    /**
     * 获取utf-8字符串
     * @param str
     * @return
     */
    private String getUtf8String(String str) {

        try {
            str = URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return str;
    }
}
