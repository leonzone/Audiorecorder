package com.reiser.audiorecorder.listeners;

/**
 * Created by reiserx on 2018/12/10.
 * desc : 音频播放监听器
 */
public interface AudioPlayChangedListener {
    void onStart(int totalTime);

    void onChange(int currentTime, int totalTime);

    void onStop();
}
