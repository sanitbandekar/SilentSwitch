package com.silentswitch;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SwitchDao {

    @Insert()
    void insertTime(SilentModel silentModel);
    @Delete()
    void deleteTime(SilentModel silentModel);

    @Query("UPDATE SILENT_TABLE SET isAlarm = 1 WHERE id = :id")
    void updateAlaram(String id);

    @Query("SELECT * FROM SILENT_TABLE")
    LiveData<List<SilentModel>> getAllTime();

    @Query("SELECT * FROM CONTACT_TABLE")
    LiveData<List<ContactModel>> getContact();

    @Insert()
    void insertContact(ContactModel contactModel);

    @Delete()
    void deleteContact(ContactModel contactModel);

    @Query("SELECT * FROM SILENT_TABLE WHERE startTime  <= :time")
    List<SilentModel> getStartTimeAll(String time);

    @Query("SELECT * FROM SILENT_TABLE WHERE endTime  <= :time")
    List<SilentModel> getEndTimeAll(String time);
}
