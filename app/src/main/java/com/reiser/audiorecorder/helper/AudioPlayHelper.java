package com.reiser.audiorecorder.helper;

import android.media.MediaPlayer;
import android.os.Handler;
import android.text.TextUtils;

import com.reiser.audiorecorder.listeners.AudioPlayChangedListener;
import com.reiser.audiorecorder.listeners.MediaPlayerListener;

import java.io.IOException;

/**
 * Created by reiserx on 2018/12/10.
 * desc : 声音播放 helper
 */
public class AudioPlayHelper {
    private MediaPlayer mMediaPlayer = null;
    private String mPlayPath = "";

    private AudioPlayChangedListener mAudioPlayChangedListener;

    public void setAudioPlayChangedListener(AudioPlayChangedListener audioPlayChangedListener) {
        this.mAudioPlayChangedListener = audioPlayChangedListener;
    }

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null && mAudioPlayChangedListener != null) {
                mAudioPlayChangedListener.onChange(mMediaPlayer.getCurrentPosition(), mMediaPlayer.getDuration());
                updateProgress();
            }
        }
    };


    /**
     * 播放录音
     *
     * @param path 文件路径
     */
    public void play(String path) {
        if (!TextUtils.isEmpty(path) && !path.equals(mPlayPath)) {
            stopPlaying();
        }
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                pausePlaying();
            } else {
                resumePlaying();
            }
        } else {
            startPlaying(path);
            mPlayPath = path;
        }
    }


    /**
     * 开始播放
     *
     * @param path 文件路径
     */
    private void startPlaying(String path) {
        mMediaPlayer = new MediaPlayer();
        new AudioPlayerListener().register(mMediaPlayer);
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateProgress();

    }

    /**
     * 暂停播放
     */
    private void pausePlaying() {
        stopProgress();
        mMediaPlayer.pause();
    }

    /**
     * 继续播放
     */
    private void resumePlaying() {
        stopProgress();
        mMediaPlayer.start();
        updateProgress();
    }

    /**
     * 停止播放
     */
    public void stopPlaying() {
        stopProgress();
        if (mAudioPlayChangedListener != null) {
            mAudioPlayChangedListener.onStop();
        }
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    /**
     * 若播放地址正在播放，则停止播放
     *
     * @param path 播放地址
     */
    public void stopPlaying(String path) {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying() && !TextUtils.isEmpty(path) && path.equals(mPlayPath)) {
            stopPlaying();
        }
    }

    /**
     * 跳转到进度
     *
     * @param progress 进度
     */
    public void seekTo(int progress) {
        if (mMediaPlayer != null) {
            mMediaPlayer.seekTo(progress);
            stopProgress();
            updateProgress();
        }
    }


    /**
     * @return 播放器是否在播放
     */
    public boolean isPlaying() {
        if (mMediaPlayer != null) {
            return mMediaPlayer.isPlaying();
        }
        return false;
    }

    /**
     * 音频播放状态监听
     */
    private class AudioPlayerListener extends MediaPlayerListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            stopPlaying();

        }

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            stopPlaying();
            return false;
        }


        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
            if (mAudioPlayChangedListener != null) {
                mAudioPlayChangedListener.onStart(mp.getDuration());
            }
        }


    }


    /**
     * 延迟刷新UI,进度越短刷新越快
     */
    public void updateProgress() {
        if (mMediaPlayer != null) {
            mHandler.postDelayed(mRunnable, mMediaPlayer.getDuration() / 1000);
        }

    }


    /**
     * 移除 Runnable
     */
    public void stopProgress() {
        mHandler.removeCallbacks(mRunnable);
    }

}
