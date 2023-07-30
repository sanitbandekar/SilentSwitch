package com.silentswitch;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {SilentModel.class,ContactModel.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class SilentRoomDatabase extends RoomDatabase {


    public abstract SwitchDao switchDao();
    private static volatile SilentRoomDatabase INSTANCE;

    public static SilentRoomDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SilentRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), SilentRoomDatabase.class,Constants.LOCAL_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
