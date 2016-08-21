package com.emailxl.consked_check_in;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.emailxl.consked_check_in.internal_db.ExpoHandler;
import com.emailxl.consked_check_in.internal_db.ExpoInt;
import com.emailxl.consked_check_in.internal_db.ShiftAssignmentHandler;
import com.emailxl.consked_check_in.internal_db.ShiftAssignmentInt;
import com.emailxl.consked_check_in.internal_db.StationJobHandler;
import com.emailxl.consked_check_in.internal_db.StationJobInt;
import com.emailxl.consked_check_in.utils.AppConstants;
import com.emailxl.consked_check_in.utils.ShiftAssignmentAdapter;
import com.emailxl.consked_check_in.utils.Utils;

import java.util.List;

public class Workers extends AppCompatActivity {
    private static final String TAG = "Workers";
    private static boolean LOG = false;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);
        if (LOG) Log.i(TAG, "Create page");

        // get expoIdExt and stationIdExt from preferences
        prefs = this.getSharedPreferences(AppConstants.PREFERENCES, Context.MODE_PRIVATE);
        int expoIdExt = prefs.getInt("expoIdExt", 0);
        int stationIdExt = prefs.getInt("stationIdExt", 0);

        // Output the expo title
        ExpoHandler dbe = new ExpoHandler(this);
        ExpoInt expoInt = dbe.getExpoIdExt(expoIdExt);

        TextView tvExpo = (TextView) findViewById(R.id.expo);
        tvExpo.setText(expoInt.getTitle());

        // Output the station title and location
        StationJobHandler dbs = new StationJobHandler(this);
        StationJobInt stationJobInt = dbs.getStationJobIdExt(stationIdExt);

        String shift = stationJobInt.getStationTitle() + " (" + stationJobInt.getLocation() + ")";

        TextView tvShift = (TextView) findViewById(R.id.shift);
        tvShift.setText(shift);

        // output the date and time
        String startTime = stationJobInt.getStartTime();
        String stopTime = stationJobInt.getStopTime();
        String time = Utils.getDate(startTime) + "  " + Utils.getTime(startTime, stopTime);

        TextView tvTime = (TextView) findViewById(R.id.time);
        tvTime.setText(time);

        // output the worker list
        ShiftAssignmentHandler dba = new ShiftAssignmentHandler(this);
        List<ShiftAssignmentInt> shiftAssignmentInts = dba.getShiftAssignmentIdExt(expoIdExt, stationIdExt);

        ShiftAssignmentAdapter shiftAssignmentAdapter = new ShiftAssignmentAdapter(this, R.layout.shiftassignment_item, shiftAssignmentInts);

        ListView lvStationJob = (ListView) findViewById(R.id.workerList);
        lvStationJob.setAdapter(shiftAssignmentAdapter);
        lvStationJob.setEmptyView(findViewById(R.id.workerList_empty));
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
