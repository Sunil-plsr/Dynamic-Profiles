package com.example.tvs.promark;

import android.content.Context;

import java.util.ArrayList;

public class DatabaseManager {

    Context context;
    DatabaseOpenHelper databaseOpenHelper;
    ProfileDAO profileDAO;

    public DatabaseManager(Context context) {
        this.context = context;
        databaseOpenHelper = new DatabaseOpenHelper(context);
        profileDAO = new ProfileDAO(databaseOpenHelper.getWritableDatabase());
    }

    public long saveProfile(Profile profile) { return this.profileDAO.save(profile); }
    public int updateProfile(Profile profile) { return this.profileDAO.update(profile); }
    public int deleteProfile(long id) { return this.profileDAO.delete(id); }
    public ArrayList<Profile> getActiveProfile() { return  this.profileDAO.getActive(); }
    public int activateProfile(long id) { return  this.profileDAO.activate(id); }
    public int deactivateProfile(long id) { return  this.profileDAO.deactivate(id); }
    public ArrayList<Profile> getAllProfiles() { return  this.profileDAO.getAll(); }
}
