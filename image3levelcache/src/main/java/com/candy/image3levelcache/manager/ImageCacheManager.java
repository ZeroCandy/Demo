package com.candy.image3levelcache.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.candy.image3levelcache.impl.ImageCacheImpl;

/**
 * Created by 帅阳 on 2016/2/24.
 */
public class ImageCacheManager {
    private static String TAG= ImageCacheManager.class.getSimpleName();

    private static ImageLoader.ImageCache mImageCache=new ImageCacheImpl();
    public static ImageLoader mImageLoader=new ImageLoader(RequestQueueManager.mRequestQueue,mImageCache);

    /**
     * 加载资源图片
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap getBitmapFromRes(Context context,int resId){
        return BitmapFactory.decodeResource(context.getResources(),resId);
    }

    /**
     * 加载网络图片
     * @param url
     * @param imageView
     * @param defaultImage
     * @param errorImage
     */
    public static void loadImage(String url,ImageView imageView,Bitmap defaultImage,Bitmap errorImage){
        loadImage(url,imageView,defaultImage,errorImage,0,0);
    }

    /**
     * 加载网络图片
     * @param url
     * @param imageView
     * @param defaultImage
     * @param errorImage
     * @param maxWidth
     * @param maxHeight
     */
    public static void loadImage(String url,ImageView imageView,Bitmap defaultImage,Bitmap errorImage,int maxWidth,int maxHeight){
        mImageLoader.get(
                url,
                ImageCacheManager.getImageListener(imageView,defaultImage,errorImage),
                maxWidth,
                maxHeight
        );
    }

    /**
     * 获取图片加载监听器
     * @param imageView 用于显示图片的控件
     * @param defaultImage 默认的图片
     * @param errorImage 加载失败的图片
     * @return
     */
    private static ImageLoader.ImageListener getImageListener(final ImageView imageView, final Bitmap defaultImage, final Bitmap errorImage){
        return new ImageLoader.ImageListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                //设置加载失败时显示的图片
                if(errorImage!=null){
                    imageView.setImageBitmap(errorImage);
                }
            }

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if(response.getBitmap()!=null){
                    imageView.setImageBitmap(response.getBitmap());
                }else if(defaultImage!=null){
                    imageView.setImageBitmap(defaultImage);
                }
            }
        };
    }
}
