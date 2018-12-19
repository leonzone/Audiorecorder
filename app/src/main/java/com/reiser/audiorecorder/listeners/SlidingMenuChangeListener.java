package com.reiser.audiorecorder.listeners;

/**
 * Created by reiserx on 2018/12/10.
 * desc : 策划菜单监听器
 */
public interface SlidingMenuChangeListener {
    void onTouch();

    void toggle(boolean open);
}
