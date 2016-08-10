package com.recker.flymanhua.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.recker.flymanhua.R;
import com.recker.flymanhua.cache.ImageLoader;
import com.recker.flymanhua.datas.NewData;


import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by recker on 16/7/9.
 */
public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {

    private List<NewData> listDatas;
    private Context mContext;

    public NewAdapter(Context context, List<NewData> list) {
        this.listDatas = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_new_item, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewData data = listDatas.get(position);
        ImageLoader.getInstance().disPlay(holder.image, data.getImgUrl());
        holder.title.setText(data.getTitle() + "");
        holder.sort.setText("第" + data.getSort() + "话");
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (listDatas.get(position).isBanner())
//            return 1;
//
//        return 0;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView sort;
        LinearLayout banner;

        public ViewHolder(View view) {
            super(view);

            image = ButterKnife.findById(view, R.id.img);
            title = ButterKnife.findById(view, R.id.tv_title);
            sort = ButterKnife.findById(view, R.id.tv_sort);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(view, getLayoutPosition());
                    }
                }
            });
        }
    }

    private OnItemClickListener mItemClickListener;

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
