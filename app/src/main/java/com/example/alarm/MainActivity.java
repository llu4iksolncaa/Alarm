package com.example.alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText edtSeconds;
    Button btnStartTimer;
    Calendar dateTime = Calendar.getInstance();
    TimePickerDialog.OnTimeSetListener picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtSeconds = findViewById(R.id.edtSeconds);
        btnStartTimer = findViewById(R.id.btnStartTimer);
        picker = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                dateTime.set(Calendar.MINUTE, minute);
                dateTime.set(Calendar.SECOND, 0);
                edtSeconds.setText(DateUtils.formatDateTime(getApplicationContext(), dateTime.getTimeInMillis(), DateUtils.FORMAT_SHOW_TIME));
            }
        };
        edtSeconds.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    new TimePickerDialog(MainActivity.this, picker,
                            dateTime.get(Calendar.HOUR_OF_DAY),
                            dateTime.get(Calendar.MINUTE), true).show();
                }
                return true;
            }
        });
        btnStartTimer.setOnClickListener(view -> {
            long seconds = dateTime.getTimeInMillis();
            if (dateTime.getTimeInMillis() < System.currentTimeMillis()) seconds += 86400000;
            Toast.makeText(getApplicationContext(), "Будильник установлен на " + DateUtils.formatDateTime(getApplicationContext(), seconds, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Timer.class);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, seconds, PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0));
        });
    }
}