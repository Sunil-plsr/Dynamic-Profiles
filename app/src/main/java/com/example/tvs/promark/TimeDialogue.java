package com.example.tvs.promark;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimeDialogue extends Dialog implements View.OnClickListener {

    private Profile.Trigger trigger;
    private TextView fromTimeValue, toTimeValue;
    private Button cancel, saveSchedule;
    private ImageView fromTimeSetter, toTimeSetter;
    private TimeScheduleHandler timeSchedulHandler;
    private final Calendar c = Calendar.getInstance();
    private String defaultFromTime, defaultToTime;
    private String fromTime, toTime;
    private int fromMinute, toMinute, fromHour, toHour;

    public TimeDialogue(Context context, Profile.Trigger trigger, TimeScheduleHandler timeSchedulHandler) {
        super(context);
        this.trigger = trigger;
        this.timeSchedulHandler = timeSchedulHandler;
        this.fromTime = trigger.fromTime;
        this.toTime = trigger.toTime;
        this.defaultFromTime = trigger.fromTime;
        this.defaultToTime = trigger.toTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_dialogue);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(params);

        fromTimeValue = (TextView) findViewById(R.id.fromTimeValue);
        toTimeValue = (TextView) findViewById(R.id.toTimeValue);
        fromTimeSetter = (ImageView) findViewById(R.id.fromTimeSetter);
        toTimeSetter = (ImageView) findViewById(R.id.toTimeSetter);
        saveSchedule = (Button) findViewById(R.id.setSchedule);
        cancel = (Button) findViewById(R.id.cancelScheduler);

        saveSchedule.setOnClickListener(this);
        fromTimeSetter.setOnClickListener(this);
        toTimeSetter.setOnClickListener(this);
        cancel.setOnClickListener(this);

        setData(trigger);
    }

    private void setData(Profile.Trigger trigger) {
        if(trigger.fromTime != null && trigger.toTime != null) {
            fromMinute = Integer.parseInt(trigger.fromTime.split(":|\\s")[1]);
            toMinute = Integer.parseInt(trigger.toTime.split(":|\\s")[1]);
            fromHour = Integer.parseInt(trigger.fromTime.split(":|\\s")[0]);
            toHour = Integer.parseInt(trigger.toTime.split(":|\\s")[0]);
            fromTimeValue.setText(getTimeInString(fromHour, fromMinute));
            toTimeValue.setText(getTimeInString(toHour, toMinute));
        } else {
            fromHour = c.get(Calendar.HOUR_OF_DAY);
            fromMinute = c.get(Calendar.MINUTE);
            toHour = c.get(Calendar.HOUR_OF_DAY);
            toMinute = c.get(Calendar.MINUTE);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fromTimeSetter:
                showTimePicker(fromTimeValue, fromHour, fromMinute, 0);
                break;
            case R.id.toTimeSetter:
                showTimePicker(toTimeValue, toHour, toMinute, 1);
                break;
            case R.id.cancelScheduler:
                timeSchedulHandler.timeSet(defaultFromTime, defaultToTime);
                dismiss();
                break;
            case R.id.setSchedule:
                if(fromTimeValue.getText().toString() == null || toTimeValue.getText().toString() == null)
                    timeSchedulHandler.timeSet(defaultFromTime, defaultToTime);
                else timeSchedulHandler.timeSet(fromTime, toTime);
                dismiss();
                break;
        }
    }

    private void showTimePicker(final TextView textView, int hour, int minute, final int flag) {
        (new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(flag==1) toTime = hourOfDay + ":" + minute;
                else fromTime = hourOfDay + ":" + minute;

                textView.setText(getTimeInString(hourOfDay, minute));
            }
        }, hour, minute, DateFormat.is24HourFormat(getContext()))).show();
    }

    private String getTimeInString(int hourOfDay, int minute) {
        int x;
        return ((x = hourOfDay%12)==0?12:x) + ":" + String.format("%1$02d", minute) + (hourOfDay>=12?" PM":" AM");
    }

    public interface TimeScheduleHandler {
        void timeSet(String fromTime, String toTime);
    }
}
