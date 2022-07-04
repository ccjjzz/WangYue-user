package com.jiuyue.user.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.jiuyue.user.App;
import com.jiuyue.user.R;
import com.jiuyue.user.recevier.NotificationClickReceiver;
import com.jiuyue.user.utils.AppUtils;
import com.jiuyue.user.utils.ToastUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadAppService extends Service {
    private static final String TAG = "DownloadAppService";
    //通知
    private String CHANNEL_ID = "android_download_notification";
    private int NotificationID = 1;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder builder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String url = intent.getStringExtra("url");
            notificationManager = (NotificationManager) App.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                createNotificationChannel(notificationManager);
            }
            builder = getNotificationBuilder();
            downloadApk(url);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void downloadApk(String url) {
        ToastUtil.show("开始下载");
        long timeCurrentTimeMillis = System.currentTimeMillis();
        String fileDirName = AppUtils.getAppName(App.getInstance());
        String apkName = fileDirName + timeCurrentTimeMillis + ".apk";
        String fileDirPath = App.getAppContext().getFilesDir().getAbsolutePath() + fileDirName;
        downloadFile(url, fileDirPath, apkName, new onDownloadCallBack() {
                    @Override
                    public void onDownloadStart(Disposable disposable) {
                        //更新通知栏
                        builder.setContentTitle(fileDirName);
                        notificationManager.notify(NotificationID, builder.build());
                    }

                    @Override
                    public void onDownloadSuccess(File file) {
                        installApk(file);
                        //更新通知栏
                        new Handler().postDelayed(() -> {
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri uri = Uri.fromFile(file);
                            intent.setData(uri);
                            sendBroadcast(intent);
                            builder.setContentText("下载完成");
                            builder.setAutoCancel(true);
                            Intent i = new Intent(App.getAppContext(), NotificationClickReceiver.class);
                            i.putExtra("url", fileDirPath);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(App.getAppContext(), 0, i, 0);
                            builder.setContentIntent(pendingIntent);
                            notificationManager.notify(NotificationID, builder.build());
                        }, 500);
                    }

                    @Override
                    public void onDownloadLoading(int progress) {
                        //更新通知栏
                        notificationManager.notify(NotificationID, builder.build());
                        builder.setProgress(100, progress, false);
                        builder.setContentText("下载进度" + progress + "%");
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        ToastUtil.show("下载文件失败,请检查网络!");
                        stopSelf();
                        //更新通知栏
                        builder.setAutoCancel(true);
                        notificationManager.cancel(NotificationID);
                    }
                });
    }

    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri data;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String applicationId = AppUtils.getPackageName(App.getAppContext());
            data = FileProvider.getUriForFile(App.getAppContext(),  applicationId+ ".provider.fileProvider", file);
        } else {
            data = Uri.fromFile(file);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        App.getAppContext().startActivity(intent);
        stopSelf();
    }

    /**
     * 获取下载链接并回调
     * @param downloadUrl
     * @param path
     * @param fileName
     * @param callBack
     */
    public void downloadFile(String downloadUrl, String path, String fileName, onDownloadCallBack callBack) {
        Observable.create((ObservableOnSubscribe<File>) emitter ->
                download(downloadUrl, path, fileName, new onDownloadCallBack() {
                    @Override
                    public void onDownloadStart(Disposable disposable) {
                    }

                    @Override
                    public void onDownloadSuccess(File file) {
                        if (file != null) {
                            Log.d(TAG, "文件下载成功");
                            emitter.onNext(file);
                        } else {
                            Log.d(TAG, "文件下载错误");
                            if (callBack != null) {
                                callBack.onDownloadFailed(null);
                            }
                        }
                    }

                    @Override
                    public void onDownloadLoading(int progress) {
                        Log.d(TAG, "文件下载进度:" + progress);
                        if (callBack != null) {
                            callBack.onDownloadLoading(progress);
                        }
                    }

                    @Override
                    public void onDownloadFailed(Exception e) {
                        Log.d(TAG, "文件下载错误:" + e);
                        if (callBack != null) {
                            callBack.onDownloadFailed(e);
                        }
                    }
                })).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        if (callBack != null) {
                            callBack.onDownloadStart(d);
                        }
                    }

                    @Override
                    public void onNext(@NonNull File file) {
                        if (callBack != null) {
                            callBack.onDownloadSuccess(file);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        if (callBack != null) {
                            callBack.onDownloadFailed(null);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * okHttp下载
     * @param url
     * @param destFileDir
     * @param destFileName
     * @param callBack
     */
    private void download(String url, String destFileDir, String destFileName, onDownloadCallBack callBack) {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                // 下载失败监听回调
                callBack.onDownloadFailed(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    //储存下载文件的目录
                    File dir = new File(destFileDir);
                    if (!dir.exists()) {
                        dir.mkdirs();
                    }
                    File file = new File(dir, destFileName);
                    is = Objects.requireNonNull(response.body()).byteStream();
                    long total = Objects.requireNonNull(response.body()).contentLength();
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        //下载中更新进度条
                        callBack.onDownloadLoading(progress);
                    }
                    fos.flush();
                    //下载完成
                    callBack.onDownloadSuccess(file);
                } catch (Exception e) {
                    callBack.onDownloadFailed(e);
                } finally {
                    try {
                        if (is != null) {
                            is.close();
                        }
                        if (fos != null) {
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public interface onDownloadCallBack {
        void onDownloadStart(Disposable disposable);

        void onDownloadSuccess(File file);

        void onDownloadLoading(int progress);

        void onDownloadFailed(Exception e);
    }


    /**
     * 创建通知渠道
     * @param notificationManager
     */
    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        // 通知渠道
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_ID, NotificationManager.IMPORTANCE_HIGH);
        //是否绕过请勿打扰模式
        channel.canBypassDnd();
        // 开启指示灯，如果设备有的话。
        channel.enableLights(true);
        // 开启震动
        channel.enableVibration(false);
        //是否会有灯光
        channel.shouldShowLights();
        //获取系统通知响铃声音的配置
        channel.getAudioAttributes();
        //  设置指示灯颜色
        channel.setLightColor(Color.RED);
        // 设置是否应在锁定屏幕上显示此频道的通知
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        // 设置是否显示角标
        channel.setShowBadge(true);
        //  设置绕过免打扰模式
        channel.setBypassDnd(true);
        // 设置震动频率
        //最后在notification中创建该通知渠道
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * 方法描述：createNotification方法
     *
     * @param
     * @return
     */

    public NotificationCompat.Builder getNotificationBuilder() {
        return new NotificationCompat.Builder(App.getAppContext(), CHANNEL_ID)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("视频下载") //设置通知标题
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) //设置通知的大图标
                //修改小圆圈默认背景颜色
                .setColor(getResources().getColor(R.color.colorAccent))
                .setDefaults(Notification.DEFAULT_LIGHTS) //设置通知的提醒方式： 呼吸灯
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) //设置通知的优先级：最大
                .setAutoCancel(false)//设置通知被点击一次是否自动取消
                .setContentText("下载进度" + "0%")
                .setProgress(100, 0, false)
                .setOnlyAlertOnce(true);
    }

}
