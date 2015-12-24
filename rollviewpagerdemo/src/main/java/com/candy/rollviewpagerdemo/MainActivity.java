package com.candy.rollviewpagerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.candy.rollviewpagerdemo.utils.CommonUtil;
import com.candy.rollviewpagerdemo.widget.RollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mShowRVPLl;
    private TextView mTitleTv;
    private LinearLayout mShowDotsLl;

    private List<Integer> mResIdList=new ArrayList<Integer>();
    private List<String> mImgUrlList=new ArrayList<String>();
    private List<String> mTitleList = new ArrayList<String>();
    private List<View> mDotViewList = new ArrayList<View>();
    private RollViewPager mRollViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initViews();
    }

    private void initData() {
//        mTitleList.add("老人");
//        mTitleList.add("樱花节");
//        mTitleList.add("雄鹰");
//        mResIdList.add(R.mipmap.pic_1);
//        mResIdList.add(R.mipmap.pic_2);
//        mResIdList.add(R.mipmap.pic_3);

        mTitleList.add("标题1");
        mTitleList.add("标题2");
        mTitleList.add("标题3");
        mTitleList.add("标题4");
        mImgUrlList.add("http://image.zcool.com.cn/56/35/1303967876491.jpg");
        mImgUrlList.add("http://image.zcool.com.cn/59/54/m_1303967870670.jpg");
        mImgUrlList.add("http://image.zcool.com.cn/47/19/1280115949992.jpg");
        mImgUrlList.add("http://image.zcool.com.cn/59/11/m_1303967844788.jpg");
    }

    private void initViews() {
        mShowRVPLl = ((LinearLayout) findViewById(R.id.rollviewpager_ll));
        mTitleTv = ((TextView) findViewById(R.id.title_tv));
        mShowDotsLl = ((LinearLayout) findViewById(R.id.dots_ll));

        initDots();

        mRollViewPager = new RollViewPager(this, mDotViewList, new RollViewPager.OnPageClickListener() {
            @Override
            public void onPageClick(int index) {
                Toast.makeText(MainActivity.this,"position="+index,Toast.LENGTH_SHORT).show();
            }
        });
        mRollViewPager.initTitleList(mTitleTv,mTitleList);
//        mRollViewPager.initImgIds(mResIdList);
        mRollViewPager.initImgUrlList(mImgUrlList);
        mRollViewPager.startRoll();

        mShowRVPLl.removeAllViews();
        mShowRVPLl.addView(mRollViewPager);
    }

    private void initDots() {
        mShowDotsLl.removeAllViews();
        mDotViewList.clear();
//        for (int i = 0; i < mResIdList.size(); i++) {
        for (int i = 0; i < mImgUrlList.size(); i++) {
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
