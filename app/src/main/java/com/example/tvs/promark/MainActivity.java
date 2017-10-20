package com.example.tvs.promark;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static String PROFILE_FRAGMENT = "profile_fragment";
    public static DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseManager = new DatabaseManager(getApplicationContext());

        getFragmentManager().beginTransaction()
                .add(R.id.activity_main, new ProfileList(), PROFILE_FRAGMENT)
                .commit();

        new SchedulerSetup(getApplicationContext()).execute();
    }

    public static class SchedulerSetup extends AsyncTask<Void, Void, Void> {

        Context context;
        PendingIntent pendingIntent;

        public SchedulerSetup(Context context) {
            this.context = context;
            pendingIntent = PendingIntent.getBroadcast(this.context, 0, new Intent(this.context, Scheduler.class), 0);
        }

        @Override
        protected Void doInBackground(Void... params) {
            AlarmManager manager = (AlarmManager) this.context.getSystemService(Context.ALARM_SERVICE);
            manager.setRepeating(AlarmManager.RTC,System.currentTimeMillis(), (long)(1000*60*1),pendingIntent);
            return null;
        }
    }
}
