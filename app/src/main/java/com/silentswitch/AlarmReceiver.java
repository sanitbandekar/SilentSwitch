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
    private boolean isAlaram;

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
//                        if (isAlaram) {
//                            isAlaram = false;
//                            normalDoNotDisturb(context);
//                        }else {
//                            requestMutePermissions(context);
//                            isAlaram = true;
//                        }
//                    }
//            }
//        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                List<SilentModel> list = SilentRoomDatabase.getInstance(context)
                        .switchDao()
                        .getStartTimeAll(currentTime);
                if (list.size() > 0) {
                    Log.d(TAG, "run: normal" + list);
                    for (int i = 0; i < list.size(); i++) {

                        if (list.get(i).isAlarm()) {
                            requestUnMutePermissions(context);
                        }else {
                            upDateAlaram(context,String.valueOf(list.get(i).getId()));
                            if (list.get(i).isSilent()){
                                requestVibratePermissions(context);
                            }else {
                            requestMutePermissions(context);
                            }
                        }
                    }
                }
            }
        }).start();
    }

    private void upDateAlaram(Context context, String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SilentRoomDatabase.getInstance(context)
                        .switchDao()
                        .updateAlaram(id);
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

    public void requestVibratePermissions(Context context) {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.requestForVibrate(context);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "requestMutePermissions: ", e);
        }
    }
    private void requestForVibrate(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
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
