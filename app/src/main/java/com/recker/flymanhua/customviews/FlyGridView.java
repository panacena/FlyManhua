package com.recker.flymanhua.customviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * Created by recker on 16/4/14.
 */
public class FlyGridView extends GridView {


    private boolean isAutoSetNumColumns = true;
    private int horizontalSpacing = 0;
    private int columnWidth = 0;//列宽
    private boolean isScroll = false;//是否正在滑动

    public FlyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {

        if (isScroll) {
            int gridViewWidth = 0;
            int adapterCount = adapter.getCount();

            if (columnWidth == 0) {
                for(int i = 0 ; i< adapter.getCount();i++)
                {
                    View rowView = adapter.getView(i,null,null);
                    measureChildView(rowView);
                    gridViewWidth += rowView.getMeasuredWidth();
                }
            } else {
                gridViewWidth += columnWidth*adapterCount;
            }

            if(isAutoSetNumColumns)
            {
                gridViewWidth += (this.getPaddingLeft()+ this.getPaddingRight());
                gridViewWidth += this.horizontalSpacing*(adapterCount-1);
                this.setNumColumns(adapterCount);
            }

            this.setLayoutParams(new
                    LinearLayout.LayoutParams(gridViewWidth,LayoutParams.WRAP_CONTENT));

        }

        super.setAdapter(adapter);
    }


    private void measureChildView(View child) {
        ViewGroup.LayoutParams lp = child.getLayoutParams();

        if (lp == null) {
            lp = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0,
                0 + 0, lp.width);
        int lpHeight = lp.height;
        int childHeightSpec;

        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }


    @Override
    public void setNumColumns(int numColumns) {

        isAutoSetNumColumns = false;

        super.setNumColumns(numColumns);
    }

    @Override
    public void setHorizontalSpacing(int horizontalSpacing) {

        this.horizontalSpacing = horizontalSpacing;

        super.setHorizontalSpacing(horizontalSpacing);
    }

    @Override
    public void setColumnWidth(int columnWidth) {

        this.columnWidth = columnWidth;

        super.setColumnWidth(columnWidth);
    }

    public void setEnableHorizontalScroll(boolean isScroll)
    {
        this.isScroll = isScroll;
    }

}
