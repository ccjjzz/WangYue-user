package com.jiuyue.user.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.util.List;

/**
 * 系统自带分享工具类
 */
public class SystemShareUtil {

    /**
     * 分享文本
     */
    public static void shareText(Context context, String text, String title) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 分享单张图片
     */
    public static void shareImage(Context context, Bitmap bitmap, String title) {
        Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                context.getContentResolver(), bitmap, null, null));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, uriToImage);
        context.startActivity(Intent.createChooser(intent, title));
    }

    /**
     * 指定分享到QQ
     */
    public static void shareTextToQQ(Context context, String text, String title) {
        if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_MOBILE_QQ)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setPackage(PlatformUtil.PACKAGE_MOBILE_QQ);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(intent, title));
        } else {
            Toast.makeText(context, "您需要安装QQ客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 分享图片给QQ好友
     */
    public static void shareImageToQQ(Context context, Bitmap bitmap, String title) {
        if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_MOBILE_QQ)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        context.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");
                // 遍历所有支持发送图片的应用。找到需要的应用
                ComponentName componentName = new ComponentName(PlatformUtil.PACKAGE_MOBILE_QQ, "com.tencent.mobileqq.activity.JumpActivity");
                shareIntent.setComponent(componentName);
                context.startActivity(Intent.createChooser(shareIntent, title));
            } catch (Exception e) {
                Toast.makeText(context, "分享图片失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "您需要安装QQ客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 分享到微信
     */
    public static void shareTextToWeChat(Context context, String text, String title) {
        if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_WECHAT)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setPackage(PlatformUtil.PACKAGE_WECHAT);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(intent, title));
        } else {
            Toast.makeText(context, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 分享图片到微信
     */
    public static void shareImageToWeChat(Context context, Bitmap bitmap, String content, String title) {
        if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_WECHAT)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        context.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");
                shareIntent.putExtra("Kdescription", content.isEmpty() ? "" : content);
                // 遍历所有支持发送图片的应用。找到需要的应用
                ComponentName componentName = new ComponentName(PlatformUtil.PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareImgUI");
                shareIntent.setComponent(componentName);
                context.startActivity(Intent.createChooser(shareIntent, title));
            } catch (Exception e) {
                Toast.makeText(context, "分享图片失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 分享到微信朋友圈
     */
    public static void shareTextToWeChatMoments(Context context, String text, String title) {
        if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_WECHAT)) {
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setComponent(new ComponentName(PlatformUtil.PACKAGE_WECHAT,
                    "com.tencent.mm.ui.tools.ShareToTimeLineUI"));
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(intent, title));
        } else {
            Toast.makeText(context, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 分享图片到微信
     */
    public static void shareImageToWeChatMoments(Context context, Bitmap bitmap, String content, String title) {
        if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_WECHAT)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        context.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");
                shareIntent.putExtra("Kdescription", content.isEmpty() ? "" : content);
                // 遍历所有支持发送图片的应用。找到需要的应用
                ComponentName componentName = new ComponentName(PlatformUtil.PACKAGE_WECHAT, "com.tencent.mm.ui.tools.ShareToTimeLineUI");
                shareIntent.setComponent(componentName);
                context.startActivity(Intent.createChooser(shareIntent, title));
            } catch (Exception e) {
                Toast.makeText(context, "分享图片失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "您需要安装微信客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 分享到微博
     */
    public static void shareTextToWeiBo(Context context, String text, String title) {
        if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_SINA)) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setPackage(PlatformUtil.PACKAGE_SINA);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            context.startActivity(Intent.createChooser(intent, title));
        } else {
            Toast.makeText(context, "您需要安装新浪微博客户端", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 分享图片到微博
     */
    public static void shareImageToWeiBo(Context context, Bitmap bitmap, String content, String title) {
        if (PlatformUtil.isInstallApp(context, PlatformUtil.PACKAGE_SINA)) {
            try {
                Uri uriToImage = Uri.parse(MediaStore.Images.Media.insertImage(
                        context.getContentResolver(), bitmap, null, null));
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shareIntent.setType("image/*");
                // 遍历所有支持发送图片的应用。找到需要的应用
                PackageManager packageManager = context.getPackageManager();
                List<ResolveInfo> matchs = packageManager.queryIntentActivities(shareIntent, PackageManager.MATCH_DEFAULT_ONLY);
                ResolveInfo resolveInfo = null;
                for (ResolveInfo each : matchs) {
                    String pkgName = each.activityInfo.applicationInfo.packageName;
                    if (PlatformUtil.PACKAGE_SINA.equals(pkgName)) {
                        resolveInfo = each;
                        break;
                    }
                }
                // type = "image/*"--->com.sina.weibo.composerinde.ComposerDispatchActivity
                // type = "text/plain"--->com.sina.weibo.weiyou.share.WeiyouShareDispatcher
                if (resolveInfo != null) {
                    shareIntent.setClassName(PlatformUtil.PACKAGE_SINA, resolveInfo.activityInfo.name);// 这里在使用resolveInfo的时候需要做判空处理防止crash
                } else {
                    ComponentName componentName = new ComponentName(PlatformUtil.PACKAGE_SINA, "com.sina.weibo.composerinde.ComposerDispatchActivity");
                    shareIntent.setComponent(componentName);
                }
                context.startActivity(Intent.createChooser(shareIntent, title));
            } catch (Exception e) {
                Toast.makeText(context, "分享图片失败", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, "您需要安装新浪微博客户端", Toast.LENGTH_LONG).show();
        }
    }
}

