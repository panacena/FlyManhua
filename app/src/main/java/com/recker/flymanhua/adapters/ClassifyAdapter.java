package com.recker.flymanhua.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.recker.flymanhua.R;
import com.recker.flymanhua.cache.ImageLoader;
import com.recker.flymanhua.datas.ClassifyData;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by recker on 16/7/10.
 */
public class ClassifyAdapter extends RecyclerView.Adapter<ClassifyAdapter.ViewHolder> {

    private List<ClassifyData> listDatas;
    private Context mContext;

    public ClassifyAdapter(Context context, List<ClassifyData> list) {
        listDatas = list;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view  = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_classify_item, null);
        } else if (viewType == 1){
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_onloading, null);
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            ClassifyData data = listDatas.get(position);
//            Glide.with(mContext).load(data.getImgUrl()).into(holder.img);
            ImageLoader.getInstance().disPlay(holder.img, data.getImgUrl());
            holder.title.setText(data.getTitle() + "");
            holder.subtitle.setText(data.getSubtitle() + "");
            holder.author.setText(data.getAuthor() + "");
            holder.sort.setText("第" + data.getSort() + "话");
        }
    }

    @Override
    public int getItemCount() {
        return listDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        ClassifyData data = listDatas.get(position);
        if (data.isFinised()) {
            return 2;
        } else if (data.isLoading()) {
            return 1;
        } else {
            return 0;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView title;
        TextView subtitle;
        TextView author;
        TextView sort;

        public ViewHolder(View view) {
            super(view);

            img = ButterKnife.findById(view, R.id.img);
            title = ButterKnife.findById(view, R.id.tv_title);
            subtitle = ButterKnife.findById(view, R.id.tv_subtitle);
            author = ButterKnife.findById(view, R.id.tv_author);
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
