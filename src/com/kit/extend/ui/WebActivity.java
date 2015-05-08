package com.kit.extend.ui;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.kit.app.interfaces.IWhere2Go;
import com.kit.extend.R;
import com.kit.ui.BaseActionBarActivity;
import com.kit.utils.MessageUtils;
import com.kit.utils.ToastUtils;
import com.kit.utils.WebViewUtils;
import com.kit.utils.ZogUtils;
import com.kit.utils.intentutils.BundleData;
import com.kit.utils.intentutils.IntentUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

public class WebActivity extends BaseActionBarActivity  implements IWhere2Go{

    public static final int WEB_LOAD_END = 99;

    private Toolbar toolbar;

    public WebView webView;


    public ProgressBar pb;


    public String content;
    private Timer timer;
    private TimerTask task;

    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {

            switch (message.what) {

                case WEB_LOAD_END:

                    ZogUtils.printLog(WebActivity.class, "WEB_LOAD_END WEB_LOAD_END");
                    webView.getSettings().setBlockNetworkImage(false);
                    pb.setVisibility(View.GONE);

                    break;


            }
            super.handleMessage(message);
        }
    };


    /**
     * 退出动画。如果子Activity想复写动画，需重新赋值
     */
    @Override
    public void where2go(BundleData bundleData) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        super.onCreate(savedInstanceState);

    }

    @Override
    public boolean initWidget() {
        setContentView(R.layout.activity_web);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.webView);
        pb = (ProgressBar) findViewById(R.id.pb);
        setToolbar();

//        WithUTools.setToolbar(this, container, toolbar);


//        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        webView.setWebViewClient(new WebViewClient() {


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                // Handle the error
                ToastUtils.mkShortTimeToast(WebActivity.this, getString(R.string.load_error));
            }

            @Override
            public void onReceivedSslError(WebView view,
                                           SslErrorHandler handler,
                                           android.net.http.SslError error) {
                // 重写此方法可以让webview处理https请求 
                handler.proceed();
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {


                                       public void onProgressChanged(WebView view, int progress) {

                                           ZogUtils.printLog(WebActivity.class, progress + "");
                                           pb.setProgress(progress);
                                           if (progress >= 100) {
                                               cancelTimer();
                                               pb.setVisibility(View.GONE);
                                               webView.getSettings().setBlockNetworkImage(false);
                                           } else {
                                               webView.getSettings().setBlockNetworkImage(true);
                                               pb.setVisibility(View.VISIBLE);
                                               startTimerTask();

                                           }
                                           super.onProgressChanged(view, progress);
                                       }
                                   }
        );

        return super.initWidget();
    }

    @Override
    public boolean getExtra() {

        BundleData bundleData = IntentUtils.getInstance().getData();
        content = (String) bundleData.getObject("content");

        return super.getExtra();
    }

    @Override
    public boolean initWidgetWithData() {
        if (content.startsWith("http://") || content.startsWith("https://"))
            WebViewUtils.loadUrl(WebActivity.this, webView, content, true);
        else if (content.startsWith("www."))
            WebViewUtils.loadUrl(WebActivity.this, webView, "http://" + content, true);
        else
            WebViewUtils.loadContent(WebActivity.this, webView, content);

//        webView.loadUrl(content);

        return super.initWidgetWithData();
    }




    private void callHiddenWebViewMethod(String name) {
        if (webView != null) {
            try {
                Method method = WebView.class.getMethod(name);
                method.invoke(webView);
            } catch (NoSuchMethodException e) {
                ZogUtils.printLog(WebActivity.class, "No such method: " + name + e.toString());
            } catch (IllegalAccessException e) {
                ZogUtils.printLog(WebActivity.class, "Illegal Access: " + name + e.toString());
            } catch (InvocationTargetException e) {
                ZogUtils.printLog(WebActivity.class, "Invocation Target Exception: " + name + e.toString());
            }
        }
    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(0x00000000);
        toolbar.setTitle("返回");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ZogUtils.printLog(WebActivity.class, "Build.VERSION.SDK_INT:" + Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            getSupportActionBar().setHomeActionContentDescription(R.string.back);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.back);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    /**
     * 为了防止说页面一直没加载完，5秒后强制加载图片，算加载完（伪完）
     */
    public void startTimerTask() {
        cancelTimer();
        //LogUtils.printLog(getClass(), "startTimerTask");
        task = new TimerTask() {
            public void run() {
                if (pb.getProgress() <= 100)
                    MessageUtils.sendMessage(mHandler, WEB_LOAD_END);
            }
        };
        timer = new Timer();

        timer.schedule(task, 5 * 1000);
    }

    private void cancelTimer() {
        if (task != null) {
            task.cancel(); // 将原任务从队列中移除
        }

        if (timer != null) {
            timer.cancel();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.pauseTimers();

        if (isFinishing()) {
            webView.loadUrl("about:blank");
            setContentView(new FrameLayout(this));
        }
        callHiddenWebViewMethod("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        callHiddenWebViewMethod("onResume");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.stopLoading();
        webView.clearCache(true);
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
    }


    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            if (webView.canGoBack()) {
                // 返回键退回
                webView.goBack();
            } else {
                this.finish();
                where2go(null);
            }
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

}
