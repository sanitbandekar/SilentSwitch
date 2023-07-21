package com.silentswitch;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.silentswitch.databinding.ActivityPermissionBinding;

public class PermissionActivity extends AppCompatActivity {
    private ActivityPermissionBinding binding;
    private static final String TAG = "PermissionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        goToHome();

        binding.openSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMutePermissions();
            }
        });

    }
    public void requestMutePermissions() {
        try {
            if (Build.VERSION.SDK_INT < 23) {
                AudioManager audioManager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            } else if( Build.VERSION.SDK_INT >= 23 ) {
                this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
            }
        } catch ( SecurityException e ) {
            Log.e(TAG, "requestMutePermissions: ",e );
        }
    }

    private void requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp() {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        // if user granted access else ask for permission
        if ( notificationManager.isNotificationPolicyAccessGranted()) {
//            AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
//            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            goToHome();
        } else{
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult( intent, 1234 );
        }
    }

    private void goToHome() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent =new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234) {
            this.requestForDoNotDisturbPermissionOrSetDoNotDisturbForApi23AndUp();
        }
    }


}