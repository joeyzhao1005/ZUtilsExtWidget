package com.kit.widget.recyclerview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 可监听滚动事件的RecyclerView
 */
public class ScrollRecyclerView extends RecyclerView {

    public ScrollRecyclerView(Context context) {
        super(context);
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    OnScrollCallback callback;

    public OnScrollCallback getOnScrollCallback() {

        return callback;
    }

    public void setOnScrollCallback(OnScrollCallback cb) {
        this.callback = cb;

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (callback != null) {
                    callback.onScrollStateChanged(ScrollRecyclerView.this, newState);
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

                        if (!recyclerView.canScrollVertically(1)) {
                            callback.onScrollToBottom();
                            isAtBottom = true;
                        }
                        if (!recyclerView.canScrollVertically(-1)) {
                            isAtTop = true;
                            callback.onScrollToTop();
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (callback != null) {
                    callback.onScrolled(recyclerView, dx, dy);

                    isAtBottom = false;
                    isAtTop = false;

                    if (dy != 0) {
                        if (dy > 0) {
                            if (Math.abs(dy) > Math.abs(sensitivity)) {
                                callback.onScrollDown(ScrollRecyclerView.this, dy);
                            }
//                            callback.onScrollDown(ScrollRecyclerView.this, dy);

                        } else {
                            if (Math.abs(dy) > Math.abs(sensitivity)) {
                                callback.onScrollUp(ScrollRecyclerView.this, dy);
                            }
//                            callback.onScrollUp(ScrollRecyclerView.this, dy);

                        }
                    }
                }
            }
        });
    }


    public void setSensitivity(int sensitivity) {
        this.sensitivity = sensitivity;
    }

    public boolean isAtTop() {
        return isAtTop;
    }

    //上下滑动监听灵敏度
    protected int sensitivity = 10;

    protected boolean isAtTop = true;
    protected boolean isAtBottom;


}