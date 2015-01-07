package com.kit.widget.imageview.photoview.sample;

import android.app.Activity;
import android.os.Bundle;

import com.kit.extend.widget.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.kit.widget.imageview.photoview.PhotoView;

public class AUILSampleActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoview_activity_simple);

        PhotoView photoView = (PhotoView) findViewById(R.id.iv_photo);

        if (!ImageLoader.getInstance().isInited()) {
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
            ImageLoader.getInstance().init(config);
        }

        ImageLoader.getInstance().displayImage("http://pbs.twimg.com/media/Bist9mvIYAAeAyQ.jpg", photoView);
    }
}
