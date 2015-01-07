package com.kit.widget.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kit.extend.widget.R;

public class WithDelEditText extends LinearLayout {

    private ImageView ivWithDelEditTextDeleteIcon;
    private EditText et;
    private String hintString;
    private Drawable WithDelEditTextDeleteIcon;
    public OnCheckValueListenter checkListenter;
    public onFocusValueListenter focusCheckListenter;

    public WithDelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 方式1获取属性
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.WithDelEditText);

        hintString = a
                .getString(R.styleable.WithDelEditText_WithDelEditText_hint);

        WithDelEditTextDeleteIcon = a
                .getDrawable(R.styleable.WithDelEditText_WithDelEditText_delete_icon);

        a.recycle();

        View view = LayoutInflater.from(context).inflate(
                R.layout.with_del_edittext, null);

        ivWithDelEditTextDeleteIcon = (ImageView) view
                .findViewById(R.id.iv_with_del_eidttext_delete);
        if (WithDelEditTextDeleteIcon != null)
            ivWithDelEditTextDeleteIcon
                    .setImageDrawable(WithDelEditTextDeleteIcon);

        et = (EditText) view.findViewById(R.id.et_with_del_edittext);
        if (!TextUtils.isEmpty(hintString))
            et.setHint(hintString);

        ivWithDelEditTextDeleteIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
            }
        });
        // 给编辑框添加文本改变事件
        et.addTextChangedListener(new MyTextWatcher());
        et.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
//				Log.v("Steel", "arg1:"+arg1+";"+et.hasFocus());
                if (et.hasFocus()) {
                    if (focusCheckListenter != null) {
                        focusCheckListenter.setOnFocusValue(true);
                    }
                } else {
                    if (focusCheckListenter != null) {
                        focusCheckListenter.setOnFocusValue(false);
                    }
                }
            }
        });
        // 把获得的view加载到这个控件中
        addView(view);
    }

    // 文本观察者
    private class MyTextWatcher implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        // 当文本改变时候的操作
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
            // TODO Auto-generated method stub
            // 如果编辑框中文本的长度大于0就显示删除按钮否则不显示
            if (s.length() > 0) {
                ivWithDelEditTextDeleteIcon.setVisibility(View.VISIBLE);
                if (s.length() == 11) {
                    if (checkListenter != null) {
                        checkListenter.setOnCheckValue(true);
                    }
                }
            } else {
                ivWithDelEditTextDeleteIcon.setVisibility(View.GONE);
            }
        }

    }

    public void setOnCheckValueListener(OnCheckValueListenter listener) {

        this.checkListenter = listener;
    }

    public void setOnFocusValueListenter(onFocusValueListenter listener) {
        this.focusCheckListenter = listener;
    }

    //定义回调接口
    public interface OnCheckValueListenter {
        public void setOnCheckValue(boolean isCheck);
    }

    public interface onFocusValueListenter {
        public void setOnFocusValue(boolean isCheck);
    }

    public void setText(CharSequence text) {
        et.setText(text);

    }

    public Editable getText() {
        return et.getText();

    }

    public EditText getEditText() {
        return et;
    }

    public void setEditText(EditText et) {
        this.et = et;
    }


}