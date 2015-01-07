package com.kit.widget.selector.selectonefromlist;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.ui.BaseActivity;
import com.kit.utils.IntentUtils;
import com.kit.utils.ZogUtils;

/**
 * 
 * @ClassName MyActivity
 * @Description 公共选择小界面
 * @author Echo 1149892817@qq.com
 * @date 2014-6-3
 * 
 */

public class SelectOneFromListActivity extends BaseActivity implements
		OnClickListener {

	private Context mContext;

	private ListView rblv;

	private LinearLayout llLeft, llTitle;

	private TextView tvTitle;

	private MyTask1 mTask1;

	// public static int selectedsPos = -2;

	private SelectOneFromListAdapter csara;

	private int selectedPosition;

	private String title;

	// String[] item = { "选我", "哦，不，选我", "选我得永生", "不选我会疯", "咦，鄙视楼上，切" };
	private ArrayList<String> item = new ArrayList<String>();

	private int startPosition = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public boolean loadData() {

		doTask1();

		super.loadData();
		return true;
	}

	@Override
	public boolean getExtra() {
		Bundle bundle = this.getIntent().getExtras();

		title = bundle
				.getString(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_ACTIVITY_TITLE);
		item = bundle
				.getStringArrayList(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_ITEMS_ARRAYLIST);
		selectedPosition = bundle
				.getInt(SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_SELECTED_POSITION);

		return super.getExtra();
	}

	@Override
	public boolean initWidget() {
		mContext = this;
		setContentView(R.layout.select_one_from_list_activity);

		llLeft = (LinearLayout) findViewById(R.id.llLeft);
		llTitle = (LinearLayout) findViewById(R.id.llTitle);
		tvTitle = (TextView) findViewById(R.id.tvTitle);

		llTitle.setVisibility(View.VISIBLE);
		tvTitle.setText(title);

		rblv = (ListView) findViewById(R.id.rblv);
		//ScrollUtils.disableOVER_SCROLL_NEVER(rblv);
		
		csara = new SelectOneFromListAdapter(mContext, selectedPosition, item);
		rblv.setAdapter(csara);

		llLeft.setOnClickListener(this);

		rblv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentView, View view,
					int position, long id) {

				// if (selectedPosition != position) {
				// selectedPosition = selectedPosition - startPosition;

				if (selectedPosition >= startPosition) {
					View viewItem = parentView.getChildAt(selectedPosition);

					viewItem.findViewById(R.id.iv).setVisibility(View.GONE);
					selectedPosition = position;
					ImageView iv = (ImageView) view.findViewById(R.id.iv);
					iv.setVisibility(View.VISIBLE);
				} else {
					selectedPosition = position;
					ImageView iv = (ImageView) view.findViewById(R.id.iv);
					iv.setVisibility(View.VISIBLE);
				}

				Bundle bundle = new Bundle();

				bundle.putString(
						SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_SELECTED_ITEM_STRING,
						item.get(position));
				bundle.putInt(
						SelectOneFromListConstant.SELECT_ONE_FROM_LIST_EXTRAS_KEY_SELECTED_POSITION,
						selectedPosition);

				ZogUtils.printLog(getClass(),
                        selectedPosition + " " + item.get(position));

				IntentUtils
						.gotoNextActivity(
								mContext,
								bundle,
								SelectOneFromListConstant.ACTIVITY_ON_RESULT_SELECT_ONE_FROM_LIST_REQUEST,
								true);
			}

			// }
		});

		return true;
	}

	public void doTask1() {

		if (mTask1 != null) {
			mTask1.cancel(true);
			mTask1 = new MyTask1();
			mTask1.execute();
		} else {
			mTask1 = new MyTask1();
			mTask1.execute();
		}
	}

	private class MyTask1 extends AsyncTask<Void, Void, ArrayList<String>> {
		// onPreExecute方法用于在执行后台任务前做一些UI操作
		@Override
		protected void onPreExecute() {

		}

		// doInBackground方法内部执行后台任务,不可在此方法内修改UI
		@Override
		protected ArrayList<String> doInBackground(Void... params) {

			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {

		}

	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.llLeft) {
			this.finish();
		}

	}

	@Override
	protected void onDestroy() {

		if (mTask1 != null) {
			mTask1.cancel(true);
			mTask1 = null;
		}

		super.onDestroy();
	}

}
