package com.example.tvs.promark;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ProfileTable {

    public static final String TABLE_NAME = "ProfileTable";
    public static final String COLUMN_ID = "Id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_ACTIVE = "Active";

    public static final String COLUMN_STIMULUS_WIFI = "StimulusWifi";
    public static final String COLUMN_STIMULUS_WIFI_ID = "StimulusWifiId";
    public static final String COLUMN_STIMULUS_CELL_NETWORK = "StimulusCellNetwork";
    public static final String COLUMN_STIMULUS_CELL_NETWORK_ID = "StimulusCellNetworkId";
    public static final String COLUMN_STIMULUS_TIME_FROM = "StimulusTimeFrom";
    public static final String COLUMN_STIMULUS_TIME_TO = "StimulusTimeTo";
    public static final String COLUMN_STIMULUS_BATTERY_MIN = "StimulusBatteryMin";
    public static final String COLUMN_STIMULUS_BATTERY_MAX = "StimulusBatteryMax";

    public static final String COLUMN_RESPONSE_WIFI = "ResponseWifi";
    public static final String COLUMN_RESPONSE_BLUETOOTH = "RepsonseBluetooth";
    public static final String COLUMN_RESPONSE_AUTO_BRIGHTNESS = "ResponseAutobrightness";
    public static final String COLUMN_RESPONSE_RING_VOLUME = "ResponseRingingVol";

    public static void onCreate(SQLiteDatabase database) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE " + ProfileTable.TABLE_NAME + " (");
        stringBuilder.append(ProfileTable.COLUMN_ID + " integer primary key autoincrement, ");
        stringBuilder.append(ProfileTable.COLUMN_NAME + " text not null, ");
        stringBuilder.append(ProfileTable.COLUMN_ACTIVE + " integer not null, ");
        stringBuilder.append(ProfileTable.COLUMN_STIMULUS_WIFI + " integer not null, ");
        stringBuilder.append(ProfileTable.COLUMN_STIMULUS_WIFI_ID + " integer, ");
        stringBuilder.append(ProfileTable.COLUMN_STIMULUS_CELL_NETWORK + " integer not null, ");
        stringBuilder.append(ProfileTable.COLUMN_STIMULUS_CELL_NETWORK_ID + " string, ");
        stringBuilder.append(ProfileTable.COLUMN_STIMULUS_TIME_FROM + " text, ");
        stringBuilder.append(ProfileTable.COLUMN_STIMULUS_TIME_TO + " text, ");
        stringBuilder.append(ProfileTable.COLUMN_STIMULUS_BATTERY_MIN + " integer, ");
        stringBuilder.append(ProfileTable.COLUMN_STIMULUS_BATTERY_MAX + " integer, ");
        stringBuilder.append(ProfileTable.COLUMN_RESPONSE_WIFI + " integer not null, ");
        stringBuilder.append(ProfileTable.COLUMN_RESPONSE_BLUETOOTH + " integer not null, ");
        stringBuilder.append(ProfileTable.COLUMN_RESPONSE_AUTO_BRIGHTNESS + " integer not null, ");
        stringBuilder.append(ProfileTable.COLUMN_RESPONSE_RING_VOLUME + " integer not null);");
        try {
            database.execSQL(stringBuilder.toString());
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DROP TABLE IF EXISTS " + ProfileTable.TABLE_NAME);
        try {
            database.execSQL(stringBuilder.toString());
            onCreate(database);
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }
}
