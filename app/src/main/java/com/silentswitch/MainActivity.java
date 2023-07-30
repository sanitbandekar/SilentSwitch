package com.silentswitch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.silentswitch.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainRecycleAdapter.OnItemClick {
    private ActivityMainBinding binding;
    private  BottomSheetDialog bottomSheetDialog;
    private ViewModel viewModel;
    private MainRecycleAdapter adapter;
    private List<SilentModel> silentModels;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        silentModels = new ArrayList<>();
        adapter = new MainRecycleAdapter(silentModels, this,this);
        binding.recycleView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.recycleView.addItemDecoration(dividerItemDecoration);

        viewModel = new  ViewModelProvider(this).get(ViewModel.class);

        viewModel.getAllTime().observe(this, new Observer<List<SilentModel>>() {
            @Override
            public void onChanged(List<SilentModel> silentModels) {
                Log.d(TAG, "onChanged: "+silentModels);
                if (silentModels != null) {
                    adapter.setSilentModels(silentModels);
                    if (silentModels.size() !=0){
                        binding.isEmpty.setVisibility(View.GONE);
                    }
                }
            }
        });

        requestMutePermissions();
        showBottomSheetDialog();
        binding.floatingActionButton.setOnClickListener(view -> {
            bottomSheetDialog.show();

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
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(MainActivity.this, CreateTimeActivity.class);
            startActivity(intent);
        });
//        LinearLayout share = bottomSheetDialog.findViewById(R.id.shareLinearLayout);
//        LinearLayout upload = bottomSheetDialog.findViewById(R.id.uploadLinearLayout);
//        LinearLayout download = bottomSheetDialog.findViewById(R.id.download);
//        LinearLayout delete = bottomSheetDialog.findViewById(R.id.delete);
        bottomSheetDialog.setContentView(view);
    }

    @Override
    public void OnClick(SilentModel silentModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                 SilentRoomDatabase.getInstance(MainActivity.this)
                        .switchDao()
                        .deleteTime(silentModel);

            }
        }).start();
    }

    @Override
    public void isActivated(boolean b) {
        if (!b) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            PendingIntent mAlarmPendingIntent = PendingIntent.getActivity(this, 140, intent, PendingIntent.FLAG_MUTABLE);

            alarmManager.cancel(mAlarmPendingIntent);
        }
    }
}