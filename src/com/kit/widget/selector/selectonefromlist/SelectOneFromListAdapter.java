package com.kit.widget.selector.selectonefromlist;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.utils.ListUtils;

public class SelectOneFromListAdapter extends BaseAdapter {
	protected Context mContext;
	protected LayoutInflater mInflater;

	ArrayList<String> str;

	private int selectedPosition;

	public SelectOneFromListAdapter(Context mContext, int selectPosition,
			ArrayList<String> str) {
		this.mContext = mContext;
		this.selectedPosition = selectPosition;
		this.str = str;

	}

	@Override
	public int getCount() {

		return str.size();

	}

	@Override
	public Object getItem(int position) {
		return str;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder vHolder = null;
		// 去掉缓存数据 防止视图重复使用产生多选的情况
		// if (convertView == null) {
		convertView = (RelativeLayout) LayoutInflater.from(mContext).inflate(
				R.layout.select_one_from_list_item, null);
		vHolder = new ViewHolder();
		vHolder.tv = (TextView) convertView.findViewById(R.id.tv);
		vHolder.iv = (ImageView) convertView.findViewById(R.id.iv);
		convertView.setTag(vHolder);
		// } else {
		// vHolder = (ViewHolder) convertView.getTag();
		// }

		if (!ListUtils.isNullOrEmpty(str))
			vHolder.tv.setText(str.get(position));

		if (selectedPosition >= 0) {
			if (position == selectedPosition) {
				vHolder.iv.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

	class ViewHolder {
		TextView tv;
		ImageView iv;
	}
}
