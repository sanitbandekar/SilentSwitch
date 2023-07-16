package com.silentswitch;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import java.util.List;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        String currentTime = String.valueOf(System.currentTimeMillis());
        Log.d(TAG, "onReceive: " + currentTime);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<SilentModel> list = SilentRoomDatabase.getInstance(context)
//                        .switchDao()
//                        .getStartTimeAll(currentTime);
//                    if (list.size() >0 ){
//                        requestMutePermissions(context);
//                        Log.d(TAG, "run: do not");
//                    }
//            }
//        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SilentModel> list = SilentRoomDatabase.getInstance(context)
                        .switchDao()
                        .getEndTimeAll(currentTime);
                if (list.size() > 0) {
                    Log.d(TAG, "run: normal" + list);
                    for (int i = 0; i < list.size(); i++) {
                         int obj = Long.compare( Long.parseLong(currentTime),list.get(i).getEndTime());
                         int start = Long.compare( Long.parseLong(currentTime),list.get(i).getStartTime() );
                        Log.d(TAG, "run: end"+obj);
                        Log.d(TAG, "run: start"+start);
//                        if (obj > 0) {
//                            Log.d(TAG, "run: unmute");
//                            requestUnMutePermissions(context);
//                        } else if (list.get(i).getStartTime() >= Long.parseLong(currentTime)) {
//                            requestMutePermissions(context);
//                            Log.d(TAG, "run: mute");
//                        }

                    }
                }
            }
        }).start();
    }

    public void requestMutePermissions(Context context) {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp(context);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "requestMutePermissions: ", e);
        }
    }

    public void requestUnMutePermissions(Context context) {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.normalDoNotDisturb(context);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "requestMutePermissions: ", e);
        }
    }

    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }

    private void normalDoNotDisturb(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
    }


}
