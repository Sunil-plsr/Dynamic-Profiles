package com.example.tvs.promark;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import io.apptik.widget.MultiSlider;

public class BatteryDialogue extends Dialog implements View.OnClickListener {

    private MultiSlider batteryLimitRange;
    private Profile.Trigger trigger;
    private Button cancelBattery, setBattery;
    private TextView minBatteryVal, maxBatteryVal;
    private BatteryManagerHandler batteryManagerHandler;
    private int minCharge, maxCharge, defaultMinCharge, defaultMaxCharge;

    public BatteryDialogue(Context context, Profile.Trigger trigger, BatteryManagerHandler batteryManagerHandler) {
        super(context);
        this.trigger = trigger;
        this.batteryManagerHandler = batteryManagerHandler;
        this.maxCharge = trigger.maxCharge;
        this.minCharge = trigger.minCharge;
        this.defaultMaxCharge = trigger.maxCharge;
        this.defaultMinCharge = trigger.minCharge;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battery_dialogue);

        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(layoutParams);

        minBatteryVal = (TextView) findViewById(R.id.minBatteryVal);
        maxBatteryVal = (TextView) findViewById(R.id.maxBatteryVal);
        setBattery = (Button) findViewById(R.id.setBattery);
        cancelBattery = (Button) findViewById(R.id.cancelBattery);
        batteryLimitRange = (MultiSlider) findViewById(R.id.batteryLimitRange);

        setBattery.setOnClickListener(this);
        cancelBattery.setOnClickListener(this);

        setData(trigger);

        batteryLimitRange.setOnThumbValueChangeListener(new MultiSlider.OnThumbValueChangeListener() {
            @Override
            public void onValueChanged(MultiSlider multiSlider, MultiSlider.Thumb thumb, int thumbIndex, int value) {
                if(thumbIndex==0) {
                    minBatteryVal.setText(value + "%");
                    minCharge = value;
                }
                else {
                    maxBatteryVal.setText(value + "%");
                    maxCharge = value;
                }
            }
        });
    }

    private void setData(Profile.Trigger trigger) {
        minBatteryVal.setText(trigger.minCharge + "%");
        maxBatteryVal.setText(trigger.maxCharge + "%");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.setBattery:
                batteryManagerHandler.batterySet(minCharge, maxCharge);
                dismiss();
                break;
            case R.id.cancelBattery:
                batteryManagerHandler.batterySet(defaultMinCharge, defaultMaxCharge);
                dismiss();
                break;
        }
    }

    public interface BatteryManagerHandler {
        void batterySet(int minCharge, int maxCharge);
    }
}
