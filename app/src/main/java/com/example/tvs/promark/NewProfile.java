package com.example.tvs.promark;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class NewProfile extends Fragment implements View.OnClickListener,
        TimeDialogue.TimeScheduleHandler, BatteryDialogue.BatteryManagerHandler, VolumeDialogue.VolumeHandler {

    private Button cancel, addNewProfile;
    private Profile profile;
    private TextView new_profile_title, profileNameField;
    private ImageView volumeClicker, batteryClicker, timeClicker;
    private Switch wifiSwitchS, cellNetwork, wifiSwitchR;
    private Switch bluetoothResponse, autoBrightness;
    private int MODE = 0, ID_UNDER_VIEW, active;
    private String fromTime, toTime, wifiId, cellNetworkId;
    private int maxCharge, minCharge, volume;

    public NewProfile() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new_profile_title = (TextView) getActivity().findViewById(R.id.new_profile_title);
        profileNameField = (TextView) getActivity().findViewById(R.id.profileNameField);
        cancel = (Button) getActivity().findViewById(R.id.cancelNewProfile);
        addNewProfile = (Button) getActivity().findViewById(R.id.createProfileBtn);
        timeClicker = (ImageView) getActivity().findViewById(R.id.timeClicker);
        batteryClicker = (ImageView) getActivity().findViewById(R.id.batteryClicker);
        volumeClicker = (ImageView) getActivity().findViewById(R.id.volumeClicker);
        wifiSwitchS = (Switch) getActivity().findViewById(R.id.wifiSwitch);
        wifiSwitchR = (Switch) getActivity().findViewById(R.id.wifiResponse);
        cellNetwork = (Switch) getActivity().findViewById(R.id.cellNetwork);
        bluetoothResponse = (Switch) getActivity().findViewById(R.id.bluetoothResponse);
        autoBrightness = (Switch) getActivity().findViewById(R.id.autoBrightness);

        volumeClicker.setOnClickListener(this);
        batteryClicker.setOnClickListener(this);
        timeClicker.setOnClickListener(this);
        cancel.setOnClickListener(this);
        addNewProfile.setOnClickListener(this);

        if(profile != null && profile.id != 0) {
            MODE = 1;
            ID_UNDER_VIEW = profile.id;
            new_profile_title.setText(getActivity().getResources().getString(R.string.edit_profile_title));
            addNewProfile.setText(getActivity().getResources().getString(R.string.edit_profile_btn));
        }

        setFields(profile);
    }

    private void setFields(Profile profile) {
        profileNameField.setText(profile.name);

        wifiSwitchS.setChecked(profile.trigger.wiFi==1);
        cellNetwork.setChecked(profile.trigger.cellNetwork==1);

        wifiId = profile.trigger.wifiId;
        cellNetworkId = profile.trigger.cellNetworkId;
        fromTime = profile.trigger.fromTime;
        toTime = profile.trigger.toTime;
        volume = profile.action.ringingVolume;
        active = profile.active;
        minCharge = profile.trigger.minCharge;
        maxCharge = profile.trigger.maxCharge;

        wifiSwitchR.setChecked(profile.action.wiFi==1);
        bluetoothResponse.setChecked(profile.action.bluetooth==1);
        autoBrightness.setChecked(profile.action.autoBrightness==1);
    }

    private Profile getProfile() {
        Profile profile = new Profile();

        profile.active = active;

        profile.id = ID_UNDER_VIEW;
        profile.name = profileNameField.getText().toString().trim();
        if(profile.name==null) profile.name = "Default";

        if(wifiSwitchS.isChecked()) {
            profile.trigger.wiFi = 1;
            // If WiFi is not connected, then connect to one to get an SSID
            WifiManager wifiManager = (WifiManager) getActivity().getSystemService(Context.WIFI_SERVICE);
            if(!wifiManager.isWifiEnabled())
                wifiManager.setWifiEnabled(true);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            profile.trigger.wifiId = wifiInfo.getSSID();
        } else {
            profile.trigger.wiFi = 0;
            profile.trigger.wifiId = wifiId;
        }

        Log.d("WiFi ID", profile.trigger.wifiId + " ");

        profile.trigger.cellNetwork = cellNetwork.isChecked()?1:0;
        TelephonyManager tel = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        try {
            profile.trigger.cellNetworkId = tel.getDeviceId();
        } catch(Exception e) {
            profile.trigger.cellNetworkId = null;
        }

        profile.trigger.fromTime = fromTime;
        profile.trigger.toTime = toTime;
        profile.trigger.maxCharge = maxCharge;
        profile.trigger.minCharge = minCharge;

        profile.action.wiFi = (profile.trigger.wiFi==1)?1:(wifiSwitchR.isChecked()?1:0);
        profile.action.bluetooth = bluetoothResponse.isChecked()?1:0;
        profile.action.autoBrightness = autoBrightness.isChecked()?1:0;
        profile.action.ringingVolume = volume;

        return profile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_profile, container, false);
    }

    public void profile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.cancelNewProfile:
                getFragmentManager().popBackStack();
                break;
            case R.id.volumeClicker:
                VolumeDialogue volumeManager = new VolumeDialogue(getActivity(), profile.action, this);
                volumeManager.show();
                break;
            case R.id.batteryClicker:
                BatteryDialogue batteryDialogue = new BatteryDialogue(getActivity(), profile.trigger, this);
                batteryDialogue.show();
                break;
            case R.id.timeClicker:
                TimeDialogue scheduler = new TimeDialogue(getActivity(), profile.trigger, this);
                scheduler.show();
                break;
            case R.id.createProfileBtn:
                if(MODE == 0) {
                    MainActivity.databaseManager.saveProfile(getProfile());
                    Toast.makeText(getActivity(), getResources().getString(R.string.profile_added_msg), Toast.LENGTH_SHORT).show();
                }
                else {
                    Profile updatedProfile = getProfile();
                    MainActivity.databaseManager.updateProfile(updatedProfile);
                    if(updatedProfile.active==1)
                        Scheduler.activateProfile(getActivity());
                    Toast.makeText(getActivity(), getResources().getString(R.string.profile_updated_msg), Toast.LENGTH_SHORT).show();
                }
                getFragmentManager().popBackStack();
                break;
        }
    }

    @Override
    public void timeSet(String fromTime, String toTime) {
        this.profile.trigger.fromTime = fromTime;
        this.fromTime = fromTime;
        this.profile.trigger.toTime = toTime;
        this.toTime = toTime;
    }

    @Override
    public void batterySet(int minCharge, int maxCharge) {
        this.profile.trigger.minCharge= minCharge;
        this.minCharge = minCharge;
        this.profile.trigger.maxCharge= maxCharge;
        this.maxCharge = maxCharge;
    }

    @Override
    public void volumeSet(int volume) {
        this.profile.action.ringingVolume = volume;
        this.volume = volume;
    }
}
