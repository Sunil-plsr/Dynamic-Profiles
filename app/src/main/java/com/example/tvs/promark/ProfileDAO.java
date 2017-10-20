package com.example.tvs.promark;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class ProfileDAO {

    SQLiteDatabase sqLiteDatabase;

    public ProfileDAO(SQLiteDatabase sqLiteDatabase) {
        this.sqLiteDatabase = sqLiteDatabase;
    }

    public long save(Profile profile) {
        ContentValues values = prepareContentValues(profile);
        return sqLiteDatabase.insert(ProfileTable.TABLE_NAME, null, values);
    }

    public int update(Profile profile) {
        ContentValues values = prepareContentValues(profile);
        return sqLiteDatabase.update(ProfileTable.TABLE_NAME, values, ProfileTable.COLUMN_ID + "=?",
                new String[] {profile.id + ""});
    }

    public int delete(long id) {
        return sqLiteDatabase.delete(ProfileTable.TABLE_NAME, ProfileTable.COLUMN_ID + "=?", new String[] {id + ""});
    }

    public int activate(long id) {
        ContentValues value = new ContentValues();
        value.put(ProfileTable.COLUMN_ACTIVE, 1);
        return sqLiteDatabase.update(ProfileTable.TABLE_NAME, value, ProfileTable.COLUMN_ID + "=?", new String[] {id + " "});
    }

    public int deactivate(long id) {
        ContentValues value = new ContentValues();
        value.put(ProfileTable.COLUMN_ACTIVE, 0);
        return sqLiteDatabase.update(ProfileTable.TABLE_NAME, value, ProfileTable.COLUMN_ID + "=?", new String[] {id + " "});
    }

    private static ContentValues prepareContentValues(Profile profile) {
        ContentValues values = new ContentValues();
        values.put(ProfileTable.COLUMN_NAME, profile.name);
        values.put(ProfileTable.COLUMN_ACTIVE, profile.active);

        values.put(ProfileTable.COLUMN_STIMULUS_WIFI, profile.trigger.wiFi);
        values.put(ProfileTable.COLUMN_STIMULUS_WIFI_ID, profile.trigger.wifiId);
        values.put(ProfileTable.COLUMN_STIMULUS_CELL_NETWORK, profile.trigger.cellNetwork);
        values.put(ProfileTable.COLUMN_STIMULUS_CELL_NETWORK_ID, profile.trigger.cellNetworkId);
        values.put(ProfileTable.COLUMN_STIMULUS_TIME_FROM, profile.trigger.fromTime);
        values.put(ProfileTable.COLUMN_STIMULUS_TIME_TO, profile.trigger.toTime);
        values.put(ProfileTable.COLUMN_STIMULUS_BATTERY_MIN, profile.trigger.minCharge);
        values.put(ProfileTable.COLUMN_STIMULUS_BATTERY_MAX, profile.trigger.maxCharge);

        values.put(ProfileTable.COLUMN_RESPONSE_WIFI, profile.action.wiFi);
        values.put(ProfileTable.COLUMN_RESPONSE_BLUETOOTH, profile.action.bluetooth);
        values.put(ProfileTable.COLUMN_RESPONSE_AUTO_BRIGHTNESS, profile.action.autoBrightness);
        values.put(ProfileTable.COLUMN_RESPONSE_RING_VOLUME, profile.action.ringingVolume);

        return values;
    }

    public ArrayList<Profile> getActive() {
        ArrayList<Profile> profiles = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(ProfileTable.TABLE_NAME, new String[]{ProfileTable.COLUMN_ID, ProfileTable.COLUMN_NAME,
                        ProfileTable.COLUMN_ACTIVE, ProfileTable.COLUMN_STIMULUS_WIFI, ProfileTable.COLUMN_STIMULUS_WIFI_ID,
                        ProfileTable.COLUMN_STIMULUS_CELL_NETWORK, ProfileTable.COLUMN_STIMULUS_CELL_NETWORK_ID, ProfileTable.COLUMN_STIMULUS_TIME_FROM, ProfileTable.COLUMN_STIMULUS_TIME_TO,
                        ProfileTable.COLUMN_STIMULUS_BATTERY_MIN, ProfileTable.COLUMN_STIMULUS_BATTERY_MAX, ProfileTable.COLUMN_RESPONSE_WIFI,
                        ProfileTable.COLUMN_RESPONSE_BLUETOOTH, ProfileTable.COLUMN_RESPONSE_AUTO_BRIGHTNESS, ProfileTable.COLUMN_RESPONSE_RING_VOLUME},
                ProfileTable.COLUMN_ACTIVE + "=?", new String[] {1 + ""}, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                profiles.add(buildProfileFromCursor(cursor));
            } while (cursor.moveToNext());
        }
        if(!cursor.isClosed())
            cursor.close();
        return profiles;
    }

    public ArrayList<Profile> getAll() {
        ArrayList<Profile> profiles = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(ProfileTable.TABLE_NAME, new String[]{ProfileTable.COLUMN_ID, ProfileTable.COLUMN_NAME,
                        ProfileTable.COLUMN_ACTIVE, ProfileTable.COLUMN_STIMULUS_WIFI, ProfileTable.COLUMN_STIMULUS_WIFI_ID,
                        ProfileTable.COLUMN_STIMULUS_CELL_NETWORK, ProfileTable.COLUMN_STIMULUS_CELL_NETWORK_ID, ProfileTable.COLUMN_STIMULUS_TIME_FROM, ProfileTable.COLUMN_STIMULUS_TIME_TO,
                        ProfileTable.COLUMN_STIMULUS_BATTERY_MIN, ProfileTable.COLUMN_STIMULUS_BATTERY_MAX, ProfileTable.COLUMN_RESPONSE_WIFI,
                        ProfileTable.COLUMN_RESPONSE_BLUETOOTH, ProfileTable.COLUMN_RESPONSE_AUTO_BRIGHTNESS, ProfileTable.COLUMN_RESPONSE_RING_VOLUME},
                null, null, null, null, null);
        if(cursor.moveToFirst()) {
            do {
                Profile profile = buildProfileFromCursor(cursor);
                if(profile != null)
                    profiles.add(profile);
            } while(cursor.moveToNext());
            if(!cursor.isClosed())
                cursor.close();
        }
        return profiles;
    }

    public Profile buildProfileFromCursor(Cursor cursor) {
        Profile profile = new Profile();
        profile.id = cursor.getInt(0);
        profile.name = cursor.getString(1);
        profile.active = cursor.getInt(2);
        profile.trigger.wiFi = cursor.getInt(3);
        profile.trigger.wifiId = cursor.getString(4);
        profile.trigger.cellNetwork = cursor.getInt(5);
        profile.trigger.cellNetworkId = cursor.getString(6);
        profile.trigger.fromTime = cursor.getString(7);
        profile.trigger.toTime = cursor.getString(8);
        profile.trigger.minCharge = cursor.getInt(9);
        profile.trigger.maxCharge = cursor.getInt(10);
        profile.action.wiFi = cursor.getInt(11);
        profile.action.bluetooth = cursor.getInt(12);
        profile.action.autoBrightness = cursor.getInt(13);
        profile.action.ringingVolume = cursor.getInt(14);
        return profile;
    }
}
