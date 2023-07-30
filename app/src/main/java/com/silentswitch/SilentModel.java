package com.silentswitch;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "silent_table")
public class SilentModel {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "startTime")
    private long startTime;

    @ColumnInfo(name = "endTime")
    private long endTime;

    @ColumnInfo(name = "day")
    private String day;

    @ColumnInfo(name = "isSilent")
    private boolean isSilent;
    @ColumnInfo(name = "isAlarm")
    private boolean isAlarm;

    @ColumnInfo(name = "isActive")
    private boolean isActive;


    public SilentModel(String name, long startTime, long endTime, String day, boolean isSilent, boolean isAlarm, boolean isActive) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.day = day;
        this.isSilent = isSilent;
        this.isAlarm = isAlarm;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isSilent() {
        return isSilent;
    }

    public void setSilent(boolean silent) {
        isSilent = silent;
    }

    public boolean isAlarm() {
        return isAlarm;
    }

    public void setAlarm(boolean alarm) {
        isAlarm = alarm;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public String toString() {
        return "SilentModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", day='" + day + '\'' +
                ", isSilent=" + isSilent +
                ", isAlarm=" + isAlarm +
                '}';
    }
}
