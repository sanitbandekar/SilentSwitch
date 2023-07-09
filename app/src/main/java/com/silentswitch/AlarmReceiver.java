package com.silentswitch;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class    AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = "AlarmReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "onReceive: "+System.currentTimeMillis());
       requestMutePermissions(context);
    }
    public void requestMutePermissions(Context context) {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if( Build.VERSION.SDK_INT >= 23 ) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp(context);
            }
        } catch ( SecurityException e ) {
            Log.e(TAG, "requestMutePermissions: ",e );
        }
    }

    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if ( notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }


}
