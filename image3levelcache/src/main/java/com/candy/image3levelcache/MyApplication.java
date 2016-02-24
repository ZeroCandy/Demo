package com.candy.image3levelcache;

import android.app.Application;

/**
 * Created by 帅阳 on 2016/2/24.
 */
public class MyApplication extends Application{
    /**全局单例上下文对象*/
    private static MyApplication mApplication;
    /**Application的名称*/
    public static String TAG;

    /**
     * 获取全局上下文对象
     * @return
     */
    public static MyApplication getInstance(){
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication=this;
        TAG=this.getClass().getSimpleName();
    }
}
