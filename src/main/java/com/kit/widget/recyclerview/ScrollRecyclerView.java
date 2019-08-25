package com.kit.widget.recyclerview;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
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

                checkTopOrBottom();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                checkTopOrBottom();

                if (callback != null) {
                    callback.onScrolled(recyclerView, dx, dy);
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


    private void checkTopOrBottom() {
        if (!canScrollVertically(1)) {
            callback.onScrollToBottom();
            isAtBottom = true;
        } else if (!canScrollVertically(-1)) {
            //一定要注意，如果headerview 高度设置成了0 那么这里永远走不进来 也就是永远不在顶部
            isAtTop = true;
            callback.onScrollToTop();
        } else {
            isAtBottom = false;
            isAtTop = false;
        }
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