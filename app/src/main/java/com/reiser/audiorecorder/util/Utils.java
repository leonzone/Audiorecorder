package com.reiser.audiorecorder.util;

import android.content.res.Resources;

/**
 * Created by reiserx on 2018/12/11.
 * desc : 工具类
 */
public class Utils {


    /**
     * 获取当前屏幕宽度
     *
     * @return 当前屏幕宽度
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}
