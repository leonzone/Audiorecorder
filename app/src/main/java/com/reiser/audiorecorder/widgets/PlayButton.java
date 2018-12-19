package com.reiser.audiorecorder.widgets;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * Created by reiserx on 2018/12/11.
 * desc : 播放按钮
 */
public class PlayButton extends AppCompatButton {
    private static final int STATE_PLAY = 1;
    private static final int STATE_STOP = 2;

    private int mState = STATE_STOP;

    public PlayButton(Context context) {
        super(context);
        init(context);
    }


    public PlayButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
//        setBackgroundDrawable(context.getDrawable(R.drawable.record_progress));
    }

    /**
     * 切换按钮状态
     */
    public void toggle() {
        if (mState == STATE_PLAY) {
            changeStateToStop();
        } else if (mState == STATE_STOP) {
            changeStateToPlay();
        }

    }

    /**
     * 播放状态、显示停止样式
     */
    private void changeStateToPlay() {
        mState = STATE_PLAY;
    }

    /**
     * 停止状态、显示播放样式
     */
    private void changeStateToStop() {
        mState = STATE_STOP;
    }

}
