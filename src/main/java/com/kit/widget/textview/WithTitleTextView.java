package com.kit.widget.textview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;

import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.utils.ApiLevel;
import com.kit.utils.DensityUtils;
import com.kit.utils.StringUtils;

public class WithTitleTextView extends LinearLayout {

    // private EditText et;
    private TextView tvTitle, tvContent;
    private ImageButton ibInfo;
    private String contentString, WithTitleTextView_title;


    private float title_size, content_size;
    private int contentPosition = 0;
    private int title_color, content_color;

    public WithTitleTextView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public WithTitleTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public WithTitleTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @TargetApi(21)
    public WithTitleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {

        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WithTitleTextView,
                defStyleAttr, 0);


    }

    private void setStyle(Context context, TypedArray a) {
        title_color = a.getColor(
                R.styleable.WithTitleTextView_WithTitleTextView_title_color,
                getResources().getColor(R.color.black));

        title_size = a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_title_size, -1);

        content_color = a.getColor(
                R.styleable.WithTitleTextView_WithTitleTextView_content_color, getResources().getColor(R.color.gray));

        content_size = a.getDimension(
                R.styleable.WithTitleTextView_WithTitleTextView_content_size,
                -1);


        WithTitleTextView_title = a.getString(R.styleable.WithTitleTextView_WithTitleTextView_title);
        contentPosition = a.getInt(R.styleable.WithTitleTextView_WithTitleTextView_content_align, 0);

        contentString = a.getString(R.styleable.WithTitleTextView_WithTitleTextView_content);

        a.recycle();


        LayoutInflater.from(context).inflate(
                R.layout.with_title_textview, this);


        //title
        tvTitle = (TextView) findViewById(R.id.tvWithTitleTextViewTitle);

        ibInfo = (ImageButton) findViewById(R.id.ibInfo);


        if (title_size != -1) {
            tvTitle.setTextSize(DensityUtils.px2dip(context, title_size));
        }

        tvTitle.setTextColor(title_color);

        if (WithTitleTextView_title != null) {
            tvTitle.setText(WithTitleTextView_title);
        }


        //content

        tvContent = (TextView) findViewById(R.id.tvWithTitleTextViewContent);
        switch (contentPosition) {
            case 1:
                if (ApiLevel.ATLEAST_JB_MR1) {
                    ((RelativeLayout.LayoutParams) tvContent.getLayoutParams()).removeRule(RelativeLayout.BELOW);
                }
                ((RelativeLayout.LayoutParams) tvContent.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                ((RelativeLayout.LayoutParams) tvTitle.getLayoutParams()).addRule(RelativeLayout.LEFT_OF, R.id.tvWithTitleTextViewContent);
                break;

            default:
                if (ApiLevel.ATLEAST_JB_MR1) {
                    ((RelativeLayout.LayoutParams) tvTitle.getLayoutParams()).removeRule(RelativeLayout.LEFT_OF);
                    ((RelativeLayout.LayoutParams) tvContent.getLayoutParams()).removeRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                }
                ((RelativeLayout.LayoutParams) tvContent.getLayoutParams()).addRule(RelativeLayout.BELOW, R.id.tvWithTitleTextViewTitle);
        }

//        if (content_margin != -1) {
//            TextViewUtils.setMargin(tvContent, content_margin, 0, content_margin, 0);
//        } else {
//            TextViewUtils.setMargin(tvContent, content_margin_left, 0, content_margin_right, 0);
//        }


        if (content_size != -1) {
            tvContent.setTextSize(DensityUtils.px2dip(context, content_size));
        }

        tvContent.setTextColor(content_color);

        if (!StringUtils.isEmptyOrNullStr(contentString)) {
            setContent(contentString);
        }
    }

    //    public void setPadding(int left, int top, int right, int bottom){
//        llContainer.setPadding( left,  top,  right,  bottom);
//    }


    public void setAppearance(@StyleRes int styleResId) {
        final TypedArray ta = getContext().obtainStyledAttributes(styleResId, R.styleable.WithTitleTextView);
        setStyle(getContext(), ta);
    }

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
        if (text == null || StringUtils.isEmptyOrNullStr(text.toString())) {
            tvContent.setVisibility(GONE);
            ((RelativeLayout.LayoutParams) tvTitle.getLayoutParams()).addRule(RelativeLayout.CENTER_VERTICAL);
        } else {
            tvContent.setVisibility(VISIBLE);
            tvContent.setText(text);
            if ("end".equals(contentPosition)) {
                if (ApiLevel.ATLEAST_JB_MR1) {
                    ((RelativeLayout.LayoutParams) tvTitle.getLayoutParams()).removeRule(RelativeLayout.CENTER_VERTICAL);
                }
            } else {
                ((RelativeLayout.LayoutParams) tvTitle.getLayoutParams()).addRule(RelativeLayout.CENTER_VERTICAL);
            }
        }
    }

    /**
     * @param textResId activity返回过来的文字
     * @return void 返回类型
     * @Title setContent
     * @Description 设置activity返回过来的文字
     */
    public void setContent(int textResId) {
        setContent(getResources().getString(textResId));
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