package com.kit.ui.common.imagelooker;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kit.app.model.ImageData;
import com.kit.app.model.Model;
import com.kit.extend.widget.R;
import com.kit.ui.BaseSwipeBackV4FragmentActivity;
import com.kit.utils.IntentUtils;
import com.kit.utils.ZogUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class ImagesLookerActivity extends BaseSwipeBackV4FragmentActivity {

    private ViewPager pager;
    private ArrayList<ImageData> imageDataList;
    private FragmentPagerAdapter adapter;

    private int witchClicked = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean getExtra() {

        Model model = (Model) IntentUtils.getInstance().getData();
        witchClicked = model.flagInt[0];
        imageDataList = (ArrayList<ImageData>) model.flagObject;

        return super.getExtra();
    }

    @TargetApi(11)
    @Override
    public boolean initWidget() {

//        try {
//            ActionBar actionBar = getSupportActionBar();
//            actionBar.setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
//            ActionBarUtils.setHomeBack(this, R.drawable.ic_back, R.string.back);
//        } catch (Exception e) {
//            ZogUtils.printLog(ImagesLookerActivity.class, "版本过低，不支持");
//        }
        setContentView(R.layout.imageslooker_activity);

        //ViewPager的adapter
        adapter = new PageIndicatorAdapter(getSupportFragmentManager(), imageDataList);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        //如果我们要对ViewPager设置监听，用indicator设置就行了
        indicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
//                ToastUtils.mkToast(mContext, position + "", 500);
                ((ImagesLookerPhotoViewFragment) adapter.getItem(position)).onPageSelected();
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


        if (imageDataList.size() == 1) {
            indicator.setVisibility(View.GONE);
        }

        ZogUtils.printLog(ImagesLookerActivity.class, "initWidget ok");
        return super.initWidget();
    }

    @Override
    public boolean loadData() {

        return super.loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //点击了哪一个，显示哪一个
        pager.setCurrentItem(witchClicked);
        ((ImagesLookerPhotoViewFragment) adapter.getItem(witchClicked)).onPageSelected();
        ZogUtils.printLog(ImagesLookerActivity.class, "onStart ok");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.photoview_main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        ZogUtils.printLog(ImagesLookerPhotoViewFragment.class, "onDestroy");

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    public interface OnPageSelectedListener {
        public void onPageSelected();
    }

}
