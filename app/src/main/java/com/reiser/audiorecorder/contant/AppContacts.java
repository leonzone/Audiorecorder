package com.reiser.audiorecorder.contant;

import android.os.Environment;

import java.io.File;

/**
 * Created by reiserx on 2018/12/11.
 * desc :
 */
public class AppContacts {

    /**
     * 录音文件夹
     */
    public static final String RECORD_SAVE_DIR_PATH =Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
}
