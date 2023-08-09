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
    @Query("UPDATE SILENT_TABLE SET isActive = :b WHERE id = :id")
    void updateSilentMode(String id, boolean b);

    @Query("SELECT * FROM SILENT_TABLE")
    LiveData<List<SilentModel>> getAllTime();

    @Query("SELECT * FROM SILENT_TABLE WHERE isActive =1")
    LiveData<List<SilentModel>> getStatusSilent();

    @Query("SELECT * FROM CONTACT_TABLE")
    LiveData<List<ContactModel>> getContact();

    @Insert()
    void insertContact(ContactModel contactModel);

    @Delete()
    void deleteContact(ContactModel contactModel);

    @Query("SELECT * FROM CONTACT_TABLE WHERE number = :number")
    List<ContactModel> getAllContact(String number);

    @Query("SELECT * FROM SILENT_TABLE WHERE startTime  <= :time")
    List<SilentModel> getStartTimeAll(String time);

    @Query("SELECT * FROM SILENT_TABLE WHERE endTime  <= :time")
    List<SilentModel> getEndTimeAll(String time);
}
