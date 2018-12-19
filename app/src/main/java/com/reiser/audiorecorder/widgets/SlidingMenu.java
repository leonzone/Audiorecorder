package com.reiser.audiorecorder.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.reiser.audiorecorder.listeners.SlidingMenuChangeListener;
import com.reiser.audiorecorder.util.Utils;

/**
 * Created by reiserx on 2018/12/10.
 * desc : 侧滑菜单栏
 */
public class SlidingMenu extends HorizontalScrollView {

    private static final float radio = 0.3f;
    private int mScreenWidth;
    private int mMenuWidth;
    private boolean once = true;
    private SlidingMenuChangeListener mSlidingMenuTouchListener;

    public void setSlidingMenuTouchListener(SlidingMenuChangeListener mSlidingMenuTouchListener) {
        this.mSlidingMenuTouchListener = mSlidingMenuTouchListener;
    }

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = Utils.getScreenWidth();
        mMenuWidth = (int) (mScreenWidth * radio);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (once) {
            LinearLayout wrapper = (LinearLayout) getChildAt(0);
            wrapper.getChildAt(0).getLayoutParams().width = mScreenWidth;
            wrapper.getChildAt(1).getLayoutParams().width = mMenuWidth;
            once = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 关闭菜单
     */
    public void close() {
        this.smoothScrollTo(0, 0);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (mSlidingMenuTouchListener != null) {
                    mSlidingMenuTouchListener.onTouch();
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (Math.abs(scrollX) > mMenuWidth / 2) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    if (mSlidingMenuTouchListener != null) {
                        mSlidingMenuTouchListener.toggle(true);
                    }
                } else {
                    this.smoothScrollTo(0, 0);
                    if (mSlidingMenuTouchListener != null) {
                        mSlidingMenuTouchListener.toggle(false);
                    }
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }


}
