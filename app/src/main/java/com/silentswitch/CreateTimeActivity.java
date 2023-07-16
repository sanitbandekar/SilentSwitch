package com.silentswitch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
    private static final String TAG = "CreateTimeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateTimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                    SilentModel silentModel = new SilentModel(binding.title.getText().toString(), startCalendar.getTimeInMillis(), endCalender.getTimeInMillis(), "monday", false);
                    insertPatient(silentModel);
                    setAlarm(startCalendar,140);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setAlarm(endCalender,138);
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

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();


    }
}