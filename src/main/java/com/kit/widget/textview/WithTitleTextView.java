package com.kit.widget.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.utils.DensityUtils;
import com.kit.utils.TextViewUtils;
import com.kit.widget.base.BaseLinearLayout;

public class WithTitleTextView extends BaseLinearLayout {

    // private EditText et;
    private TextView tvTitle, tvContent;
    private ImageButton ibInfo;
    private String contentString, WithTitleTextView_title;
    private Drawable WithTitleTextView_background, goSrc;

    private LinearLayout llWithTitleTextView, llContainer, llContent;

    private float title_size, content_size;
    private int title_gravity;
    private int title_margin, title_margin_left, title_margin_right, title_margin_top, title_margin_bottom, content_margin, content_margin_left, content_margin_right,
            margin, margin_left, margin_right;
    private boolean is_content_left, go_enabled;
    private int title_color, content_color;

    @SuppressLint("NewApi")
    public WithTitleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 方式1获取属性
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WithTitleTextView);

        title_color = a.getColor(
                R.styleable.WithTitleTextView_WithTitleTextView_title_color,
                getResources().getColor(R.color.black));

        title_size = a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_title_size, -1);

        title_gravity = a.getInt(
                R.styleable.WithTitleTextView_WithTitleTextView_title_gravity, Gravity.LEFT | Gravity.CENTER_VERTICAL);

        switch (title_gravity) {
            case 0:
                title_gravity = Gravity.LEFT;
                break;
            case 1:
                title_gravity = Gravity.TOP;
                break;
            case 2:
                title_gravity = Gravity.CENTER;
                break;
            case 3:
                title_gravity = Gravity.RIGHT;
                break;
            case 4:
                title_gravity = Gravity.BOTTOM;
                break;
            case 5:
                title_gravity = Gravity.CENTER_VERTICAL;
                break;
            case 6:
                title_gravity = Gravity.CENTER_HORIZONTAL;
                break;
        }

        if (title_gravity == (0 | 5)) {
            title_gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
        } else if (title_gravity == (0 | 4)) {
            title_gravity = Gravity.LEFT | Gravity.BOTTOM;
        }

//        title_margin = (int) a.getDimension(
//                R.styleable.WithTitleTextView_WithTitleTextView_title_margin, -1);
//
        title_margin_left = (int) a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_title_margin_left, 0);

        title_margin_right = (int) a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_title_margin_right, 0);

        title_margin_top = (int) a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_title_margin_top, 0);


        title_margin_bottom = (int) a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_title_margin_bottom, 0);

        content_color = a.getColor(
                R.styleable.WithTitleTextView_WithTitleTextView_content_color,
                getResources().getColor(R.color.black));

        content_size = a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_content_size,
                -1);

//        content_margin = (int) a.getDimension(
//                R.styleable.WithTitleTextView_WithTitleTextView_content_margin, -1);
//
//        content_margin_left = (int) a.getDimension(
//                R.styleable.WithTitleTextView_WithTitleTextView_content_margin_left, 0);
//
//        content_margin_right = (int) a.getDimension(
//                R.styleable.WithTitleTextView_WithTitleTextView_content_margin_right, 0);

        WithTitleTextView_background = a
                .getDrawable(R.styleable.WithTitleTextView_WithTitleTextView_background);

        WithTitleTextView_title = a
                .getString(R.styleable.WithTitleTextView_WithTitleTextView_title);

        contentString = a
                .getString(R.styleable.WithTitleTextView_WithTitleTextView_content);

        is_content_left = a
                .getBoolean(
                        R.styleable.WithTitleTextView_WithTitleTextView_is_content_left,
                        false);


        View view = LayoutInflater.from(context).inflate(
                R.layout.with_title_textview, null);


        //title
        tvTitle = (TextView) view.findViewById(R.id.tvWithTitleTextViewTitle);
        tvTitle.setGravity(title_gravity);

        ibInfo = (ImageButton) view.findViewById(R.id.ibInfo);

        ((RelativeLayout.LayoutParams) tvTitle.getLayoutParams()).setMargins(
                DensityUtils.dip2px(context, title_margin_left)
                , DensityUtils.dip2px(context, title_margin_top)
                , DensityUtils.dip2px(context, title_margin_right)
                , DensityUtils.dip2px(context, title_margin_bottom)
        );


        ((RelativeLayout.LayoutParams) tvTitle.getLayoutParams()).setMargins(
                title_margin_left
                , title_margin_top
                , title_margin_right
                , title_margin_bottom
        );


        if (title_size != -1)
            tvTitle.setTextSize(DensityUtils.px2dip(context, title_size));

        tvTitle.setTextColor(title_color);

        if (WithTitleTextView_title != null)
            tvTitle.setText(WithTitleTextView_title);


        //content
        llContent = (LinearLayout) view.findViewById(R.id.llContent);

        tvContent = (TextView) view
                .findViewById(R.id.tvWithTitleTextViewContent);

//        if (content_margin != -1) {
//            TextViewUtils.setMargin(tvContent, content_margin, 0, content_margin, 0);
//        } else {
//            TextViewUtils.setMargin(tvContent, content_margin_left, 0, content_margin_right, 0);
//        }

        if (is_content_left)
            llContent.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        else
            llContent.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);

        if (content_size != -1)
            tvContent.setTextSize(DensityUtils.px2dip(context, content_size));

        tvContent.setTextColor(content_color);

        if (contentString != null)
            tvContent.setText(contentString);


        //whole

        llWithTitleTextView = (LinearLayout) view
                .findViewById(R.id.llWithTitleTextView);

        llContainer = (LinearLayout) view
                .findViewById(R.id.llContainer);

        if (WithTitleTextView_background != null)
            llWithTitleTextView.setBackground(WithTitleTextView_background);

        margin = (int) a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_margin, -1);

        margin_left = (int) a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_margin_left, 0);

        margin_right = (int) a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_margin_right, 0);


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        if (margin != -1) {
            lp.setMargins(margin, 0, margin, 0);
        } else {
            lp.setMargins(margin_left, 0, margin_right, 0);
        }

        llContainer.setLayoutParams(lp);

        a.recycle();

        // 把获得的view加载到这个控件中
        addView(view);
    }


//    public void setPadding(int left, int top, int right, int bottom){
//        llContainer.setPadding( left,  top,  right,  bottom);
//    }

    /**
     * @param text title文字
     * @return void 返回类型
     * @Title setTitle
     * @Description 设置title
     */
    public void setTitle(CharSequence text) {
        tvTitle.setText(text);
    }

    /**
     * @param text activity返回过来的文字
     * @return void 返回类型
     * @Title setContent
     * @Description 设置activity返回过来的文字
     */
    public void setContent(CharSequence text) {
        tvContent.setText(text);
    }

    /**
     * @param textResId activity返回过来的文字
     * @return void 返回类型
     * @Title setContent
     * @Description 设置activity返回过来的文字
     */
    public void setContent(int textResId) {
        tvContent.setText(textResId);
    }


    public int getContentColor() {
        return content_color;
    }

    public void setContentColor(int contentColor) {
        this.content_color = contentColor;
        tvContent.setTextColor(contentColor);
    }

    public TextView getTextViewContent() {
        return tvContent;
    }


    /**
     * 设置info点击监听器
     *
     * @param
     */
    public void setOnIbInfoClickListener(OnClickListener onClickListener) {
        ibInfo.setVisibility(VISIBLE);
        ibInfo.setOnClickListener(onClickListener);
    }


    public ImageButton getIbInfo() {
        return ibInfo;
    }

    /**
     * @return void 返回类型
     * @Title getContent
     * @Description 设置activity返回过来的文字
     */
    public CharSequence getContent() {
        return tvContent.getText();
    }


    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvContent() {
        return tvContent;
    }
}