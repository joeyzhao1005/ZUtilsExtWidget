package com.kit.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class DefaultDialog extends AbstractDialog {

	private ProgressDialog pd;

	private boolean isAbleGotoNext;

	private String content;

	private int layoutId;
	private boolean haveCancel;

	public DefaultDialog(Context context, String content, int layoutId,
			boolean haveCancel) {
		super(context, content, layoutId);
		this.mContext = context;
		this.content = content;
		this.layoutId = layoutId;
		this.haveCancel = haveCancel;
	}

	@Override
	public void setContent(String content) {
		// TODO Auto-generated method stub
		mTVContent.setText(content);
	}

	@Override
	protected void onButtonOK() {
		// TODO Auto-generated method stub
		super.onButtonOK();

	}

	/**
	 * @return the isAbleGotoNext
	 */
	public boolean isAbleGotoNext() {
		return isAbleGotoNext;
	}

	/**
	 * @param isAbleGotoNext
	 *            the isAbleGotoNext to set
	 */
	public void setAbleGotoNext(boolean isAbleGotoNext) {
		this.isAbleGotoNext = isAbleGotoNext;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (haveCancel) {
			this.mButtonCancel.setVisibility(View.VISIBLE);

		} else {
			this.mButtonCancel.setVisibility(View.GONE);
		}

	}

}
