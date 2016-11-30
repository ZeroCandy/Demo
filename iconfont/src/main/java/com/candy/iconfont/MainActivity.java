package com.candy.iconfont;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mBubbleTv, mBulbTv, mDogTv, mLabTv, mLikeTv, mLocationTv, mDynamicTv, mDynamicColorTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {
        setContentView(R.layout.activity_main);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "iconfont.ttf");

        /* 静态使用IconFont */
        mBubbleTv = ((TextView) findViewById(R.id.bubble_tv));
        mBubbleTv.setTypeface(typeface);

        mBulbTv = ((TextView) findViewById(R.id.bulb_tv));
        mBulbTv.setTypeface(typeface);

        mDogTv = ((TextView) findViewById(R.id.dog_tv));
        mDogTv.setTypeface(typeface);

        mLabTv = ((TextView) findViewById(R.id.lab_tv));
        mLabTv.setTypeface(typeface);

        mLikeTv = ((TextView) findViewById(R.id.like_tv));
        mLikeTv.setTypeface(typeface);

        mLocationTv = ((TextView) findViewById(R.id.location_tv));
        mLocationTv.setTypeface(typeface);

        /* 动态使用IconFont */
        mDynamicTv = ((TextView) findViewById(R.id.dynamic_tv));
        mDynamicTv.setTypeface(typeface);
        mDynamicTv.setText("\ue8d4 动态设置的IconFont");// &#xe8d4; -> "\ue8d4"

        mDynamicColorTv = ((TextView) findViewById(R.id.dynamic_color_tv));
        mDynamicColorTv.setTypeface(typeface);
        mDynamicColorTv.setText(Html.fromHtml("<font color=\"red\">&#xe8d4;</font><font color=\"gray\">设置IconFont图标颜色</font>"));
    }
}
