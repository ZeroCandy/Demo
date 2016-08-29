package com.candy.runtimepermissionsdemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    public static final int REQUEST_CODE_READ_CONTACTS = 0;
    public static final String PERMISSION_READ_CONTACTS = Manifest.permission.READ_CONTACTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_CODE_READ_CONTACTS:
                if(grantResults.length > 0){
                    for(int i = 0; i < permissions.length; i++){
                        if(PERMISSION_READ_CONTACTS.equals(permissions[i])){
                            handleRequestReadContactsPermissionResult(grantResults[i]);
                            break;
                        }
                    }
                }
                break;
        }
    }

    /**
     * 请求读取通讯录权限
     * @param v
     */
    public void requestReadContactsPermission(View v){
        // 检查是否获取到读取通讯录的权限
        if(ContextCompat.checkSelfPermission(this, PERMISSION_READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){// 未获取
            // 判断是否需要给出权限使用的解释
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,PERMISSION_READ_CONTACTS)){
                Log.i(TAG,"需要给出解释");
                showRequestPermissionRationale();
            }else{
                Log.i(TAG,"不需要给出解释");
                // 请求权限
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{PERMISSION_READ_CONTACTS},
                        REQUEST_CODE_READ_CONTACTS
                );
            }
        }else{// 已获取
            Toast.makeText(this,"已获取读取通讯录权限",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 处理请求读取通讯录权限结果
     * @param grantResult
     */
    public void handleRequestReadContactsPermissionResult(int grantResult){
        Log.i(TAG,"grantResult:"+grantResult);
        if(grantResult == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"已获取读取通讯录权限",Toast.LENGTH_SHORT).show();
        }else{
            showAskSettingsDialog();
        }
    }

    /**
     * 弹出权限解释对话框
     */
    private void showRequestPermissionRationale() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("因为XXX原因所以要用权限，赶紧同意了！")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{PERMISSION_READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }

    /**
     * 弹出跳转Settings对话框
     */
    private void showAskSettingsDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("无法获取权限")
                .setMessage("请跳转到“设置”中允许App使用权限！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.fromParts("package",getPackageName(),null));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }
}
