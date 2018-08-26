package com.kit.widget.recyclerview

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.LayoutRes
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.kit.extend.widget.R
import com.kit.utils.DensityUtils
import com.kit.utils.ResWrapper
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
                Zog.d("ACTION_UP lastSelectedPostion:$lastSelectedPostion srolledY:$srolledY contentPaddingBottom:$contentPaddingBottom isScrollBack:$isScrollBack")

                if (lastSelectedPostion >= 0) {
                    onMenuChangedListener?.onSailfishOSMenuSelected(lastSelectedPostion, getSelectedTextView(), menuView!!)
                } else if (srolledY > 0 && srolledY < contentPaddingBottom) {
                    if (!isScrollBack) {
                        if (srolledY > minScrollY) {
                            onMenuChangedListener?.onSailfishOSMenuSelectedNone(menuView!!)
                        }
                    } else {
                        onMenuChangedListener?.onSailfishOSMenuSelectedCancel(menuView!!)
                    }
                }

                startY = 0f
                srolledY = 0f
                lastY = 0f
                lastSelectedPostion = -1
                readyShowMenu = false
                isScrollBack = false

                onMenuChangedListener?.onSailfishOSMenuPullEnd()

                if (menuView != null) {
                    menuView!!.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0)
                }

                Zog.d("ACTION_UP readyShowMenu:$readyShowMenu")
                return readyShowMenu
            }

            MotionEvent.ACTION_MOVE -> {

                if (startY == 0f) {
                    startY = motionEvent.y
                    lastY = motionEvent.y
                }


                if ((!readyShowMenu && motionEvent.y - startY < 0)) {
                    readyShowMenu = false
                } else {

                    Zog.d("canScrollVertically: ${canScrollVertically(-1)}  ${ViewCompat.canScrollVertically(this, -1)} getY:${getY()} isAtTop:$isAtTop");
                    if (Math.abs(motionEvent.y - startY) > Math.abs(sensitivity) && startY > 0 && motionEvent.y > 0 && isAtTop) {
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

                    val newDy = (motionEvent.y - lastY) * (if (srolledY < contentPaddingBottom) 1.0f else parallax)

                    if (newDy < 0) {
                        isScrollBack = true
                    }
                    srolledY += newDy
                    Zog.d("srolledY:$srolledY")
                    if (srolledY < 0) {
                        motionEvent.action = MotionEvent.ACTION_UP
                        onTouch(view, motionEvent)
                        return false
                    }

                    if (srolledY > maxHeight) {
                        srolledY = maxHeight.toFloat()
                    }

                    lastY = motionEvent.y


                    onMenuChangedListener?.onSailfishOSMenuPulling(srolledY)


                    if (srolledY < contentPaddingBottom + itemHeight) {
                        if (lastSelectedPostion != -1) {
                            textViewSelected(-1)
                        }
                        lastSelectedPostion = -1
                    } else if (srolledY > contentPaddingBottom + 1 * itemHeight && srolledY <= contentPaddingBottom + 2 * itemHeight) {
                        if (lastSelectedPostion != 0) {
                            textViewSelected(0)
                        }
                        lastSelectedPostion = 0
                    } else if (srolledY > contentPaddingBottom + 2 * itemHeight && srolledY <= contentPaddingBottom + 3 * itemHeight) {
                        if (lastSelectedPostion != 1) {
                            textViewSelected(1)
                        }
                        lastSelectedPostion = 1
                    } else if (srolledY > contentPaddingBottom + 3 * itemHeight && srolledY <= contentPaddingBottom + 4 * itemHeight) {
                        if (lastSelectedPostion != 2) {
                            textViewSelected(2)
                        }
                        lastSelectedPostion = 2
                    } else if (srolledY > contentPaddingBottom + 4 * itemHeight && srolledY <= contentPaddingBottom + 5 * itemHeight) {
                        if (lastSelectedPostion != 3) {
                            textViewSelected(3)
                        }
                        lastSelectedPostion = 3
                    } else if (srolledY > contentPaddingBottom + 5 * itemHeight && srolledY <= contentPaddingBottom + 6 * itemHeight) {
                        if (lastSelectedPostion != 4) {
                            textViewSelected(4)
                        }
                        lastSelectedPostion = 4
                    } else if (srolledY > contentPaddingBottom + 6 * itemHeight && srolledY <= contentPaddingBottom + 7 * itemHeight) {
                        if (lastSelectedPostion != 5) {
                            textViewSelected(5)
                        }
                        lastSelectedPostion = 5
                    } else if (srolledY > contentPaddingBottom + 7 * itemHeight && srolledY <= contentPaddingBottom + 8 * itemHeight) {
                        if (lastSelectedPostion != 6) {
                            textViewSelected(6)
                        }
                        lastSelectedPostion = 6
                    } else if (srolledY > contentPaddingBottom + 8 * itemHeight && srolledY <= contentPaddingBottom + 9 * itemHeight) {
                        if (lastSelectedPostion != 7) {
                            textViewSelected(7)
                        }
                        lastSelectedPostion = 7
                    } else if (srolledY > contentPaddingBottom + 9 * itemHeight && srolledY <= contentPaddingBottom + 10 * itemHeight) {
                        if (lastSelectedPostion != 8) {
                            textViewSelected(8)
                        }
                        lastSelectedPostion = 8
                    } else if (srolledY > contentPaddingBottom + 10 * itemHeight) {
                        if (lastSelectedPostion != 9) {
                            textViewSelected(9)
                        }
                        lastSelectedPostion = 9
                    }



                    if (menuView != null) {
                        menuView!!.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, srolledY.toInt())
                    }

//
//
//                    if (layoutParams is RelativeLayout.LayoutParams) {
//                        layoutParams = RelativeLayout.LayoutParams(layoutParams.width,layoutParams.height)
//
////                        Zog.d("(layoutParams as RelativeLayout.LayoutParams).topMargin:${(layoutParams as RelativeLayout.LayoutParams).topMargin}")
//                        (layoutParams as RelativeLayout.LayoutParams).topMargin = srolledY.toInt()
//                    }

//                    paddingTop = motionEvent.y.toInt()
//                    setPadding(0, srolledY.toInt(), 0, 0)
//                    invalidate()
//                    Zog.d("srolledY:$srolledY   paddingTop:$paddingTop")

                }
                return readyShowMenu
            }

            else -> return true
        }
    }

    private fun getSelectedTextView(): TextView {
        when (lastSelectedPostion) {
            0 -> {
                return menu0!!
            }

            1 -> {
                return menu1!!
            }


            2 -> {
                return menu2!!
            }


            3 -> {
                return menu3!!
            }


            4 -> {
                return menu4!!
            }


            5 -> {
                return menu5!!
            }

            6 -> {
                return menu6!!
            }


            7 -> {
                return menu7!!
            }

            8 -> {
                return menu8!!
            }


            9 -> {
                return menu9!!
            }

            else ->
                return menu0!!
        }
    }

    private fun textViewSelected(index: Int) {
        Zog.d("textViewSelected:$index")
//        tintSelectedMenuBackgroundDrawable()

        when (index) {
            0 -> {
                menu0?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
                menu1?.setBackgroundResource(R.drawable.transparent)
                menu2?.setBackgroundResource(R.drawable.transparent)
                menu3?.setBackgroundResource(R.drawable.transparent)
                menu4?.setBackgroundResource(R.drawable.transparent)
                menu5?.setBackgroundResource(R.drawable.transparent)
                menu6?.setBackgroundResource(R.drawable.transparent)
                menu7?.setBackgroundResource(R.drawable.transparent)
                menu8?.setBackgroundResource(R.drawable.transparent)
                menu9?.setBackgroundResource(R.drawable.transparent)
            }

            1 -> {
                menu0?.setBackgroundResource(R.drawable.transparent)
                menu1?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
                menu2?.setBackgroundResource(R.drawable.transparent)
                menu3?.setBackgroundResource(R.drawable.transparent)
                menu4?.setBackgroundResource(R.drawable.transparent)
                menu5?.setBackgroundResource(R.drawable.transparent)
                menu6?.setBackgroundResource(R.drawable.transparent)
                menu7?.setBackgroundResource(R.drawable.transparent)
                menu8?.setBackgroundResource(R.drawable.transparent)
                menu9?.setBackgroundResource(R.drawable.transparent)
            }

            2 -> {
                menu0?.setBackgroundResource(R.drawable.transparent)
                menu1?.setBackgroundResource(R.drawable.transparent)
                menu2?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
                menu3?.setBackgroundResource(R.drawable.transparent)
                menu4?.setBackgroundResource(R.drawable.transparent)
                menu5?.setBackgroundResource(R.drawable.transparent)
                menu6?.setBackgroundResource(R.drawable.transparent)
                menu7?.setBackgroundResource(R.drawable.transparent)
                menu8?.setBackgroundResource(R.drawable.transparent)
                menu9?.setBackgroundResource(R.drawable.transparent)
            }

            3 -> {
                menu0?.setBackgroundResource(R.drawable.transparent)
                menu1?.setBackgroundResource(R.drawable.transparent)
                menu2?.setBackgroundResource(R.drawable.transparent)
                menu3?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
                menu4?.setBackgroundResource(R.drawable.transparent)
                menu5?.setBackgroundResource(R.drawable.transparent)
                menu6?.setBackgroundResource(R.drawable.transparent)
                menu7?.setBackgroundResource(R.drawable.transparent)
                menu8?.setBackgroundResource(R.drawable.transparent)
                menu9?.setBackgroundResource(R.drawable.transparent)
            }

            4 -> {
                menu0?.setBackgroundResource(R.drawable.transparent)
                menu1?.setBackgroundResource(R.drawable.transparent)
                menu2?.setBackgroundResource(R.drawable.transparent)
                menu3?.setBackgroundResource(R.drawable.transparent)
                menu4?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
                menu5?.setBackgroundResource(R.drawable.transparent)
                menu6?.setBackgroundResource(R.drawable.transparent)
                menu7?.setBackgroundResource(R.drawable.transparent)
                menu8?.setBackgroundResource(R.drawable.transparent)
                menu9?.setBackgroundResource(R.drawable.transparent)
            }

            5 -> {
                menu0?.setBackgroundResource(R.drawable.transparent)
                menu1?.setBackgroundResource(R.drawable.transparent)
                menu2?.setBackgroundResource(R.drawable.transparent)
                menu3?.setBackgroundResource(R.drawable.transparent)
                menu4?.setBackgroundResource(R.drawable.transparent)
                menu5?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
                menu6?.setBackgroundResource(R.drawable.transparent)
                menu7?.setBackgroundResource(R.drawable.transparent)
                menu8?.setBackgroundResource(R.drawable.transparent)
                menu9?.setBackgroundResource(R.drawable.transparent)
            }

            6 -> {
                menu0?.setBackgroundResource(R.drawable.transparent)
                menu1?.setBackgroundResource(R.drawable.transparent)
                menu2?.setBackgroundResource(R.drawable.transparent)
                menu3?.setBackgroundResource(R.drawable.transparent)
                menu4?.setBackgroundResource(R.drawable.transparent)
                menu5?.setBackgroundResource(R.drawable.transparent)
                menu6?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
                menu7?.setBackgroundResource(R.drawable.transparent)
                menu8?.setBackgroundResource(R.drawable.transparent)
                menu9?.setBackgroundResource(R.drawable.transparent)
            }

            7 -> {
                menu0?.setBackgroundResource(R.drawable.transparent)
                menu1?.setBackgroundResource(R.drawable.transparent)
                menu2?.setBackgroundResource(R.drawable.transparent)
                menu3?.setBackgroundResource(R.drawable.transparent)
                menu4?.setBackgroundResource(R.drawable.transparent)
                menu5?.setBackgroundResource(R.drawable.transparent)
                menu6?.setBackgroundResource(R.drawable.transparent)
                menu7?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
                menu8?.setBackgroundResource(R.drawable.transparent)
                menu9?.setBackgroundResource(R.drawable.transparent)
            }

            8 -> {
                menu0?.setBackgroundResource(R.drawable.transparent)
                menu1?.setBackgroundResource(R.drawable.transparent)
                menu2?.setBackgroundResource(R.drawable.transparent)
                menu3?.setBackgroundResource(R.drawable.transparent)
                menu4?.setBackgroundResource(R.drawable.transparent)
                menu5?.setBackgroundResource(R.drawable.transparent)
                menu6?.setBackgroundResource(R.drawable.transparent)
                menu7?.setBackgroundResource(R.drawable.transparent)
                menu8?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
                menu9?.setBackgroundResource(R.drawable.transparent)
            }

            9 -> {
                menu0?.setBackgroundResource(R.drawable.transparent)
                menu1?.setBackgroundResource(R.drawable.transparent)
                menu2?.setBackgroundResource(R.drawable.transparent)
                menu3?.setBackgroundResource(R.drawable.transparent)
                menu4?.setBackgroundResource(R.drawable.transparent)
                menu5?.setBackgroundResource(R.drawable.transparent)
                menu6?.setBackgroundResource(R.drawable.transparent)
                menu7?.setBackgroundResource(R.drawable.transparent)
                menu8?.setBackgroundResource(R.drawable.transparent)
                menu9?.background = selectedMenuBackgroundDrawable ?: ResWrapper.getDrawable(R.drawable.bg_sailfish_os_menu_selected_line)
            }
        }
    }

    private var startY: Float = 0.toFloat()
    private var lastY: Float = 0.toFloat()
    private var srolledY: Float = 0.toFloat()
    private var readyShowMenu = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    fun setMenu(menus: Array<String>?) {
        setMenu(menus, R.layout.item_sailfish_os_menu)
    }

    fun setMenu(menus: Array<String>?, @LayoutRes layoutItemResId: Int) {

        if (menus == null || menus.size <= 0) {
            return
        }

        this.menus = menus

        val iterator: Iterator<String> = menus.iterator()

        if (menuView == null) {
            menuView = LayoutInflater.from(context).inflate(R.layout.layout_sailfish_os_menu_view, null)
        }
        contentPaddingBottom = DensityUtils.dip2px(context, 150f)

        if (parent is RelativeLayout) {
            val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0)
            (parent as RelativeLayout).addView(menuView, layoutParams)
            menuView!!.setPadding(0, 0, 0, contentPaddingBottom)
        }

        itemHeight = DensityUtils.dip2px(context, 40f)
        contentPaddingTop = DensityUtils.dip2px(context, 40f)
        minScrollY = DensityUtils.dip2px(context, 20f)
//        tintSelectedMenuBackgroundDrawable()

        var index = 0
        while (iterator.hasNext()) {
            val textView = LayoutInflater.from(context).inflate(layoutItemResId, null) as TextView
            textView.height = itemHeight
            addView(textView, iterator.next(), index)
            index++
        }
        maxHeight += contentPaddingTop + contentPaddingBottom

        setOnTouchListener(this)
    }

    private fun addView(textView: TextView, text: String?, index: Int) {
        textView.text = text ?: ""
        maxHeight += itemHeight
        if (parent is RelativeLayout) {
            when (index) {
                0 -> {
                    menu0 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

                    textView.id = INDEX_OF_MENU_ID + 0
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }

                1 -> {
                    menu1 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ABOVE, INDEX_OF_MENU_ID + 0)

                    textView.id = INDEX_OF_MENU_ID + 1
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }


                2 -> {
                    menu2 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ABOVE, INDEX_OF_MENU_ID + 1)

                    textView.id = INDEX_OF_MENU_ID + 2
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }


                3 -> {
                    menu3 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ABOVE, INDEX_OF_MENU_ID + 2)

                    textView.id = INDEX_OF_MENU_ID + 3
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }


                4 -> {
                    menu4 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ABOVE, INDEX_OF_MENU_ID + 3)

                    textView.id = INDEX_OF_MENU_ID + 4
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }


                5 -> {
                    menu5 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ABOVE, INDEX_OF_MENU_ID + 4)

                    textView.id = INDEX_OF_MENU_ID + 5
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }


                6 -> {
                    menu6 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ABOVE, INDEX_OF_MENU_ID + 5)

                    textView.id = INDEX_OF_MENU_ID + 6
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }


                7 -> {
                    menu7 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ABOVE, INDEX_OF_MENU_ID + 6)

                    textView.id = INDEX_OF_MENU_ID + 7
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }


                8 -> {
                    menu8 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ABOVE, INDEX_OF_MENU_ID + 7)

                    textView.id = INDEX_OF_MENU_ID + 8
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }

                9 -> {
                    menu9 = textView

                    val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, itemHeight)
                    layoutParams.addRule(RelativeLayout.ABOVE, INDEX_OF_MENU_ID + 8)

                    textView.id = INDEX_OF_MENU_ID + 9
                    (menuView as RelativeLayout).addView(textView, layoutParams)
                }
            }
        }
    }

    fun setMenuView(value: View?, maxHeight: Int) {
        if (parent is RelativeLayout) {

            if (menuView != null) {
                (parent as RelativeLayout).removeView(menuView)
            }
            val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0)
            (parent as RelativeLayout).addView(value, layoutParams)

        }
        this.menuView = value
        this.maxHeight = maxHeight
    }

//    @Deprecated("this function is deprecated! use setMenuView(value: View?, maxHeight: Int) instead")
//    fun setMenuView(value: View?) {
//        if (parent is RelativeLayout) {
//
//            if (menuView != null) {
//                (parent as RelativeLayout).removeView(menuView)
//            }
//            val layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 0)
//            (parent as RelativeLayout).addView(value, layoutParams)
//
//        }
//        menuView = value
//    }

    fun getContentPaddingBottom(): Int {
        return contentPaddingBottom
    }

    var selectedMenuBackgroundDrawable: Drawable? = null

    private var lastSelectedPostion: Int = -1
    private var isScrollBack: Boolean = false

    private var menuView: View? = null

    private var menu0: TextView? = null
    private var menu1: TextView? = null
    private var menu2: TextView? = null
    private var menu3: TextView? = null
    private var menu4: TextView? = null
    private var menu5: TextView? = null
    private var menu6: TextView? = null
    private var menu7: TextView? = null
    private var menu8: TextView? = null
    private var menu9: TextView? = null


    var maxHeight: Int = 0
    var itemHeight: Int = 0
    var onMenuChangedListener: OnMenuChangedListener? = null
    /**
     * 要展示的 menu 数据
     */
    private var menus: Array<String>? = null

    /**
     * 顶边距(上边距)
     */
    private var contentPaddingTop: Int = 0

    /**
     * 最小纵向滚动
     */
    private var minScrollY: Int = 0

    /**
     * 底边距（下边距）
     */
    private var contentPaddingBottom: Int = 0

    /**
     * 视差系数
     */
    private val parallax: Float = 1.8f
    private val INDEX_OF_MENU_ID: Int = 10000

    interface OnMenuChangedListener {
        /**
         * 正在下拉
         *
         * @param pullSize 下拉的高度
         */
        fun onSailfishOSMenuPulling(pullSize: Float)

        /**
         * 下拉操作结束了
         */
        fun onSailfishOSMenuPullEnd()

        /**
         * 最终选中了什么
         */
        fun onSailfishOSMenuSelected(position: Int, itemView: TextView, menuView: View)

        /**
         * 什么都没选
         */
        fun onSailfishOSMenuSelectedNone(menuView: View)

        /**
         * 取消选择
         */
        fun onSailfishOSMenuSelectedCancel(menuView: View)
    }
}