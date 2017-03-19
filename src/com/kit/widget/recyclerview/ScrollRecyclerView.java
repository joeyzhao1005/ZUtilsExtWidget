package com.kit.widget.recyclerview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 可监听滚动事件的RecyclerView
 * Created by zyz on 2016/6/13.
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

                if (callback != null)
                    callback.onStateChanged(ScrollRecyclerView.this, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

                        if (!recyclerView.canScrollVertically(1)) {
                            callback.onScrollToBottom();
                        }
                        if (!recyclerView.canScrollVertically(-1)) {
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


                    if (dy != 0) {
                        if (dy > 0) {
                            callback.onScrollDown(ScrollRecyclerView.this, dy);
                        } else {
                            callback.onScrollUp(ScrollRecyclerView.this, dy);
                        }
                    }
                }
            }
        });
    }


}