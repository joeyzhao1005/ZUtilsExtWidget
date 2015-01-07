/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.kit.ui.common.imagelooker;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.kit.extend.widget.R;
import com.kit.app.model.ImageData;
import com.kit.ui.BaseV4Fragment;
import com.kit.utils.AppUtils;
import com.kit.utils.DeviceUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.MathExtend;
import com.kit.utils.MessageUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.WebViewUtils;
import com.kit.widget.progressbar.RoundProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

public class ImagesLookerWebViewFragment extends BaseV4Fragment implements ImagesLookerActivity.OnPageSelectedListener {

    private String thumbnail_pic, bmiddle_pic, original_pic;

    public String useUrl = "";

    public WebView mImageView;
    public RoundProgressBar rpb;

    public Handler mHandler = new Handler() {
        //用handler的目的在于保证加载图片，更新视图的时候在主线程中，保证线程安全
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    loadImage();
                    break;

            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @TargetApi(11)
    @Override
    public View initWidget(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {


        View contextView = inflater.inflate(R.layout.imageslooker_fragment, container, false);

//        setContentView(R.layout.activity_imagelooker_main);

        mImageView = (WebView) contextView.findViewById(R.id.iv_photo);
        mImageView.setBackgroundColor(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mImageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        rpb = (RoundProgressBar) contextView.findViewById(R.id.rpb);

        ZogUtils.printLog(ImagesLookerWebViewFragment.class, "rpb:" + rpb);
//        Drawable bitmap = getResources().getDrawable(R.drawable.default_pic);
//        mImageView.setImageDrawable(bitmap);

        // The MAGIC happens here!


//        mAttacher.setScale(1f, 0, 0, true);

        return contextView;
    }

    @Override
    public boolean getExtra() {

        Bundle mBundle = getArguments();
        ImageData imageData = (ImageData) mBundle.getSerializable("arg");
        thumbnail_pic = imageData.thumbnail_pic;
        if (!StringUtils.isNullOrEmpty(thumbnail_pic))
            useUrl = thumbnail_pic;

        bmiddle_pic = imageData.bmiddle_pic;
        if (!StringUtils.isNullOrEmpty(bmiddle_pic))
            useUrl = bmiddle_pic;

        original_pic = imageData.original_pic;
        if (!StringUtils.isNullOrEmpty(original_pic))
            useUrl = original_pic;

        return super.getExtra();
    }

    @Override
    public boolean loadData() {


        WebViewUtils.showLocalImage(mContext, mImageView,
                ImageLoader.getInstance().getDiskCache().get(thumbnail_pic).getPath(), 0, true, false);


//        ImageLoader.getInstance().displayImage(useUrl, mImageView);


        return super.loadData();
    }

    @Override
    public void onPageSelected() {

        ZogUtils.printLog(ImagesLookerWebViewFragment.class, "onPageSelected!!!!!!!!!!! useUrl:" + useUrl);

        if (rpb == null) {//妈的，adapter布局有时候加载比较慢，需要延迟显示
            yanchixianshi();
        } else {
            MessageUtils.sendMessage(mHandler, 0);
        }
    }


    private void yanchixianshi() {
        new Thread(new Runnable() {
            public void run() {

                AppUtils.sleep(100);
                if (rpb == null) {
                    yanchixianshi();
                }
                MessageUtils.sendMessage(mHandler, 0);

            }
        }).start();
    }

    public void loadImage() {
        ImageLoader.getInstance().loadImage(useUrl, null, null, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String s, View view) {
                rpb.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                rpb.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                rpb.setVisibility(View.GONE);

                int bitmapWidth = bitmap.getWidth();
                int bitmapHeight = bitmap.getHeight();

                int screenWidth = DeviceUtils.getScreenWidth(mContext);
                int screenHeight = DeviceUtils.getScreenHeight(mContext);
                float scale = (float) MathExtend.divide((double) screenWidth, (double) bitmapWidth);

                ZogUtils.printLog(ImagesLookerWebViewFragment.class, "screenWidth:" + screenWidth
                                + " bitmapWidth:" + bitmapWidth + " bitmapHeight:" + bitmapHeight
                                + " scale:" + scale + " mImageView.getScale():" + mImageView.getScale()
                );

                WebViewUtils.showLocalImage(mContext, mImageView,
                        ImageLoader.getInstance().getDiskCache().get(s).getPath(), 0, true, false);


            }

            @Override
            public void onLoadingCancelled(String s, View view) {
                rpb.setVisibility(View.GONE);

            }

        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {
                rpb.setVisibility(View.VISIBLE);
                int bee = (int) (MathExtend.divide(current, total) * 100);
                rpb.setProgress(bee);

//                String baifen = bee + "%";
//                ToastUtils.mkToast(mContext, "current:" + current + " total:" + total + " bee:" + bee, 500);
            }
        });
    }
}
