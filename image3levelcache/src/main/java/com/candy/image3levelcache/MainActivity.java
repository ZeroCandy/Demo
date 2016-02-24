package com.candy.image3levelcache;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.candy.image3levelcache.manager.ImageCacheManager;

/**
 * 图片三级缓存Demo
 * <p/>
 * Volley+LruCache+DiskLruCache
 * <p/>
 */
public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = ((ImageView) findViewById(R.id.iv));
    }

    /**
     * 加载图片
     * @param view
     */
    public void loadImage(View view){
        String address="https://www.baidu.com/img/baidu_jgylogo3.gif";
        ImageCacheManager.loadImage(
                address,
                mImageView,
                ImageCacheManager.getBitmapFromRes(MyApplication.getInstance(),R.mipmap.pic_default),
                ImageCacheManager.getBitmapFromRes(MyApplication.getInstance(),R.mipmap.pic_default)
        );
    }
}
