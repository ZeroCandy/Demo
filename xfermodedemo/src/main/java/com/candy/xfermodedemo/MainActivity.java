package com.candy.xfermodedemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = ((ImageView) findViewById(R.id.round_rect_iv));

        Bitmap roundRectBitmap = BitmapTransformUtil.getRoundRectBitmap(
                BitmapFactory.decodeResource(getResources(), R.mipmap.pic1));
        mImageView.setImageBitmap(roundRectBitmap);
    }

}
