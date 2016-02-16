package com.candy.ivlayoutparams;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        ImageView iv = (ImageView) LayoutInflater.from(this).inflate(R.layout.layout_image, null, false);
        LinearLayout imageContainerLl = (LinearLayout) findViewById(R.id.image_container_ll);

        /************************Error Code************************/
        //R.layout.layout_image布局中设置的width、height在以下代码中实际是没有效果的
        //因为布局实例化时width、height参数丢失，addView方法中child.getLayoutParams()获取的值为null，故会使用默认的布局参数（见addView方法源码）
        //iv.setImageResource(R.mipmap.img1);
        //imageContainerLl.addView(iv);
        /************************Error Code************************/

        /************************Correction************************/
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        iv.setLayoutParams(layoutParams);
        iv.setImageResource(R.mipmap.img1);
        imageContainerLl.addView(iv);
        /************************Correction************************/
    }
}
