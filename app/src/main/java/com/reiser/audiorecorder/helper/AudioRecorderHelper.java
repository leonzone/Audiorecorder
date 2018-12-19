package com.reiser.audiorecorder.helper;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import com.reiser.audiorecorder.R;
import com.reiser.audiorecorder.contant.AppContacts;

import java.io.File;
import java.io.IOException;

/**
 * Created by reiserx on 2018/12/10.
 * desc :声音录制 helper
 */
public class AudioRecorderHelper {

    private MediaRecorder mRecorder = null;
    private long mStartMillis = 0;
    private String mFilePath = "";
    private String mFileName = "";
    private DBHelper dbHelper;
    private Context mContext;
    private static String TAG = "AudioRecorderHelper";

    public AudioRecorderHelper(Context context) {
        mContext = context;
        dbHelper = new DBHelper(context);
    }

    /**
     * 开始录制
     */
    public void startRecording() {
        int count = dbHelper.getCount() + 1;
        File f;
        String dirPath = AppContacts.RECORD_SAVE_DIR_PATH;
        do {
            count++;
            mFileName = mContext.getString(R.string.file_name) + count + ".3gp";
            mFilePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFilePath = dirPath + mFileName;
            f = new File(mFilePath);
        } while (f.exists() && !f.isDirectory());
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFilePath);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
            mRecorder.start();
            mStartMillis = System.currentTimeMillis();
            Log.d(TAG, "startRecording: " + mFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 停止录制
     *
     * @return 文件名
     */
    public String stopRecording() {
        mRecorder.stop();
        long enddMillis = (System.currentTimeMillis() - mStartMillis);
        mRecorder.release();
        Log.d(TAG, "stopRecording: " + mFilePath);
        mRecorder = null;
        try {
            dbHelper.addRecord(mFileName, mFilePath, enddMillis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mFileName;
    }

    /**
     * @return 获取分贝 0 dB 到90.3 dB
     */
    public double getdB() {
        double db = 0f;
        if (mRecorder != null) {
            double ratio = (double) mRecorder.getMaxAmplitude();
            db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
            }

        }
        return db;
    }
}
