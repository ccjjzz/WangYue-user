package com.jiuyue.user.recevier;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.jiuyue.user.App;
import com.jiuyue.user.utils.AppUtils;

import java.io.File;

public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String url = intent.getStringExtra("url");
        File file = null;
        if (url != null) {
            file = new File(url);
        }
        if(null==file || !file.exists()){
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {//7.0 Android N
            //com.xxx.xxx.fileprovider为上述manifest中provider所配置相同
            String applicationId = AppUtils.getPackageName(App.getAppContext());
            uri = FileProvider.getUriForFile(context, applicationId+ ".provider.fileProvider", file);
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//7.0以后，系统要求授予临时uri读取权限，安装完毕以后，系统会自动收回权限，该过程没有用户交互
        } else {//7.0以下
            uri = Uri.fromFile(file);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        i.setDataAndType(uri, "video/*");
        context.startActivity(i);
    }
}
