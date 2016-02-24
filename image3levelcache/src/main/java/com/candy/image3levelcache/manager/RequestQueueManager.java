package com.candy.image3levelcache.manager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.candy.image3levelcache.MyApplication;

/**
 * Created by 帅阳 on 2016/2/24.
 */
public class RequestQueueManager {
    /**
     * 获取Volley请求队列
     */
    public static RequestQueue mRequestQueue= Volley.newRequestQueue(MyApplication.getInstance());

    /**
     * 向请求队列中添加指定TAG标签的请求
     * @param request
     * @param tag
     */
    public static void addRequest(Request<?> request,Object tag){
        if(null!=tag){
            request.setTag(tag);
        }
        mRequestQueue.add(request);
    }

    /**
     * 取消在请求队列中指定TAG标签的请求
     * @param tag
     */
    public static void cancelRequest(Object tag){
        mRequestQueue.cancelAll(tag);
    }
}
