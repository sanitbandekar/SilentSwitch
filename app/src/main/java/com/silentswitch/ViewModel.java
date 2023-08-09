package com.silentswitch;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private  SilentRoomDatabase silentRoomDatabase;
    public ViewModel(@NonNull Application application) {
        super(application);

        silentRoomDatabase =SilentRoomDatabase.getInstance(application.getApplicationContext());
    }

    public LiveData<List<SilentModel>> getAllTime(){
        return silentRoomDatabase.switchDao().getAllTime();
    }

    public LiveData<List<SilentModel>> getStatusSilent(){
        return silentRoomDatabase.switchDao().getStatusSilent();
    }
    public LiveData<List<ContactModel>> getContact(){
        return silentRoomDatabase.switchDao().getContact();
    }
}
