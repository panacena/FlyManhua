package com.recker.flymanhua.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.recker.flymanhua.R;
import com.recker.flymanhua.datas.RecortSort;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by recker on 16/6/15.
 */
public class RecordAdapter extends BaseAdapter {

    private List<RecortSort> listDatas;
    private LayoutInflater inflater;

    public RecordAdapter(Context context, List<RecortSort> list) {
        this.listDatas = list;
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
    public int getItemViewType(int position) {
        if (listDatas.get(position).isTitle())
            return 0;

        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RecortSort data = listDatas.get(i);

        if (getItemViewType(i) == 0) {
            return getTitleView(data, view);
        }

        return getContentView(data, view);
    }

    private View getTitleView(RecortSort data, View view) {
        ViewHolderTitle holer;

        if (view == null) {
            holer = new ViewHolderTitle();
            view = inflater.inflate(R.layout.activity_record_title, null);

            holer.title = ButterKnife.findById(view, R.id.tv_title);

            view.setTag(holer);
        } else {
            holer = (ViewHolderTitle) view.getTag();
        }

        holer.title.setText(data.getDate());

        return view;
    }

    private View getContentView(RecortSort data, View view) {
        ViewHolderContent holer;

        if (view == null) {
            holer = new ViewHolderContent();
            view = inflater.inflate(R.layout.activity_record_content, null);

            holer.title = ButterKnife.findById(view, R.id.tv_title);
            holer.subtitle = ButterKnife.findById(view, R.id.tv_subtitle);

            view.setTag(holer);
        } else {
            holer = (ViewHolderContent) view.getTag();
        }

        holer.title.setText(data.getDimensionTitle());
        holer.subtitle.setText("第"+data.getSort()+"话");

        return view;
    }

    private static class ViewHolderTitle {
        TextView title;
    }

    private static class ViewHolderContent {
        TextView title;
        TextView subtitle;
    }


}
