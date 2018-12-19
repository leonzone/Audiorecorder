package com.reiser.audiorecorder.listeners;

import android.media.MediaPlayer;


/**
 * Created by reiserx on 2018/12/10.
 * desc : 媒体播放状态监听
 */
public abstract class MediaPlayerListener implements
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnPreparedListener {

    /**
     * 注册监听器
     *
     * @param mediaPlayer mediaPlayer
     */
    public void register(MediaPlayer mediaPlayer) {
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
    }

}
