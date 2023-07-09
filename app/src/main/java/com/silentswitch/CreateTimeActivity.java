package com.silentswitch;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.silentswitch.databinding.ActivityCreateTimeBinding;

import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateTimeActivity extends AppCompatActivity {

    private ActivityCreateTimeBinding binding;
    private Calendar calendar;
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
                                

                                calendar = Calendar.getInstance();
                                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                                calendar.set(Calendar.MINUTE,minute);
                                calendar.set(Calendar.SECOND,0);
                                calendar.set(Calendar.MILLISECOND,0);
                                Log.d(TAG, "onTimeSet: "+calendar.getTimeInMillis());
//                                1688920740000

                                SilentModel silentModel = new SilentModel("ganpati", sysTime, sysTime, "monday", false);
                                insertPatient(silentModel);
                                setAlarm();
                            }
                        }, hour, minute, false);
                // at last we are calling show to
                // display our time picker dialog.
                timePickerDialog.show();
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

    private void setAlarm() {

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(this, 15324, intent,  PendingIntent.FLAG_MUTABLE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();


    }
}