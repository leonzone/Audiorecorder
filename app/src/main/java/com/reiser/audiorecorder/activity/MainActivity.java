package com.reiser.audiorecorder.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;

import com.reiser.audiorecorder.R;
import com.reiser.audiorecorder.helper.AudioRecorderHelper;

/**
 * Created by reiserx on 2018/12/10.
 * desc : 主界面
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private boolean permissionToRecordAccepted = false;

    private FloatingActionButton mFBtnStart;
    private Chronometer mChmTimer = null;
    private AudioRecorderHelper mAudioRecorderHelper;
    private boolean mIsRecording = false;
    private AnimatorSet mAnimatorSet;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if (permissionToRecordAccepted) {
                    startRecording();
                }
                break;
        }
        if (!permissionToRecordAccepted) finish();

    }


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_main);
        initView();
        mAudioRecorderHelper = new AudioRecorderHelper(getApplicationContext());
    }

    @Override
    protected void onDestroy() {
        stopRecording();
        cancelAnimator();
        super.onDestroy();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mFBtnStart = findViewById(R.id.fbtn_play);
        mFBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsRecording) {
                    stopRecording();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_RECORD_AUDIO_PERMISSION);
                }
            }
        });

        mChmTimer = findViewById(R.id.chn_timer);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                startActivity(new Intent(this, RecordListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * 开始录制
     */
    private void startRecording() {
        mChmTimer.setBase(SystemClock.elapsedRealtime());
        mChmTimer.start();
        if (mAudioRecorderHelper != null) {
            mAudioRecorderHelper.startRecording();
            mIsRecording = true;
            mFBtnStart.setImageResource(R.drawable.ic_stop);
            updateMicStatus();
        }

    }

    /**
     * 停止录制
     */
    private void stopRecording() {
        mChmTimer.stop();
        mChmTimer.setBase(SystemClock.elapsedRealtime());
        if (mAudioRecorderHelper != null) {
            String fileName = mAudioRecorderHelper.stopRecording();
            mIsRecording = false;
            mFBtnStart.setImageResource(R.drawable.ic_play);
            Snackbar.make(mFBtnStart, getString(R.string.saved_file, fileName), Snackbar.LENGTH_SHORT)
                    .setAction(R.string.checkin, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this, RecordListActivity.class));
                        }
                    })
                    .show();
            cancelAnimator();
        }
    }

    /**
     * 结束动画
     */
    private void cancelAnimator() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
            mAnimatorSet.removeAllListeners();
            mAnimatorSet = null;

        }
    }


    /**
     * 更新话筒状态
     */
    private void updateMicStatus() {
        float times = (float) mAudioRecorderHelper.getdB();
        if (times < 45) {
            times = 1;
        } else {
            times = times / 45;
        }

        mAnimatorSet = new AnimatorSet();

        ValueAnimator scaleYAnim = ObjectAnimator.ofFloat(mChmTimer, "scaleY", mChmTimer.getScaleY(), times);
        ValueAnimator scaleXAnim = ObjectAnimator.ofFloat(mChmTimer, "scaleX", mChmTimer.getScaleX(), times);
        mAnimatorSet.play(scaleYAnim).with(scaleXAnim);
        mAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateMicStatus();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mChmTimer.animate().scaleX(1).start();
                mChmTimer.animate().scaleY(1).start();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimatorSet.setDuration(500);
        mAnimatorSet.start();
    }

}
