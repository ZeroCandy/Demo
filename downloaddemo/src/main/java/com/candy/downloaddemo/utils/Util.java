package com.candy.downloaddemo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ZeroCandy on 2016/2/18.
 */
public class Util {
    /**
     * 启动安装界面
     * @param context
     * @param uri
     */
    public static void startInstallActivity(Context context, Uri uri) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
