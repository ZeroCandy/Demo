package com.candy.rollviewpagerdemo.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.candy.rollviewpagerdemo.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * 轮播ViewPager
 * Created by 帅阳 on 2015/12/9.
 */
public class RollViewPager extends ViewPager {
    private static final String TAG="RollViewPager";

    private Context mContext;
    private ImageLoader mImageLoader;
    /**按下时的X坐标*/
    private int mDownY;
    /**按下时的Y坐标*/
    private int mDownX;

    /**标记是否有标题*/
    private boolean mWithTitle = false;
    /**标记是否为图片资源集合*/
    private boolean mIsImgResIds =false;
    /**当前显示Pager*/
    private int mCurrentIndex = 0;

    /**标题集合*/
    private List<String> mTitleList;
    /**图片资源集合*/
    private List mImgList;
    /**指示点集合*/
    private List<View> mDotViewList;
    private OnPageClickListener mOnPageClickListener;
    private RollTask mRollTask;
    private MyAdapter mAdapter;

    private TextView mPagerTitleTv;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //设置当前显示Page
            RollViewPager.this.setCurrentItem(mCurrentIndex);
            startRoll();
        }
    };

    public RollViewPager(Context context, List<View> viewList, OnPageClickListener listener) {
        super(context);
        this.mContext = context;
        this.mDotViewList = viewList;
        this.mOnPageClickListener = listener;

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
        mRollTask = new RollTask();

        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {//Page选择中后的操作
                mCurrentIndex=position;
                //带标题的情况下，切换到当前对应的标题
                if (mWithTitle) {
                    mPagerTitleTv.setText(mTitleList.get(position));
                }
                //修改对应的指示点背景图片
                for(int i=0;i<mImgList.size();i++){
                    mDotViewList.get(i).setBackgroundResource(i==position?R.mipmap.dot_focus:R.mipmap.dot_normal);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    /**
     * 初始化标题栏
     *
     * @param pagerTitleTv 显示标题
     * @param titleList    标题字符串集合
     */
    public void initTitleList(TextView pagerTitleTv, List<String> titleList) {
        if (null != pagerTitleTv && null != titleList && titleList.size() >= 0) {
            mWithTitle = true;
            this.mPagerTitleTv = pagerTitleTv;
            this.mTitleList = titleList;
            pagerTitleTv.setText(titleList.get(0));
        }
    }

    /**
     * 初始化图片URL地址
     *
     * @param urlImgList 图片URL地址集合
     */
    public void initImgUrlList(List<String> urlImgList) {
        this.mImgList = urlImgList;
    }

    /**
     * 初始化图片资源IDs
     * @param resIdList
     */
    public void initImgIds(List<Integer> resIdList){
        this.mIsImgResIds =true;
        this.mImgList= resIdList;
    }

    /**
     * 从界面移除时回调该方法
     * <p/>
     * 移除所有任务
     * </p>
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 分发触摸事件
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN://按下
                //要求父控件不拦截触摸事件
                getParent().requestDisallowInterceptTouchEvent(true);
                //记录按下的X坐标和Y坐标
                mDownX= (int) ev.getX();
                mDownY= (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE://移动
                //记录移动时的X坐标和Y坐标
                int moveX= (int) ev.getX();
                int moveY= (int) ev.getY();
                //若Y轴移动距离大于X轴移动距离，视为下拉
                if(Math.abs(moveY-mDownY)>Math.abs(moveX-mDownX)){
                    //让父控件处理触摸事件
                    getParent().requestDisallowInterceptTouchEvent(false);
                }else{
                    //让父控件不处理触摸事件，并默认把事件传递给RollViewPager自己的子控件（这里是条目）
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * Page点击事件监听器
     */
    public interface OnPageClickListener {
        void onPageClick(int index);
    }

    /**
     * 开始滚动
     */
    public void startRoll(){
        //设置Adapter
        if(mAdapter==null){
            mAdapter=new MyAdapter();
            setAdapter(mAdapter);
        }else{
            mAdapter.notifyDataSetChanged();
        }
        //延迟执行轮播任务
        mHandler.postDelayed(mRollTask, 3000);
    }

    /**
     * 轮播任务
     */
    private class RollTask implements Runnable {
        @Override
        public void run() {
            //设置当前Page索引
            mCurrentIndex = (mCurrentIndex + 1) % mImgList.size();
            //发送消息
            mHandler.obtainMessage().sendToTarget();
        }
    }

    private class MyAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mImgList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            //初始化条目布局并加载显示图片
            View view=View.inflate(mContext, R.layout.item_viewpager,null);
            ImageView imageView=((ImageView) view.findViewById(R.id.item_iv));
            if(mIsImgResIds){
                imageView.setImageResource((Integer) mImgList.get(position));
            }else{
                Log.i(TAG,(String)mImgList.get(position));
                mImageLoader.displayImage((String)mImgList.get(position), imageView);
            }
            //设置Touch监听器判断是否为点击事件
            view.setOnTouchListener(new OnTouchListener() {
                private int downX;
                private long downTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()){
                        case MotionEvent.ACTION_DOWN://手指按下
                            //清除轮播任务
                            mHandler.removeCallbacksAndMessages(null);
                            //记录按下时的X坐标和时间
                            downX= (int) event.getX();
                            downTime=System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP://手指抬起
                            //当触摸事件在500毫秒以内且按下和抬起的X坐标相同时，视为发生点击事件
                            if(System.currentTimeMillis()-downTime<500 && downX==event.getX()){
                                if(mOnPageClickListener!=null){
                                    mOnPageClickListener.onPageClick(position);
                                }
                            }
                            startRoll();
                            break;
                        case MotionEvent.ACTION_CANCEL://动作取消
                            startRoll();
                            break;
                    }
                    //返回true表示消耗了该点击事件
                    return true;
                }
            });
            ((RollViewPager) container).addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((RollViewPager) container).removeView(((View) object));
        }
    }
}
