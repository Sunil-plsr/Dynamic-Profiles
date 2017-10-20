package com.example.tvs.promark;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class Scheduler extends BroadcastReceiver {

    private static final Calendar c = Calendar.getInstance();
    private static String currentTime = c.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%1$02d", c.get(Calendar.MINUTE));

    @Override
    public void onReceive(Context context, Intent intent) {
        activateProfile(context);
    }

    public static void activateProfile(Context context) {
        int wifiStatus = 0;
        int mobileCharge = 0;

        ArrayList<Profile> profiles = (new DatabaseManager(context)).getActiveProfile();
        Profile profile;
        Iterator<Profile> iterator = profiles.iterator();
        while (iterator.hasNext()) {
            profile = iterator.next();

            if(profile == null || profile.active == 0) return;

            ConnectivityManager connexionManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connexionManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi.isConnected()) wifiStatus = 1;

            BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                mobileCharge = (int) batteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            if(profile.trigger.wiFi != wifiStatus) continue;
            else if(profile.trigger.wifiId!=null&&!wifiManager.getConnectionInfo().getSSID().equals(profile.trigger.wifiId)) continue;

            TelephonyManager systemService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if(profile.trigger.cellNetwork==1)
                try {
                    if(!systemService.getDeviceId().equals(profile.trigger.cellNetworkId)) continue;
                } catch(Exception e) {}

            if(!(profile.trigger.minCharge <= mobileCharge && profile.trigger.maxCharge >= mobileCharge)) continue;
            if(profile.trigger.fromTime != null && profile.trigger.toTime != null)
                if(!timeLiesBetween(profile.trigger.fromTime, profile.trigger.toTime, currentTime))
                    continue;

            wifiManager.setWifiEnabled(profile.action.wiFi==1);

            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if(bluetoothAdapter != null) {
                if (profile.action.bluetooth == 1) bluetoothAdapter.enable();
                else bluetoothAdapter.disable();
            }

            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_RING, profile.action.ringingVolume, 0);

            try {
                if(profile.action.autoBrightness==1)
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
                else Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            } catch(SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean timeLiesBetween(String fromDate, String toDate, String currentTime) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        try {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(dateFormat.parse(fromDate));
            calendar1.add(Calendar.DATE, 1);

            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(dateFormat.parse(toDate));
            calendar2.add(Calendar.DATE, 1);

            Calendar calendar3 = Calendar.getInstance();
            calendar3.setTime(dateFormat.parse(currentTime));
            calendar3.add(Calendar.DATE, 1);

            Date currentTimeInDate = calendar3.getTime();
            if(calendar1.getTime().after(calendar2.getTime()))
                calendar2.add(Calendar.DATE, 2);
            if ((currentTimeInDate.after(calendar1.getTime()) || currentTimeInDate.equals(calendar1.getTime())) &&
                    currentTimeInDate.before(calendar2.getTime()) || (currentTimeInDate.equals(calendar2.getTime())))
                return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}