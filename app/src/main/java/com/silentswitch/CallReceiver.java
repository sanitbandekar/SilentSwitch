package com.silentswitch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {
    private PreferenceManager preferenceManager;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("call", "call coming: ");
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        preferenceManager = new PreferenceManager(context);


        if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Log.d("call", "onReceive: "+incomingNumber);
            if (preferenceManager.getString(Constants.MSG)!= null) {
                sendSMS(context, incomingNumber, "Sorry, I’m not available at the minute");
            }else {
                sendSMS(context, incomingNumber, preferenceManager.getString(Constants.MSG));

            }
            Toast.makeText(context, "Hey! Calling Number : " + incomingNumber, Toast.LENGTH_LONG).show();
        }
    }

    public void sendSMS(Context context,String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(context, "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(context,ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
