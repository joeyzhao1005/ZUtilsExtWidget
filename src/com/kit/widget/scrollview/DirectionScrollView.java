package com.kit.widget.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.kit.widget.recyclerview.ScrollRecyclerView;


//
public class DirectionScrollView extends ScrollView {


    public DirectionScrollView(Context context) {
        super(context);
    }

    public DirectionScrollView(Context context, AttributeSet attrs,
                               int defStyle) {
        super(context, attrs, defStyle);
    }

    public DirectionScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setOnScrollViewListener(OnScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        // 这个log可以研究ScrollView的上下padding对结果的影响
        System.out.println("onScrollChanged getScrollY():" + getScrollY() + " t: " + t + " paddingTop: " + getPaddingTop());

        if (scrollViewListener != null) {

            if (oldt < t && ((t - oldt) > 15)) {// 向上
                scrollViewListener.onScrollUp(this, t - oldt);
            } else if (oldt > t && (oldt - t) > 15) {// 向下
                scrollViewListener.onScrollDown(this, oldt - t);
            }

            if (getScrollY() == 0) {
                scrollViewListener.onScrollToTop();
            } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()) {
                scrollViewListener.onScrollToBottom();
            }

            scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        }


    }


    private OnScrollViewListener scrollViewListener = null;

    public interface OnScrollViewListener {
        void onScrollChanged(DirectionScrollView scrollView, int x, int y, int oldx, int oldy);

        void onScrollUp(DirectionScrollView recycler, int dy);

        void onScrollDown(DirectionScrollView recycler, int dy);

        void onScrollToBottom();

        void onScrollToTop();
    }


}