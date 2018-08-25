package com.kit.widget.recyclerview

import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout

import com.kit.utils.log.Zog

/**
 * 可监听滚动事件的RecyclerView
 */
class SailfishOSMenuRecyclerView : ScrollRecyclerView, View.OnTouchListener {


    //
    //    @Override
    //    public boolean onInterceptTouchEvent(MotionEvent e) {
    //        Zog.d("e.y::::::" + e.getY());
    //        return super.onInterceptTouchEvent(e);
    //    }


    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                startY = motionEvent.y
                Zog.d("ACTION_DOWN startY::::::" + motionEvent.y + " readyShowMenu:" + readyShowMenu)
                return true
            }


            MotionEvent.ACTION_UP -> {
                //                if (getTranslationY() != 0) {
                //                    setTranslationY(0);
                //                }


//                if (paddingTop != 0) {
//                    setPadding(0, 0, 0, 0)
//                }

//                if (layoutParams is RelativeLayout.LayoutParams && (layoutParams as RelativeLayout.LayoutParams).topMargin != 0) {
//                    (layoutParams as RelativeLayout.LayoutParams).topMargin = 0
//                }


                startY = 0f
                paddingY = 0f
                lastY = 0f
                readyShowMenu = false

                onMenuChangedListener?.sailfishOSMenuPullEnd()

                Zog.d("ACTION_UP:$readyShowMenu")
                return readyShowMenu
            }

            MotionEvent.ACTION_MOVE -> {

                if (startY == 0f) {
                    startY = motionEvent.y
                    lastY = motionEvent.y
                }


                if (!readyShowMenu && motionEvent.y - startY < 0) {
                    readyShowMenu = false
                } else {
                    //                    Zog.d("ACTION_MOVE startY:" + startY + " " + (Math.abs(motionEvent.getY() - startY) > Math.abs(sensitivity)) + " " + motionEvent.getY());
                    if (Math.abs(motionEvent.y - startY) > Math.abs(sensitivity) && startY > 0 && motionEvent.y > 0 && !canScrollVertically(-1)) {
                        readyShowMenu = true
                        isAtTop = true
                    }
                }

                //                Zog.d("readyShowMenu:" + readyShowMenu);
                if (readyShowMenu) {

                    //                    if (getItemAnimator() != null) {
                    ////                        ((SimpleItemAnimator) getItemAnimator()).setSupportsChangeAnimations(false);
                    ////                        getItemAnimator().setChangeDuration(0);
                    ////                    }
                    //                    if (motionEvent.getY() != translationY) {
                    //                        setTranslationY(motionEvent.getY());
                    //                    }
                    //                    translationY = motionEvent.getY();

//                    if (layoutParams is RelativeLayout.LayoutParams) {
//                        Zog.d("(layoutParams as RelativeLayout.LayoutParams).topMargin:${(layoutParams as RelativeLayout.LayoutParams).topMargin}")
//                        (layoutParams as RelativeLayout.LayoutParams).topMargin = motionEvent.y.toInt()
//                    }


                    paddingY += (motionEvent.y - lastY)
                    lastY = motionEvent.y

                    onMenuChangedListener?.sailfishOSMenuPulling(paddingY)
//
//
//                    if (layoutParams is RelativeLayout.LayoutParams) {
//                        layoutParams = RelativeLayout.LayoutParams(layoutParams.width,layoutParams.height)
//
////                        Zog.d("(layoutParams as RelativeLayout.LayoutParams).topMargin:${(layoutParams as RelativeLayout.LayoutParams).topMargin}")
//                        (layoutParams as RelativeLayout.LayoutParams).topMargin = paddingY.toInt()
//                    }

//                    paddingTop = motionEvent.y.toInt()
//                    setPadding(0, paddingY.toInt(), 0, 0)
//                    invalidate()
//                    Zog.d("paddingY:$paddingY   paddingTop:$paddingTop")

                }
                return readyShowMenu
            }

            else -> return true
        }
    }

    private var startY: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()
    private var paddingY: Float = 0.toFloat()
    private var readyShowMenu = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun setOnScrollCallback(cb: OnScrollCallback) {
        this.callback = cb

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (callback != null) {
                    callback.onScrollStateChanged(this@SailfishOSMenuRecyclerView, newState)
                }

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

                        if (!recyclerView.canScrollVertically(1)) {
                            callback.onScrollToBottom()
                            isAtBottom = true
                        }
                        if (!recyclerView.canScrollVertically(-1)) {
                            isAtTop = true
                            callback.onScrollToTop()
                            setOnTouchListener(this@SailfishOSMenuRecyclerView)
                        }
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (callback != null) {
                    callback.onScrolled(recyclerView, dx, dy)

                    isAtBottom = false
                    isAtTop = false

                    if (dy != 0) {
                        if (dy > 0) {
                            if (Math.abs(dy) > Math.abs(sensitivity)) {
                                callback.onScrollDown(this@SailfishOSMenuRecyclerView, dy)
                            }
                            //                            callback.onScrollDown(ScrollRecyclerView.this, dy);

                        } else {
                            if (Math.abs(dy) > Math.abs(sensitivity)) {
                                callback.onScrollUp(this@SailfishOSMenuRecyclerView, dy)
                            }
                            //                            callback.onScrollUp(ScrollRecyclerView.this, dy);

                        }
                    }
                }
            }
        })
    }


    var onMenuChangedListener: OnMenuChangedListener? = null
    public interface OnMenuChangedListener {
        fun sailfishOSMenuPulling(pullSize: Float)
        fun sailfishOSMenuPullEnd()
    }
}