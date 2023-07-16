package com.silentswitch;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SwitchDao {

    @Insert()
    void insertTime(SilentModel silentModel);

    @Query("SELECT * FROM SILENT_TABLE")
    LiveData<List<SilentModel>> getAllTime();

    @Query("SELECT * FROM SILENT_TABLE WHERE startTime  <= :time")
    List<SilentModel> getStartTimeAll(String time);

    @Query("SELECT * FROM SILENT_TABLE WHERE endTime  <= :time")
    List<SilentModel> getEndTimeAll(String time);
}
