package com.silentswitch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.silentswitch.databinding.ActivityCreateTimeBinding;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateTimeActivity extends AppCompatActivity {

    private ActivityCreateTimeBinding binding;
    private Calendar startCalendar, endCalender;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Vibrator mVibrator;
    private static final String TAG = "CreateTimeActivity";
    private static final long[] VIBRATE_PATTERN = {500, 500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        NotificationHelper notificationHelper = new NotificationHelper(this);

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.vibrateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                binding.silentSwitch.setChecked(!b);

                mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                if (!b) {
                    mVibrator.cancel();
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // API 26 and above
                        Log.d(TAG, "vibrate");
                        mVibrator.vibrate(VibrationEffect.createWaveform(VIBRATE_PATTERN, 0));
                        final Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mVibrator.cancel();
                            }
                        }, 2000);
                    } else {
                        // Below API 26
                        mVibrator.vibrate(100);
                    }
                }
            }
        });

        binding.silentSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                binding.vibrateSwitch.setChecked(!b);
            }
        });

        binding.startTimeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateTimeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                long time = new Date().getTime();
                                long sysTime = System.currentTimeMillis();
                                binding.startTime.setText(getTime(hourOfDay, minute));


                                startCalendar = Calendar.getInstance();
                                startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                startCalendar.set(Calendar.MINUTE, minute);
                                startCalendar.set(Calendar.SECOND, 0);
                                startCalendar.set(Calendar.MILLISECOND, 0);
                                Log.d(TAG, "start: " + startCalendar.getTimeInMillis());
//                                1688920740000
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });
        binding.endTimeHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on below line we are getting the
                // instance of our calendar.
                final Calendar c = Calendar.getInstance();

                // on below line we are getting our hour, minute.
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // on below line we are initializing our Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(CreateTimeActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                // on below line we are setting selected time
                                // in our text view.
                                long time = new Date().getTime();
                                long sysTime = System.currentTimeMillis();
                                binding.endTime.setText(getTime(hourOfDay, minute));


                                endCalender = Calendar.getInstance();
                                endCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                endCalender.set(Calendar.MINUTE, minute);
                                endCalender.set(Calendar.SECOND, 0);
                                endCalender.set(Calendar.MILLISECOND, 0);
                                Log.d(TAG, "end: " + endCalender.getTimeInMillis());
//                                1688920740000


                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.title.getText().toString().length() != 0) {
                    binding.prosess.setVisibility(View.VISIBLE);
                    SilentModel silentModel = new SilentModel(binding.title.getText().toString(), startCalendar.getTimeInMillis(), endCalender.getTimeInMillis(), "monday", binding.vibrateSwitch.isChecked(), false,true);
                    insertPatient(silentModel);
                    setAlarm(startCalendar, 140);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setAlarm(endCalender, 138);
                            binding.prosess.setVisibility(View.VISIBLE);
                            notificationHelper.sendHighPriorityNotification("Silent Switch activated", "", MapsActivity.class);
                            finish();
                        }
                    }, 3000);
                } else {
//                    Toast.makeText(CreateTimeActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    Snackbar.make(view, "Oops! Schedule name required", Snackbar.LENGTH_SHORT).setBackgroundTint(Color.RED).setTextColor(Color.WHITE).show();
                }
            }
        });
    }

    private void insertPatient(SilentModel silentModel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SilentRoomDatabase.getInstance(CreateTimeActivity.this)
                        .switchDao()
                        .insertTime(silentModel);
            }
        }).start();
    }

    private String getTime(int hr, int min) {
        Time tme = new Time(hr, min, 0);//seconds by default set to zero
        Format formatter;
        formatter = new SimpleDateFormat("h:mm a", Locale.getDefault());
        return formatter.format(tme);
    }

    private void setAlarm(Calendar calendar, int code) {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, code, intent, PendingIntent.FLAG_MUTABLE);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()+100, pendingIntent);

//        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();


    }
}