package com.kit.widget.recyclerview;

import android.support.v7.widget.RecyclerView;

public interface OnScrollCallback {

    void onStateChanged(ScrollRecyclerView recycler, int state);

    void onScrollUp(ScrollRecyclerView recycler, int dy);

    void onScrollToBottom();

    void onScrollDown(ScrollRecyclerView recycler, int dy);

    void onScrollToTop();

    void onScrolled(RecyclerView recyclerView, int dx, int dy);

}
