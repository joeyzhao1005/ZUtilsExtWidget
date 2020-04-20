package androidx.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        scrollByInternal(x, y, ev);
    }


    private void init() {

    }


}
