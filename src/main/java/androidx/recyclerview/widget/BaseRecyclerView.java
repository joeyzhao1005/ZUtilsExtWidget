package androidx.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

/**
 * @author joeyzhao
 */
public class BaseRecyclerView extends RecyclerView {

    public BaseRecyclerView(@NonNull Context context) {
        super(context);
        init();
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void scrollByDirectly(int x, int y) {
        scrollByDirectly(x, y, null);
    }

    public void scrollByDirectly(int x, int y, MotionEvent ev) {
        if (mLayout == null) {
            Log.e(TAG, "Cannot scroll without a LayoutManager set. "
                    + "Call setLayoutManager with a non-null argument.");
            return;
        }
        if (mLayoutSuppressed) {
            return;
        }
//        final boolean canScrollHorizontal = mLayout.canScrollHorizontally();
//        final boolean canScrollVertical = mLayout.canScrollVertically();
//        if (canScrollHorizontal || canScrollVertical) {
        scrollByInternal(x, y, null, ViewCompat.TYPE_NON_TOUCH);
//        }
    }


    private void init() {

    }


}
