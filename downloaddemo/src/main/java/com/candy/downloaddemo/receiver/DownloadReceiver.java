package com.candy.downloaddemo.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by ZeroCandy on 2016/1/27.
 */
public class DownloadReceiver extends BroadcastReceiver{
    private static final String TAG="DownloadReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){//判断是否为下载完成广播
            //获取下载任务ID
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            //获取下载查询对象并设置过滤下载ID
            DownloadManager.Query query=new DownloadManager.Query();
            query.setFilterById(id);
            //获取数据表指针并移至第一行数据
            Cursor cursor = downloadManager.query(query);
            if(cursor.moveToFirst()){
                //获取下载文件URI
                String uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                if(!TextUtils.isEmpty(uri)){
                    Log.i(TAG, uri);
                    Toast.makeText(context,"\""+uri.substring(uri.lastIndexOf("/")+1)+"\"下载完成",Toast.LENGTH_LONG).show();
                    startInstallActivity(context, uri);
                }
            }
        }else if(intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)){//判断是否为通知栏点击广播（Receiver过滤的有下载通知栏点击广播）
            //获取通知栏点击的下载任务ID数组
            long[] ids = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
            //移除（取消）下载
            downloadManager.remove(ids);
            Toast.makeText(context,"取消下载",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 启动安装界面
     * @param context
     * @param uri
     */
    private void startInstallActivity(Context context, String uri) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse(uri),"application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
