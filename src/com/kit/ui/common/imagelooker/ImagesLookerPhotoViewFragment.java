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
import android.widget.ImageView;

import com.kit.app.model.ImageData;
import com.kit.extend.widget.R;
import com.kit.ui.BaseV4Fragment;
import com.kit.utils.AppUtils;
import com.kit.utils.DeviceUtils;
import com.kit.utils.FileUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.MathExtend;
import com.kit.utils.MessageUtils;
import com.kit.utils.StringUtils;
import com.kit.utils.ToastUtils;
import com.kit.utils.bitmap.BitmapUtils;
import com.kit.widget.imageview.photoview.GifView;
import com.kit.widget.imageview.photoview.PhotoView;
import com.kit.widget.imageview.photoview.PhotoViewAttacher;
import com.kit.widget.progressbar.RoundProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;

import java.io.File;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class ImagesLookerPhotoViewFragment extends BaseV4Fragment implements ImagesLookerActivity.OnPageSelectedListener {

    private String thumbnail_pic, bmiddle_pic, original_pic;

    public String useUrl = "";

    private PhotoViewAttacher mAttacher;


    private ImageView imageView;

    private GifView gifImageView;
    private PhotoView mPhotoView;

    private RoundProgressBar rpb;

    public Handler mHandler = new Handler() {
        //用handler的目的在于保证加载图片，更新视图的时候在主线程中，保证线程安全
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    loadImg();
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @TargetApi(11)
    @Override
    public View initWidget(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {

        super.initWidget(inflater, container, savedInstanceState);

        View contextView = inflater.inflate(R.layout.imageslooker_photoview_fragment, container, false);

//        setContentView(R.layout.activity_imagelooker_main);

        mPhotoView = (PhotoView) contextView.findViewById(R.id.photoview);
        gifImageView = (GifView) contextView.findViewById(R.id.gifImageView);

        mPhotoView.setBackgroundColor(0);
        mPhotoView.setAdapterWidth(true);
        // The MAGIC happens here!
//        mAttacher = new PhotoViewAttacher(mPhotoView);

        // Lets attach some listeners, not required though!
//        mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
//        mAttacher.setOnPhotoTapListener(new PhotoTapListener());
//        mAttacher.setAdapterWidth(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPhotoView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        rpb = (RoundProgressBar) contextView.findViewById(R.id.rpb);

//        LogUtils.printLog(ImagesLookerPhotoViewFragment.class, "rpb:" + rpb);
//        Drawable bitmap = getResources().getDrawable(R.drawable.default_pic);
//        mPhotoView.setImageDrawable(bitmap);

        // The MAGIC happens here!


//        mAttacher.setScale(1f, 0, 0, true);

        imageView = mPhotoView;
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


//        LogUtils.printLog(ImagesLookerPhotoViewFragment.class, "useUrl:" + useUrl);


        return super.getExtra();
    }


    @Override
    public boolean initWidgetWithData() {

//        LogUtils.printLog(ImagesLookerPhotoViewFragment.class,"FileUtils.getSuffix(useUrl):"+FileUtils.getSuffix(useUrl));

        //如果是gif图片，把photoview 隐藏掉，把gifimageview展示出来
        if (!StringUtils.isEmptyOrNullOrNullStr(useUrl) &&
                !StringUtils.isEmptyOrNullOrNullStr(FileUtils.getSuffix(useUrl)) &&
                FileUtils.getSuffix(useUrl).equals("gif")) {
            mPhotoView.setVisibility(View.GONE);
            gifImageView.setVisibility(View.VISIBLE);
            imageView = gifImageView;
        }

//        String thumbnail_pic_dir = ImageLoader.getInstance().getDiskCache().get(thumbnail_pic).getPath();

        if (isLoaded(thumbnail_pic)) {
            showImage(thumbnail_pic);
        }

        return super.initWidgetWithData();
    }

    @Override
    public void onPageSelected() {

//        LogUtils.printLog(ImagesLookerPhotoViewFragment.class, "onPageSelected!!!!!!!!!!! useUrl:" + useUrl);

        if (rpb == null) {//妈的，adapter布局有时候加载比较慢，需要延迟显示
            yanchixianshi();
        } else {
            MessageUtils.sendMessage(mHandler, 0);
        }
    }


    private void yanchixianshi() {
        new Thread(new Runnable() {
            public void run() {

                AppUtils.sleep(200);
                if (rpb == null) {
                    yanchixianshi();
                } else
                    MessageUtils.sendMessage(mHandler, 0);

            }
        }).start();
    }

    public void loadImg() {
        if (isLoaded(useUrl)) {//如果本地缓存有，就从本地缓存加载
            showImage(useUrl);
        } else {
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
                    if (isLoaded(s)) {
                        showImage(s);
                    } else {
                        rpb.setVisibility(View.GONE);

                        ImageLoader.getInstance().cancelDisplayTask(imageView);
                        FileUtils.deleteFile(ImageLoader.getInstance().getDiskCache().get(s));

                        if (isAdded()) {
                            String msg = getString(R.string.get_pic_failed);
                            ToastUtils.mkLongTimeToast(mContext, msg);
                        }
                    }
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
                }
            });
        }
    }

    private void showImage(String url) {
        String filedir = ImageLoader.getInstance().getDiskCache().get(url).getPath();
        if (imageView instanceof PhotoView) {
            Bitmap bitmap = BitmapUtils.generateBitmapFile(filedir, null);

            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            int screenWidth = DeviceUtils.getScreenWidth(mContext);
//                int screenHeight = DeviceUtils.getScreenHeight(mContext);
//                float scale = (float) MathExtend.divide((double) screenWidth, (double) bitmapWidth);

            ZogUtils.printLog(ImagesLookerPhotoViewFragment.class, "screenWidth:" + screenWidth
                    + " bitmapWidth:" + bitmapWidth + " bitmapHeight:" + bitmapHeight
                    + " imageView.getScaleType():" + imageView.getScaleType());

            imageView.setImageBitmap(bitmap);

        } else if (imageView instanceof GifView) {
            GifDrawable gifFromPath = getGif(filedir);
            ((GifImageView) imageView).setImageDrawable(gifFromPath);
        }

        rpb.setVisibility(View.GONE);

    }

    private GifDrawable getGif(String filedir) {
        GifDrawable gifFromPath = null;
        try {
            gifFromPath = new GifDrawable(filedir);
        } catch (Exception e) {
            gifFromPath = getGif(filedir);
//            LogUtils.showException(e);
        }

        return gifFromPath;
    }

    @Override
    public boolean loadData() {
        return super.loadData();
    }

    private boolean isLoaded(String url) {
        File file = ImageLoader.getInstance().getDiskCache().get(url);
        boolean isLoaded = file.exists();
        ZogUtils.printLog(ImagesLookerPhotoViewFragment.class, url + " isLoaded():" + isLoaded);

        return isLoaded;
    }
}
