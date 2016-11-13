package com.emailxl.consked_check_in;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.emailxl.consked_check_in.internal_db.ExpoHandler;
import com.emailxl.consked_check_in.internal_db.ExpoInt;
import com.emailxl.consked_check_in.internal_db.WorkerHandler;
import com.emailxl.consked_check_in.internal_db.WorkerInt;
import com.emailxl.consked_check_in.utils.AppConstants;

public class Crew extends AppCompatActivity {
    private static final String TAG = "Crew";
    private static final boolean LOG = AppConstants.LOG_MAIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crew);
        if (LOG) Log.i(TAG, "Create page");

        // get User
        WorkerHandler dbw = new WorkerHandler(this);
        WorkerInt user = dbw.getUser();

        // get Expo
        SharedPreferences prefs = this.getSharedPreferences(AppConstants.PREFERENCES, Context.MODE_PRIVATE);
        int expoIdExt = prefs.getInt("expoIdExt", 0);

        ExpoHandler dbe = new ExpoHandler(this);
        ExpoInt expo = dbe.getExpoIdExt(expoIdExt);

        String lineSep = System.getProperty("line.separator");
        String name = user.getFirstName() + " " + user.getLastName();
        String expoName = expo.getTitle();
        String notice = name + lineSep +
                "is not an Organizer or Supervisor for expo" + lineSep +
                expoName;

        TextView tvCrew = (TextView) findViewById(R.id.crew);
        tvCrew.setText(notice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.workers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.action_station:
                Intent inStations = new Intent(this, Stations.class);
                startActivity(inStations);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
