package com.goodmorning.view.recyclerview.decoration;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.goodmorning.view.recyclerview.CommonRecyclerViewAdapter;


public class DiverItemDecoration extends RecyclerView.ItemDecoration {
    private static final String TAG = DiverItemDecoration.class.getName();
    private int space;
    private int columnCount;

    public DiverItemDecoration(int space, int columnCount) {
        this.space = space;
        this.columnCount = columnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getChildViewHolder(view) instanceof HomeAdapter.FormalThreeVH) {}
        RecyclerView.Adapter adapter = parent.getAdapter();

        CommonRecyclerViewAdapter lRecyclerViewAdapter;
        if (adapter instanceof CommonRecyclerViewAdapter) {
            lRecyclerViewAdapter = (CommonRecyclerViewAdapter) adapter;
        } else {
            throw new RuntimeException("the adapter must be LRecyclerViewAdapter");
        }
//        outRect.top = space;
        //瀑布流专属分割线
        StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        int itemPosition = params.getViewLayoutPosition();
        if(lRecyclerViewAdapter.isHeader(itemPosition) || lRecyclerViewAdapter.isRefreshHeader(itemPosition) || lRecyclerViewAdapter.isFooter(itemPosition)) {
            outRect.top = 0;
        }else {
            outRect.top = space;
        }
        /**
         * 根据params.getSpanIndex()来判断左右边确定分割线
         * 第一列设置左边距为space，右边距为space/2  （第二列反之）
         */
        if (params.getSpanIndex() % 2 == 0) {
            outRect.left = space;
            outRect.right = space / 2;
        } else {
            outRect.left = space / 2;
            outRect.right = space;
        }
    }
}
