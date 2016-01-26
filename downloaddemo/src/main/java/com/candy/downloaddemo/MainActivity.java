package com.candy.downloaddemo;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="MainActivity";
    private static final String URL="http://gaobeicrowdfunding.com/projectApk/quanchou.apk";

    private long mRequestID;
    private DownloadManager mDownloadManager;
    private DownloadManager.Request mRequest;
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDownloadManager();
        initBroadcastReceiver();
    }

    private void initDownloadManager() {
        //获取DownloadManager系统服务对象
        mDownloadManager = ((DownloadManager) getSystemService(DOWNLOAD_SERVICE));
        //获取下载请求对象
        mRequest = new DownloadManager.Request(Uri.parse(URL));
        //设置下载路径和下载文件名
        //mRequest.setDestinationInExternalPublicDir("Gaobei", "quanchouv1.0.apk");
        mRequest.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS,"quanchou_v1.0.apk");
        //设置只允许在WiFi下进行下载
        mRequest.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //设置Notifications的标题和描述
        mRequest.setTitle("圈筹");
        mRequest.setDescription("P2P平台App");
        //不允许使用计费网络(>=API16)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mRequest.setAllowedOverMetered(false);
        }
        //Request.VISIBILITY_HIDDEN:不显示该下载请求的Notification。如果要使用这个参数，需要在应用的清单文件中加上DOWNLOAD_WITHOUT_NOTIFICATION权限
        //Request.VISIBILITY_VISIBLE:在下载进行的过程中，通知栏中会一直显示该下载的Notification，当下载完成时，该Notification会被移除，这是默认的参数
        //Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED:在下载过程中通知栏会一直显示该下载的Notification，在下载完成后该Notification会继续显示，直到用户点击该Notification或者消除该Notification
        //Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION:只有在下载完成后该Notification才会被显示
        //(>=API11)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
    }

    private void initBroadcastReceiver() {
        //设置广播过滤意图
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        //注册广播接收者
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //从广播中获取下载请求ID
                long requestID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if(requestID==mRequestID){
                    Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
                }
            }
        };
        registerReceiver(mBroadcastReceiver,intentFilter);
    }

    /**
     * 开始下载
     * @param view
     */
    public void startDownload(View view){
        //进行下载，并获取系统为当前的下载请求分配的一个唯一ID
        //利用该ID可以重新获得这个下载任务并进行查询下载状态、取消下载等操作
        mRequestID = mDownloadManager.enqueue(mRequest);
    }

    /**
     * 自爆
     * @param view
     */
    public void exitApp(View view){
        android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
    }
}
