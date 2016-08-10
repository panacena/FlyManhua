package com.recker.flymanhua.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.recker.flymanhua.R;
import com.recker.flymanhua.datas.SearchData;
import com.recker.flymanhua.db.DBSearchDAOImpl;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by recker on 16/7/13.
 */
public class SearchLocalAdapter extends BaseAdapter {

    private List<SearchData> listDatas;
    private LayoutInflater inflater;
    private Context mContext;

    public SearchLocalAdapter(Context context, List<SearchData> list) {
        this.listDatas = list;
        inflater = LayoutInflater.from(context);
        mContext = context;
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final SearchData data = listDatas.get(i);
        ViewHolder holder = null;

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_search_local_item, null);

            holder.info = ButterKnife.findById(view, R.id.tv_info);
            holder.clear = ButterKnife.findById(view, R.id.rel_clear);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.info.setText(data.getInfo());
        holder.clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SearchAdapter", "size--"+data.getId());
                mListener.onRefresh(data.getId());
            }
        });


        return view;
    }

    private static class ViewHolder {
        TextView info;
        RelativeLayout clear;
    }

    private OnRefreshDataListener mListener;

    public void setOnRefreshDataListener(OnRefreshDataListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshDataListener {
        void onRefresh(int id);
    }

}
