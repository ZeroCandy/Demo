package com.candy.lockscreen;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.candy.lockscreen.receiver.MyDeviceAdminReceiver;

public class MainActivity extends AppCompatActivity {
    private DevicePolicyManager mDPM;
    private ComponentName mDARSample;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDPM = ((DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE));
        //将当前Activity和设备广播接收者进行绑定【ComponentName(组件名称)是用来打开其他应用程序中的Activity或服务的】
        mDARSample =new ComponentName(this,MyDeviceAdminReceiver.class);
    }

    /**
     * 激活管理员权限
     * @param v
     */
    public void openAdminAuthority(View v) {
        //1.设置添加设备管理权限意图
        Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDARSample);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"这是说明内容");
        //2.打开“激活设备管理器”页面
        startActivity(intent);
    }

    /**
     * 卸载当前应用
     * @param v
     */
    public void uninstallCurrentApp(View v) {
        //1.移除“设备管理员”权限
        //判断是否获取设备管理权限
        if(mDPM.isAdminActive(mDARSample)){
            mDPM.removeActiveAdmin(mDARSample);
        }
        //2.卸载当前应用
        Intent intent=new Intent();
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:"+getPackageName()));
        startActivity(intent);
    }

    /**
     * 锁屏
     * @param v
     */
    public void lockScreen(View v) {
        //判断是否获取设备管理权限
        if(mDPM.isAdminActive(mDARSample)){
            mDPM.lockNow();
        }
    }

}
