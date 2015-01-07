package com.kit.widget.adbanner;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.kit.extend.widget.R;
import com.kit.widget.adbanner.ADBannerView.TransitionEffect;
import com.viewpagerindicator.CirclePageIndicator;

public class ADBannerViewSampleActivity extends Activity {

	private ADBannerView mADView;

	private ADBannerView jazzy_pager_indicator;

	private ArrayList<TextView> mViews = new ArrayList<TextView>();

	private ArrayList<View> viewList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adbannnerview_activity);
		viewList = new ArrayList<View>();
		viewList.add(getTV(0));
		viewList.add(getTV(1));
		viewList.add(getTV(2));
		viewList.add(getTV(3));
		viewList.add(getTV(4));

		setADView(TransitionEffect.FlipHorizontal);

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add("Toggle Fade");
//		String[] effects = this.getResources().getStringArray(
//				R.array.adbannerview_effects);
//		for (String effect : effects)
//			menu.add(effect);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		if (item.getTitle().toString().equals("Toggle Fade")) {
//			mADView.setFadeEnabled(!mADView.getFadeEnabled());
//		} else {
//			TransitionEffect effect = TransitionEffect.valueOf(item.getTitle()
//					.toString());
//			setADView(effect);
//		}
//		return true;
//	}

	private void setADView(TransitionEffect effect) {
		mADView = (ADBannerView) findViewById(R.id.adBannerView);
		mADView.setTransitionEffect(effect);
		mADView.setAdapter(new MyAdapter());
		// 设置边距
		// mADView.setPageMargin(30);
		mADView.startAutoScroll(3000, viewList.size());
		// 设置速度
		mADView.setScrollDurationFactor(3);
		CirclePageIndicator mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		// 设置重复数目和CirclePageIndicator动画启用
		mIndicator.setViewPager(mADView, viewList.size(), true);
		mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {

				System.out.println(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return 5000;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			View text = viewList.get(position % viewList.size());
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			// destroyItem(container, position,
			// mADView.findViewFromObject(position));
			container.addView(text, lp);
			mADView.setObjectForPosition(text, position);

			return text;

		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			// TODO Auto-generated method stub
			container.removeView(mADView.findViewFromObject(position));
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}

		// @Override
		// public int getItemPosition(Object object) {
		// // TODO Auto-generated method stub
		// return super.getItemPosition(object) % viewlist.size();
		// }
		//
		//
		//
		// @Override
		// public void restoreState(Parcelable arg0, ClassLoader arg1) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public Parcelable saveState() {
		// // TODO Auto-generated method stub
		// return null;
		// }
		//
		// @Override
		// public void startUpdate(View arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void finishUpdate(View arg0) {
		// // TODO Auto-generated method stub
		//
		// }
	}

	private TextView getTV(int i) {
		TextView text = new TextView(ADBannerViewSampleActivity.this);
		text.setGravity(Gravity.CENTER);
		text.setTextSize(30);
		text.setTextColor(Color.WHITE);
		text.setText("Page " + i);
		text.setPadding(30, 30, 30, 30);
		int bg = Color.rgb((int) Math.floor(Math.random() * 128) + 64,
				(int) Math.floor(Math.random() * 128) + 64,
				(int) Math.floor(Math.random() * 128) + 64);
		text.setBackgroundColor(bg);
		return text;
	}

}
