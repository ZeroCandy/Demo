package com.candy.rollviewpagerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.candy.rollviewpagerdemo.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mShowRVPLl;
    private TextView mTitleTv;
    private LinearLayout mShowDotsLl;

    private int[] mImgIds = {R.mipmap.pic_1, R.mipmap.pic_2, R.mipmap.pic_3};
    private List<String> mTitleList = new ArrayList<String>();
    private List<View> mDotViewList = new ArrayList<View>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    private void initData() {
        mTitleList.add("标题1");
        mTitleList.add("标题2");
        mTitleList.add("标题3");
    }

    private void initViews() {
        mShowRVPLl = ((LinearLayout) findViewById(R.id.rollpage_ll));
        mTitleTv = ((TextView) findViewById(R.id.title_tv));
        mShowDotsLl = ((LinearLayout) findViewById(R.id.dots_ll));
        initDots();
    }

    private void initDots() {
        mShowDotsLl.removeAllViews();
        mDotViewList.clear();
        for (int i = 0; i < mImgIds.length; i++) {
            View view = new View(this);
            view.setBackgroundResource(i == 0 ? R.mipmap.dot_focus : R.mipmap.dot_normal);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(CommonUtil.dip2px(this, 6), CommonUtil.dip2px(this, 6));
            view.setLayoutParams(layoutParams);
            layoutParams.setMargins(5, 0, 5, 0);
            mShowDotsLl.addView(view);
            mDotViewList.add(view);
        }
    }
}
