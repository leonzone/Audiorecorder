package com.reiser.audiorecorder.bean;

import java.io.Serializable;

/**
 * Created by reiserx on 2018/12/10.
 * desc : 录音实体
 */
public class RecordEntity implements Serializable {
    private long id;
    private String name;
    private String path;
    private long time;
    private int length;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
