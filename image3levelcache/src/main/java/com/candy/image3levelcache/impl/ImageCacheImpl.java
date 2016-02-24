package com.candy.image3levelcache.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.candy.image3levelcache.MyApplication;
import com.candy.image3levelcache.utils.MD5Util;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by 帅阳 on 2016/2/24.
 */
public class ImageCacheImpl implements ImageLoader.ImageCache {
    private String TAG=ImageCacheImpl.this.getClass().getSimpleName();

    /**磁盘最大缓存大小*/
    private static final int DISK_MAX_SIZE=10*1024*1024;
    /**磁盘缓存目录*/
    private static final String DISK_CACHE_FOLDER_NAME="DiskLruCache";

    private static LruCache<String,Bitmap> mLruCache;
    private static DiskLruCache mDiskLruCache;

    public ImageCacheImpl() {
        /*
            LruCache以键值对的形式，初始化时，需要设置缓存的大小K，超过这个大小的数据将会被清除；
            清除的数据，是那些被先加入的数据；
            从而实现了缓存最近put的K个数据。
            注意：缓存不同的数据，需要重写sizeOf方法
         */

        //获取最大可用内存的1/8作为内存缓存大小
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        //获取LruCache对象并设置最大缓存
        mLruCache=new LruCache<String,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };

        /*
            DiskLruCache会在缓存目录中创建一个journal文件，文件内容如下：
            ======================
            libcor.io.DiskLruCache                       --->    标志使用DiskLruCache技术
            1                                            --->    DiskLruCahce版本号
            1                                            --->    应用程序版本号
            1                                            --->    valueCount，通常为1
                                                         --->    至此的前五行表示journal文件头
            DIRTY e21354b1244e0d1244b1274c078            --->    DIRTY关键字表示正准备写入一条缓存数据，每调用edit()方法的时候就会写入一条DIRTY记录。
                                                                 DIRTY关键字后面跟着的是缓存图片的key
                                                                 每一条DIRTY记录后面都应该有一行对于的CLEAN记录或者REMOVE记录，否则会被自动删除
            CLEAN e21354b1244e0d1244b1274c078 152313     --->    调用commit()方法表示写入缓存成功，这时就写入一条CLEAN记录
                                                                 key后面的152313表示缓存数据的大小，以字节为单位。
            READ e21354b1244e0d1244b1274c078
            REMOVE e21354b1244e0d1244b1274c078           --->    调用abort()方法表示写入缓存失败，这时就写入一条REMOVE记录
            ======================
         */

        //获取DiskLruCache对象
        try {
            mDiskLruCache=DiskLruCache.open(getDiskCacheDir(MyApplication.getInstance(),DISK_CACHE_FOLDER_NAME),
                    getAppVersion(MyApplication.getInstance()),
                    1,
                    DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 先从内存获取，再从磁盘获取转到内存
     * @param url
     * @return
     */
    @Override
    public Bitmap getBitmap(String url) {
        //判断内存中是否有缓存图片
        if(mLruCache.get(url)!=null) {//有缓存，则直接从内存中获取
            Log.i(TAG, "get from LruCahce");
            return mLruCache.get(url);
        }else{//否则到磁盘中获取
            String key= MD5Util.getMD5(url);
            try {
                if(mDiskLruCache.get(key)!=null){
                    //从快照中获取Bitmap并写入内存中
                    DiskLruCache.Snapshot snapshot=mDiskLruCache.get(key);
                    Bitmap bitmap=null;
                    if(snapshot!=null){
                        bitmap= BitmapFactory.decodeStream(snapshot.getInputStream(0));
                        mLruCache.put(url,bitmap);
                        Log.i(TAG,"get from DiskLruCache");
                    }
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 写到内存和磁盘中
     * @param url
     * @param bitmap
     */
    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        /*将Bitmap使用软引用存在内存中*/
        mLruCache.put(url,bitmap);

        /*将Bitmap以JPEG的格式缓存到磁盘中*/
        //根据url获取MD5 Key
        String key = MD5Util.getMD5(url);
        try {
            //判断是否缓存过该记录，没有缓存过则进行缓存
            if(mDiskLruCache.get(key)==null){
                //准备写入一条缓存数据
                DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                if(editor!=null){
                    OutputStream outputStream = editor.newOutputStream(0);
                    //将Bitmap以JPEG格式压缩写入输出流中
                    if(bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)){
                        //写入缓存成功
                        editor.commit();
                    }else{
                        //写入缓存失败
                        editor.abort();
                    }
                }
                mDiskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取磁盘缓存文件夹
     * @param context
     * @param folderName
     * @return
     */
    public File getDiskCacheDir(Context context,String folderName){
        String cachePath;
        //判断外部存储是否可以，可以则获取外部存储缓存路径
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()){
            cachePath=context.getExternalCacheDir().getPath();
        }else{
            cachePath=context.getCacheDir().getPath();
        }
        Log.i(TAG,"磁盘缓存路径："+cachePath);
        return new File(cachePath+File.separator+folderName);
    }

    /**
     * 获取App版本号
     * @param context
     * @return
     */
    public int getAppVersion(Context context){
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
