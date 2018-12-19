package com.reiser.audiorecorder.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.reiser.audiorecorder.bean.RecordEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reiserx on 2018/12/10.
 * desc : 数据库
 */
public class DBHelper extends SQLiteOpenHelper {
    private Context mContext;

    public static final String DATABASE_NAME = "saved_datas.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "recordings";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String PATH = "path";
    public static final String LENGTH = "length";
    public static final String TIME = "time";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY ," +
                    NAME + " TEXT," +
                    PATH + " TEXT," +
                    LENGTH + " INTEGER," +
                    TIME + " INTEGER" +
                    ")";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    public Context getContext() {
        return mContext;
    }


    /**
     * @return 获取全部录音
     */
    public List<RecordEntity> getRecords() {

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {ID, NAME, PATH, LENGTH, TIME};
        List<RecordEntity> result = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, projection, null, null, null, null, TIME + " DESC");
        while (cursor.moveToNext()) {
            RecordEntity item = new RecordEntity();
            item.setId(cursor.getLong(cursor.getColumnIndex(ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            item.setPath(cursor.getString(cursor.getColumnIndex(PATH)));
            item.setLength(cursor.getInt(cursor.getColumnIndex(LENGTH)));
            item.setTime(cursor.getLong(cursor.getColumnIndex(TIME)));
            result.add(item);
        }
        cursor.close();
        return result;
    }


    /**
     * 根据文件路径获取录音对象
     *
     * @param path 文件路径
     * @return 获取录音
     */
    public RecordEntity getRecords(String path) {

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {ID, NAME, PATH, LENGTH, TIME};
        RecordEntity result = null;
        Cursor cursor = db.query(TABLE_NAME, projection, PATH + "=?", new String[]{path}, null, null, TIME + " DESC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            RecordEntity item = new RecordEntity();
            item.setId(cursor.getLong(cursor.getColumnIndex(ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(NAME)));
            item.setPath(cursor.getString(cursor.getColumnIndex(PATH)));
            item.setLength(cursor.getInt(cursor.getColumnIndex(LENGTH)));
            item.setTime(cursor.getLong(cursor.getColumnIndex(TIME)));
            result = item;
        }
        cursor.close();
        return result;
    }

    /**
     * 保存录音
     *
     * @param name   文件名
     * @param path   文件路径
     * @param length 文件大小
     * @return 数据库中的 id
     */
    public long addRecord(String name, String path, long length) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAME, name);
        cv.put(PATH, path);
        cv.put(LENGTH, length);
        cv.put(TIME, System.currentTimeMillis());
        return db.insert(TABLE_NAME, null, cv);
    }

    /**
     * 删除录音
     *
     * @param id 录音id
     */
    public void removeRecordById(long id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {String.valueOf(id)};
        db.delete(TABLE_NAME, ID + "=?", whereArgs);
    }

    /**
     * @return 录音总数
     */
    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {ID};
        Cursor c = db.query(TABLE_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

}
