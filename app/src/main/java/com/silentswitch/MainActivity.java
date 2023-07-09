package com.silentswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.silentswitch.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private  BottomSheetDialog bottomSheetDialog;
    private ViewModel viewModel;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new  ViewModelProvider(this).get(ViewModel.class);

        viewModel.getAllTime().observe(this, new Observer<List<SilentModel>>() {
            @Override
            public void onChanged(List<SilentModel> silentModels) {
                Log.d(TAG, "onChanged: "+silentModels);
            }
        });


        requestMutePermissions();
        showBottomSheetDialog();
        binding.floatingActionButton.setOnClickListener(view -> {
            bottomSheetDialog.show();
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                SilentRoomDatabase.getInstance(MainActivity.this)
                        .switchDao()
                        .getDateAll("1688920740000");
            }
        }).start();
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
        } else{
            // Open Setting screen to ask for permisssion
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivityForResult( intent, 1234 );
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
    private void showBottomSheetDialog() {

       bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_home, null);

        LinearLayout time_based = view.findViewById(R.id.time_based);

        time_based.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateTimeActivity.class);
            startActivity(intent);
        });
//        LinearLayout share = bottomSheetDialog.findViewById(R.id.shareLinearLayout);
//        LinearLayout upload = bottomSheetDialog.findViewById(R.id.uploadLinearLayout);
//        LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
//        LinearLayout delete = bottomSheetDialog.findViewById(R.id.delete);
        bottomSheetDialog.setContentView(view);
    }
}