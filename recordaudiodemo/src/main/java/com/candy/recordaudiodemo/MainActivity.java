package com.candy.recordaudiodemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_RECORD_AUDIO = 0;

    /**最大录音时长*/
    private static final int RECORD_MAX_LENGTH = 1000 * 60 * 3;
    /**间隔取样时间*/
    private static final int SPACE = 100;
    private static final int BASE = 1;
    private boolean mIsRecording = false;
    private String mFilePath;
    private String[] mPermissions;

    private MediaRecorder mMediaRecord;
    private Button mRecordBtn;

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMICStatusRunnable = new Runnable() {
        @Override
        public void run() {
            updateMICStatus();
        }
    };
    private ImageView mAudioIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initViews();
    }

    private void init() {
        mPermissions = new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};

        File externalCacheDir = new File(getExternalCacheDir().getPath()+"/record");
        if(!externalCacheDir.exists()){
            externalCacheDir.mkdirs();
        }
        mFilePath = externalCacheDir.getPath()+"/audio.mp3";
        Log.i(TAG,"录音存放路径："+mFilePath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG,">>>onRequestPermissionsResult<<<");
        switch (requestCode){
            case REQUEST_CODE_RECORD_AUDIO:
                if(grantResults.length > 0){
                    handleRequestPermissionResult(permissions,grantResults);
                }
                break;
            default:
                break;
        }
    }

    private void handleRequestPermissionResult(String[] permissions,int[] grantResults) {
        for(int i = 0;i < grantResults.length; i++){
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                showAskSettingDialog(permissions[i]);
                return ;
            }
        }
        startRecord();
    }

    private void initViews() {
        mRecordBtn = ((Button) findViewById(R.id.record_btn));
        mAudioIv = ((ImageView) findViewById(R.id.audio_iv));
    }

    public void record(View v) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !checkPermissions()){
            return ;
        }
        if(!mIsRecording){
            startRecord();
        }else{
            stopRecord();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean checkPermissions(){
        for(String permission:mPermissions){
            if(ActivityCompat.checkSelfPermission(this, permission)!= PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                    showRequestPermissionRationale(permission);
                }else{
                    requestPermissions();
                }
                return false;
            }
        }
        return true;
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions(
                this,
                mPermissions,
                REQUEST_CODE_RECORD_AUDIO
        );
    }

    private void showRequestPermissionRationale(String permission) {
        String title = null;
        String msg = null;
        switch (permission){
            case Manifest.permission.RECORD_AUDIO:
                title = "录音权限使用提示";
                msg = "App需要使用到录音权限才能进行录音。";
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                title = "存储空间权限使用提示";
                msg = "App需要使用到存储空间权限才能存储录音数据。";
                break;
        }
        createDialog(title,msg,false);
    }

    private void showAskSettingDialog(String permission) {
        String title = null;
        String msg = null;
        switch (permission){
            case Manifest.permission.RECORD_AUDIO:
                title = "录音权限使用提示";
                msg = "请跳转到“设置”中允许App使用录音权限，否则无法使用录音功能！";
                break;
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                title = "存储空间权限使用提示";
                msg = "请跳转到“设置”中允许App使用存储空间权限，否则无法使用录音功能！";
                break;
        }
        createDialog(title,msg,true);
    }

    private void createDialog(String title, String msg, final boolean isAskSettingDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(msg)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(isAskSettingDialog){
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                            startActivity(intent);
                        }else{
                            requestPermissions();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * 开始录音
     */
    private void startRecord(){
        if(mMediaRecord == null){
            mMediaRecord = new MediaRecorder();
            // 设置麦克风
            mMediaRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
            // 设置音频文件的编码：AAC/AMR_NB/AMR_MB/Default 声音的（波形）的采样
            mMediaRecord.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            // 设置输出文件的格式：THREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp格式，H263视频/ARM音频编码)、MPEG-4、RAW_AMR(只支持音频且音频编码要求为AMR_NB)
            mMediaRecord.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecord.setOutputFile(mFilePath);
            mMediaRecord.setMaxDuration(RECORD_MAX_LENGTH);
        }
        try {
            mMediaRecord.prepare();
            mMediaRecord.start();
            updateMICStatus();
            mIsRecording = true;
            mRecordBtn.setText("停止录音");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    private void stopRecord(){
        mAudioIv.setImageResource(R.drawable.icon_record_1);
        mIsRecording = false;
        mRecordBtn.setText("开始录音");
        if(mMediaRecord != null){
            mMediaRecord.stop();
            mMediaRecord.reset();
            mMediaRecord.release();
            mMediaRecord = null;
        }
    }

    /**
     * 更新麦克风状态
     */
    private void updateMICStatus(){
        if(mMediaRecord != null){
            // 比率
            double ratio = (double) mMediaRecord.getMaxAmplitude() / BASE;
            // 分贝
            double db = 0;
            if(ratio > 1){
                db = 20 * Math.log10(ratio);
            }
            Log.i(TAG,"分贝值："+db);
            if(db >= 82.5){
                mAudioIv.setImageResource(R.drawable.icon_record_12);
            }else if(db >= 75){
                mAudioIv.setImageResource(R.drawable.icon_record_11);
            }else if(db >= 67.5){
                mAudioIv.setImageResource(R.drawable.icon_record_10);
            }else if(db >= 60){
                mAudioIv.setImageResource(R.drawable.icon_record_9);
            }else if(db >= 52.5){
                mAudioIv.setImageResource(R.drawable.icon_record_8);
            }else if(db >= 45){
                mAudioIv.setImageResource(R.drawable.icon_record_7);
            }else if(db >= 37.5){
                mAudioIv.setImageResource(R.drawable.icon_record_6);
            }else if(db >= 30){
                mAudioIv.setImageResource(R.drawable.icon_record_5);
            }else if(db >= 22.5){
                mAudioIv.setImageResource(R.drawable.icon_record_4);
            }else if(db >= 15){
                mAudioIv.setImageResource(R.drawable.icon_record_3);
            }else if(db >= 7.5){
                mAudioIv.setImageResource(R.drawable.icon_record_2);
            }else{
                mAudioIv.setImageResource(R.drawable.icon_record_1);
            }
            mHandler.postDelayed(mUpdateMICStatusRunnable,SPACE);
        }
    }
}
