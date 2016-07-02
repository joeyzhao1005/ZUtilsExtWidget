package com.kit.widget.update;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.kit.extend.widget.R;
import com.kit.utils.NotificationUtils;
import com.kit.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateService extends Service {

    // private UpdateInfo updateInfo;
    // 标题
    private String title;

    // 文件存储
    private File updateDir = null;
    private File updateFile = null;
    private String netDownloadUrl, clickNotify2Activity;

    private final static int DOWNLOAD_COMPLETE = 0;
    private final static int DOWNLOAD_FAIL = 1;
    // 通知栏
    private NotificationManager updateNotificationManager = null;
    private Notification updateNotification = null;
    // 通知栏跳转Intent
    private Intent updateIntent = null;
    private PendingIntent updatePendingIntent = null;

    public String downloadUrl;
    // public int drawableIcon;
    public Context context;

    private Handler updateHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWNLOAD_COMPLETE:
                    // 点击安装PendingIntent
                    Uri uri = Uri.fromFile(updateFile);
                    Intent installIntent = new Intent(Intent.ACTION_VIEW);
                    installIntent.setDataAndType(uri,
                            "application/vnd.android.package-archive");
                    updatePendingIntent = PendingIntent.getActivity(
                            UpdateService.this, 0, installIntent, 0);

                    updateNotification = NotificationUtils.mkNotity(context
                            , updateNotificationManager
                            , installIntent
                            , updatePendingIntent
                            , ""
                            , title
                            , "下载完成,点击安装。"
                            , android.R.drawable.ic_dialog_info
                            , -1
                            , Notification.DEFAULT_SOUND
                            , Notification.FLAG_AUTO_CANCEL
                            , 0, 0);

                    ToastUtils.mkShortTimeToast(context, "更新包下载完成");


                    // 停止服务
                    stopService(updateIntent);

                    // 自动安装
                    Intent install = new Intent();
                    install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.setAction(android.content.Intent.ACTION_VIEW);
                    install.setDataAndType(uri,
                            "application/vnd.android.package-archive");
                    startActivity(install);// 安装

                case DOWNLOAD_FAIL:
                    // 下载失败
                    updateNotification = NotificationUtils.mkNotity(context
                            , updateNotificationManager
                            , null
                            , updatePendingIntent
                            , ""
                            , title
                            , "下载失败,点击重试。"
                            , android.R.drawable.ic_dialog_info
                            , -1
                            , Notification.DEFAULT_SOUND
                            , Notification.FLAG_AUTO_CANCEL
                            , 0, 0);
                default:
                    stopService(updateIntent);
            }
        }
    };

    @Override
    public void onCreate() {
        System.out.println("updateservice start");
        context = UpdateService.this;

        // newNotify();

    }

    ;

    @Override
    public void onStart(Intent intent, int startId) {
        // 获取传值

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            title = getString(R.string.update) + bundle.getString("title");
            netDownloadUrl = bundle.getString("netDownloadUrl");
            clickNotify2Activity = bundle.getString("clickNotify2Activity");

            // updateInfo = (UpdateInfo) bundle.getSerializable("updateInfo");

            // 创建文件
            if (android.os.Environment.MEDIA_MOUNTED
                    .equals(android.os.Environment.getExternalStorageState())) {
                downloadUrl = Environment.getExternalStorageDirectory()
                        + "/Download/";
                updateDir = new File(Environment.getExternalStorageDirectory(),
                        downloadUrl);
                updateFile = new File(updateDir.getPath(), title + ".apk");

                updateNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                // 设置下载过程中，点击通知栏，回到主界面
                updateIntent = new Intent();
                updateIntent.setClassName(this, clickNotify2Activity);
                updatePendingIntent = PendingIntent.getActivity(this, 0,
                        updateIntent, 0);


                updateNotification = NotificationUtils.mkNotity(context
                        , updateNotificationManager
                        , updateIntent
                        , updatePendingIntent
                        , "开始下载更新包"
                        , title
                        , "0%"
                        , R.drawable.update_new
                        , -1
                        , Notification.DEFAULT_SOUND
                        , Notification.FLAG_AUTO_CANCEL
                        , 0, 0);


                ToastUtils.mkShortTimeToast(context,
                        updateNotification.tickerText.toString());

                // 开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
                new Thread(new updateRunnable()).start();// 这个是下载的重点，是下载的过程
            } else {
                String msg = "请插入sd卡之后再进行更新操作";
                ToastUtils.mkLongTimeToast(context, msg);
            }

        }

        super.onStart(intent, startId);
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    class updateRunnable implements Runnable {
        Message message = updateHandler.obtainMessage();

        public void run() {
            message.what = DOWNLOAD_COMPLETE;
            try {
                // 增加权限<uses-permission
                // android:name="android.permission.WRITE_EXTERNAL_STORAGE">;
                if (!updateDir.exists()) {
                    updateDir.mkdirs();
                }
                if (!updateFile.exists()) {
                    updateFile.createNewFile();
                }
                // 下载函数，以QQ为例子
                // 增加权限<uses-permission
                // android:name="android.permission.INTERNET">;

                System.out.println(netDownloadUrl);
                long downloadSize = downloadUpdateFile(netDownloadUrl,
                        updateFile);
                if (downloadSize > 0) {
                    // 下载成功
                    updateHandler.sendMessage(message);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                message.what = DOWNLOAD_FAIL;
                // 下载失败
                updateHandler.sendMessage(message);
            }
        }
    }

    public long downloadUpdateFile(String downloadUrl, File saveFile)
            throws Exception {
        // 这样的下载代码很多，我就不做过多的说明
        int downloadCount = 0;
        int currentSize = 0;
        long totalSize = 0;
        int updateTotalSize = 0;

        HttpURLConnection httpConnection = null;
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URL url = new URL(downloadUrl);
            httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection
                    .setRequestProperty("User-Agent", "PacificHttpClient");
            if (currentSize > 0) {
                httpConnection.setRequestProperty("RANGE", "bytes="
                        + currentSize + "-");
            }
            httpConnection.setConnectTimeout(10000);
            httpConnection.setReadTimeout(20000);
            updateTotalSize = httpConnection.getContentLength();
            if (httpConnection.getResponseCode() == 404) {
                throw new Exception("fail!");
            }
            is = httpConnection.getInputStream();
            fos = new FileOutputStream(saveFile, false);
            byte buffer[] = new byte[4096];
            int readsize = 0;
            while ((readsize = is.read(buffer)) > 0) {
                fos.write(buffer, 0, readsize);
                totalSize += readsize;
                // 为了防止频繁的通知导致应用吃紧，百分比增加10才通知一次
                if ((downloadCount == 0)
                        || (int) (totalSize * 100 / updateTotalSize) - 5 > downloadCount) {
                    downloadCount += 5;
                    updateNotification = NotificationUtils.mkNotity(context
                            , updateNotificationManager
                            , null
                            , updatePendingIntent
                            , ""
                            , title
                            ,(int) totalSize * 100 / updateTotalSize + "%"
                            , android.R.drawable.ic_dialog_info
                            , -1
                            , Notification.DEFAULT_SOUND
                            , Notification.FLAG_AUTO_CANCEL
                            , 0, 0);

                }
            }
        } finally {
            if (httpConnection != null) {
                httpConnection.disconnect();
            }
            if (is != null) {
                is.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return totalSize;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
