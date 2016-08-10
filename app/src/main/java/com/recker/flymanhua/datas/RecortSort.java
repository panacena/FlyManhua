package com.recker.flymanhua.datas;

/**
 * Created by recker on 16/6/15.
 *
 * 观看记录
 *
 */
public class RecortSort {

    private int dimensionId;//漫画ID
    private int sortId;//漫画话数ID
    private int _id;
    private String dimensionTitle;//漫画标题
    private String sortTile;//话数标题
    private String date;//时间戳
    private int sort;//第几话
    private String author;//作者
    private String netImg;//网络图片地址
    private String localImg;//本地图片地址
    private boolean isTitle = false;//是否是日期标题

    public boolean isTitle() {
        return isTitle;
    }

    public void setTitle(boolean title) {
        isTitle = title;
    }

    public int getDimensionId() {
        return dimensionId;
    }

    public void setDimensionId(int dimensionId) {
        this.dimensionId = dimensionId;
    }

    public int getSortID() {
        return sortId;
    }

    public void setSortID(int sortID) {
        this.sortId = sortID;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getDimensionTitle() {
        return dimensionTitle;
    }

    public void setDimensionTitle(String dimensionTitle) {
        this.dimensionTitle = dimensionTitle;
    }

    public String getSortTile() {
        return sortTile;
    }

    public void setSortTile(String sortTile) {
        this.sortTile = sortTile;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNetImg() {
        return netImg;
    }

    public void setNetImg(String netImg) {
        this.netImg = netImg;
    }

    public String getLocalImg() {
        return localImg;
    }

    public void setLocalImg(String localImg) {
        this.localImg = localImg;
    }

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
        this.sortId = sortId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RecortSort{" +
                "dimensionId=" + dimensionId +
                ", sortId=" + sortId +
                ", _id=" + _id +
                ", dimensionTitle='" + dimensionTitle + '\'' +
                ", sortTile='" + sortTile + '\'' +
                ", date='" + date + '\'' +
                ", sort=" + sort +
                ", author='" + author + '\'' +
                ", netImg='" + netImg + '\'' +
                ", localImg='" + localImg + '\'' +
                '}';
    }
}
