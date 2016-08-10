package com.recker.flymanhua.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.recker.flymanhua.R;
import com.recker.flymanhua.cache.ImageLoader;
import com.recker.flymanhua.datas.SortData;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by recker on 16/7/13.
 */
public class SortAdapter extends BaseAdapter {

    private List<SortData> listDatas;
    private LayoutInflater inflater;
    private Context context;

    public SortAdapter(Context context, List<SortData> list) {
        this.context = context;
        listDatas = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return listDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        SortData sort = listDatas.get(i);
        ViewHolder holder = null;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_sort_item, null);

            holder.img = ButterKnife.findById(view, R.id.img);
            holder.title = ButterKnife.findById(view, R.id.tv_title);
            holder.date = ButterKnife.findById(view, R.id.tv_date);
            holder.sort = ButterKnife.findById(view, R.id.tv_sort);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (sort.getImgUrl() == null) {
            holder.img.setImageResource(R.drawable.ic_clear);
        } else {
//            Glide.with(context).load(sort.getImgUrl()).into(holder.img);
            ImageLoader.getInstance().disPlay(holder.img, sort.getImgUrl());
        }

        if (sort.getTitle() == null) {
            holder.title.setText("");
        } else {
            holder.title.setText(sort.getTitle() + "");
        }

        if (sort.getDate() == null) {
            holder.date.setText("");
        } else {
            holder.date.setText(sort.getDate() + "");
        }

        if (sort.getSort() == 0) {
            holder.sort.setText("");
        } else {
            holder.sort.setText("第" + sort.getSort() + "话");
        }


        return view;
    }

    private static class ViewHolder {
        ImageView img;
        TextView title;
        TextView date;
        TextView sort;
    }
}

