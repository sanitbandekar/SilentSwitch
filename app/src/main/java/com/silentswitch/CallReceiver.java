package com.silentswitch;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class CallReceiver extends BroadcastReceiver {
    private PreferenceManager preferenceManager;
    private static final String TAG = "CallReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("call", "call coming: ");
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        preferenceManager = new PreferenceManager(context);


        if (preferenceManager.getBoolean(Constants.isService)) {
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.d("call", "onReceive: " + incomingNumber);
                if (incomingNumber != null) {
                    if (preferenceManager.getString(Constants.MSG) == null) {
                        sendSMS(context, incomingNumber, "Sorry, I’m not available at the minute");
                    } else {
                        sendSMS(context, incomingNumber, preferenceManager.getString(Constants.MSG));

                    }
                }

                Toast.makeText(context, "Hey! Calling Number : " + incomingNumber, Toast.LENGTH_LONG).show();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<ContactModel> list = SilentRoomDatabase.getInstance(context)
                            .switchDao()
                            .getAllContact(intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER));
                    if (list.size() > 0) {
                        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                            requestUnMutePermissions(context);
                        } else {
                            requestMutePermissions(context);
                        }
                    }
                }
            }).start();
        }
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
                audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            } else if (Build.VERSION.SDK_INT >= 23) {
                this.normalDoNotDisturb(context);
            }
        } catch (SecurityException e) {
            Log.e(TAG, "requestMutePermissions: ", e);
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
    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if (notificationManager.isNotificationPolicyAccessGranted()) {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        }
    }
    public void sendSMS(Context context,String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//            Toast.makeText(context, "Message Sent",
//                    Toast.LENGTH_LONG).show();
            Log.d(TAG, "sendSMS: sent");
        } catch (Exception ex) {
            Log.e(TAG, "sendSMS: ", ex);
//            Toast.makeText(context,ex.getMessage().toString(),
//                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }


}
