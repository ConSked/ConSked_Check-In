package com.emailxl.consked_check_in;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.emailxl.consked_check_in.internal_db.ConSkedCheckInProvider;
import com.emailxl.consked_check_in.internal_db.ExpoHandler;
import com.emailxl.consked_check_in.internal_db.ExpoInt;
import com.emailxl.consked_check_in.internal_db.ShiftAssignmentHandler;
import com.emailxl.consked_check_in.internal_db.ShiftAssignmentInt;
import com.emailxl.consked_check_in.internal_db.ShiftCheckin;
import com.emailxl.consked_check_in.internal_db.ShiftStatusHandler;
import com.emailxl.consked_check_in.internal_db.ShiftStatusInt;
import com.emailxl.consked_check_in.internal_db.StationJobHandler;
import com.emailxl.consked_check_in.internal_db.StationJobInt;
import com.emailxl.consked_check_in.internal_db.TableObserver;
import com.emailxl.consked_check_in.internal_db.WorkerHandler;
import com.emailxl.consked_check_in.internal_db.WorkerInt;
import com.emailxl.consked_check_in.utils.AppConstants;
import com.emailxl.consked_check_in.utils.ShiftCheckinAdapter;
import com.emailxl.consked_check_in.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Workers extends AppCompatActivity {
    public static final String ACTION_FINISHED_SYNC = "com.emailxl.consked_check_in.ACTION_FINISHED_SYNC";
    private static final String TAG = "Workers";
    private static final boolean LOG = false;
    SharedPreferences prefs;
    private int expoIdExt, stationIdExt;
    private ShiftCheckinAdapter shiftCheckinAdapter;
    private TableObserver observer;
    private ContentResolver resolver;
    private static IntentFilter syncIntentFilter = new IntentFilter(ACTION_FINISHED_SYNC);
    private BroadcastReceiver workersReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);
        if (LOG) Log.i(TAG, "Create page");

        // get expoIdExt and stationIdExt from preferences
        prefs = this.getSharedPreferences(AppConstants.PREFERENCES, Context.MODE_PRIVATE);
        expoIdExt = prefs.getInt("expoIdExt", 0);
        stationIdExt = prefs.getInt("stationIdExt", 0);

        // Output the expo title
        ExpoHandler dbe = new ExpoHandler(this);
        ExpoInt expoInt = dbe.getExpoIdExt(expoIdExt);

        TextView tvExpo = (TextView) findViewById(R.id.expo);
        tvExpo.setText(expoInt.getTitle());

        // Output the station title and location
        StationJobHandler dbs = new StationJobHandler(this);
        StationJobInt stationJobInt = dbs.getStationJobIdExt(stationIdExt);

        final String shift = stationJobInt.getStationTitle() + " (" + stationJobInt.getLocation() + ")";

        TextView tvShift = (TextView) findViewById(R.id.shift);
        tvShift.setText(shift);

        // output the date and time
        String startTime = stationJobInt.getStartTime();
        String stopTime = stationJobInt.getStopTime();
        String time = Utils.getDate(startTime) + "  " + Utils.getTime(startTime, stopTime);

        TextView tvTime = (TextView) findViewById(R.id.time);
        tvTime.setText(time);

        // output the shiftCheckin list
        List<ShiftCheckin> shiftCheckins = fillShiftCheckin(expoIdExt, stationIdExt);

        List<ShiftCheckin> shiftCheckinList = new ArrayList<>();
        shiftCheckinList.addAll(shiftCheckins);

        shiftCheckinAdapter = new ShiftCheckinAdapter(this, R.layout.shiftassignment_item, shiftCheckinList);

        ListView lvShiftAssignment = (ListView) findViewById(R.id.workerList);
        lvShiftAssignment.setAdapter(shiftCheckinAdapter);
        lvShiftAssignment.setEmptyView(findViewById(R.id.workerList_empty));

        workersReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                List<ShiftCheckin> shiftCheckins = fillShiftCheckin(expoIdExt, stationIdExt);

                shiftCheckinAdapter.clear();
                shiftCheckinAdapter.addAll(shiftCheckins);
                shiftCheckinAdapter.notifyDataSetChanged();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        observer = new TableObserver(new Handler(), this);
        resolver = getContentResolver();
        Uri uri = new Uri.Builder()
                .scheme(AppConstants.SCHEME)
                .authority(AppConstants.AUTHORITY)
                .path(ConSkedCheckInProvider.CHANGELOG_TABLE)
                .build();
        resolver.registerContentObserver(uri, true, observer);

        registerReceiver(workersReceiver, syncIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        resolver.unregisterContentObserver(observer);

        if (workersReceiver != null) {
            unregisterReceiver(workersReceiver);
        }
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

    private List<ShiftCheckin> fillShiftCheckin(int expoIdExt, int stationIdExt) {

        ShiftAssignmentHandler dba = new ShiftAssignmentHandler(this);
        ShiftStatusHandler dbs = new ShiftStatusHandler(this);
        WorkerHandler dbw = new WorkerHandler(this);

        List<ShiftAssignmentInt> shiftAssignments = dba.getShiftAssignmentIdExt(expoIdExt, stationIdExt);
        List<ShiftCheckin> shiftCheckinList = new ArrayList<>();

        if (shiftAssignments != null && shiftAssignments.size() != 0) {
            for (ShiftAssignmentInt shiftAssignment : shiftAssignments) {

                // Get shiftAssignment
                ShiftCheckin shiftCheckin = new ShiftCheckin();
                shiftCheckin.setShiftAssignment(shiftAssignment);

                int workerIdExt = shiftAssignment.getWorkerIdExt();

                // Get name
                WorkerInt workerInt = dbw.getWorkerIdExt(workerIdExt);
                String name = workerInt.getFirstName() + " " + workerInt.getLastName();
                shiftCheckin.setName(name);

                // Get statusType
                String statusType = "CHECK_IN";
                List<ShiftStatusInt> shiftStatusInts = dbs.getShiftStatusIdExt(expoIdExt, stationIdExt, workerIdExt);
                if (shiftStatusInts != null && shiftStatusInts.size() != 0) {
                    statusType = shiftStatusInts.get(0).getStatusType();
                }
                shiftCheckin.setStatusType(statusType);

                shiftCheckinList.add(shiftCheckin);
            }
        }

        return shiftCheckinList;
    }
}
