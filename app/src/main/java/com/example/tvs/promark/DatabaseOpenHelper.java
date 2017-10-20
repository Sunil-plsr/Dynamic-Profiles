package com.example.tvs.promark;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "profile.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ProfileTable.onCreate(db);

        Profile profile = new Profile("Default", 1,
                new Profile.Trigger(0, 0, null, null, null, null, 0,  100), new Profile.Action(0, 0, 0, 4));
        new ProfileDAO(db).save(profile);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        ProfileTable.onUpgrade(db, oldVersion, newVersion);
    }
}
