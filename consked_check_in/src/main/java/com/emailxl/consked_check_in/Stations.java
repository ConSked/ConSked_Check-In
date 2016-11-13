package com.emailxl.consked_check_in;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.emailxl.consked_check_in.internal_db.ExpoHandler;
import com.emailxl.consked_check_in.internal_db.ExpoInt;
import com.emailxl.consked_check_in.internal_db.StationJobHandler;
import com.emailxl.consked_check_in.internal_db.StationJobInt;
import com.emailxl.consked_check_in.internal_db.WorkerHandler;
import com.emailxl.consked_check_in.internal_db.WorkerInt;
import com.emailxl.consked_check_in.utils.AppConstants;
import com.emailxl.consked_check_in.utils.StationJobAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.emailxl.consked_check_in.utils.Utils.createSyncAccount;
import static com.emailxl.consked_check_in.utils.Utils.toastError;

public class Stations extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String ACTION_FINISHED_SYNC = "com.emailxl.consked_check_in.ACTION_FINISHED_SYNC";
    private static final String TAG = "Stations";
    private static final boolean LOG = AppConstants.LOG_MAIN;

    private SharedPreferences prefs;
    private int expoIdActive;
    private TextView tvExpoName;
    private ExpoHandler dbe;
    private WorkerHandler dbw;
    private StationJobHandler dbs;
    private List<StationJobInt> stationJobInts;
    private List<String> dateList;
    private StationJobAdapter stationJobAdapter;
    private ArrayAdapter<String> dateAdapter;
    private Account account;
    private static IntentFilter syncIntentFilter = new IntentFilter(ACTION_FINISHED_SYNC);
    private ProgressDialog pd;
    private BroadcastReceiver stationsBroadcastReceiver = null;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        if (LOG) Log.i(TAG, "Create page");

        dbe = new ExpoHandler(this);
        List<ExpoInt> expoInts = dbe.getAllExpos();

        prefs = this.getSharedPreferences(AppConstants.PREFERENCES, Context.MODE_PRIVATE);
        int expoIdActive = prefs.getInt("expoIdExt", 0);

        if (expoIdActive == 0) {
            expoIdActive = expoInts.get(0).getExpoIdExt();
        }

        dbw = new WorkerHandler(this);

        tvExpoName = (TextView) findViewById(R.id.expoName);

        tvExpoName.setText(dbe.getExpoIdExt(expoIdActive).getTitle());
        tvExpoName.setTextColor(Color.BLACK);

        dbs = new StationJobHandler(this);

        if (expoInts != null && expoInts.size() != 0) {

            List<Integer> expoList = new ArrayList<>();

            for (ExpoInt expoInt : expoInts) {
                int expoIdExt = expoInt.getExpoIdExt();

                expoList.add(expoIdExt);
            }

            stationJobInts = dbs.getStationJobExpoIdExt(expoIdActive);

            dateList = createDateList(stationJobInts);

            ArrayAdapter<Integer> expoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expoList);
            expoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateList);
            dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            stationJobAdapter = new StationJobAdapter(this, R.layout.stationjob_item, stationJobInts);

            Spinner spExpos = (Spinner) findViewById(R.id.spinner_expo);
            spExpos.setAdapter(expoAdapter);
            spExpos.setOnItemSelectedListener(this);
            spExpos.setSelection(expoList.indexOf(expoIdActive));

            Spinner spDates = (Spinner) findViewById(R.id.spinner_date);
            spDates.setAdapter(dateAdapter);
            spDates.setOnItemSelectedListener(this);

            ListView lvStationJob = (ListView) findViewById(R.id.stationJobList);
            lvStationJob.setAdapter(stationJobAdapter);
            lvStationJob.setEmptyView(findViewById(R.id.stationJobList_empty));
            lvStationJob.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            // Check network connectivity
                            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                            if (networkInfo != null && networkInfo.isConnected()) {

                                account = createSyncAccount(Stations.this);

                                int expoIdExt = stationJobInts.get(i).getExpoIdExt();
                                int stationIdExt = stationJobInts.get(i).getStationIdExt();

                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putInt("expoIdExt", expoIdExt);
                                editor.putInt("stationIdExt", stationIdExt);
                                editor.apply();

                                Bundle extras = new Bundle();
                                extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                                extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                                extras.putString("action", "readShift");
                                extras.putInt("expoIdExt", expoIdExt);
                                extras.putInt("stationIdExt", stationIdExt);

                                ContentResolver.requestSync(account, AppConstants.AUTHORITY, extras);
                                pd = ProgressDialog.show(Stations.this, "", getString(R.string.loading), true);

                            } else {
                                toastError(Stations.this, R.string.error_network);
                            }
                        }
                    }
            );
        }

        stationsBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                WorkerInt user = dbw.getUser();

                if ("CREWMEMBER".equals(user.getAuthrole())) {
                    Intent inCrew = new Intent(context, Crew.class);
                    startActivity(inCrew);
                    finish();
                } else {
                    Intent inWorkers = new Intent(context, Workers.class);
                    startActivity(inWorkers);
                    finish();
                }

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(stationsBroadcastReceiver, syncIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (stationsBroadcastReceiver != null) {
            unregisterReceiver(stationsBroadcastReceiver);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String date = "";

        int id = adapterView.getId();
        switch (id) {
            case R.id.spinner_expo:
                expoIdActive =  (int) adapterView.getItemAtPosition(i);
                break;
        }

        ExpoInt expoInt = dbe.getExpoIdExt(expoIdActive);

        tvExpoName.setText(expoInt.getTitle());
        tvExpoName.setTextColor(Color.BLACK);

        List<StationJobInt> stationJobIntList = dbs.getStationJobExpoIdExt(expoIdActive);

        List<String> tmpDateList = createDateList(stationJobIntList);

        dateList.clear();
        dateList.addAll(tmpDateList);
        dateAdapter.notifyDataSetChanged();

        switch (id) {
            case R.id.spinner_expo:
                date = dateList.get(0);
                break;

            case R.id.spinner_date:
                date = adapterView.getItemAtPosition(i).toString();
                break;
        }

        List<StationJobInt> tmpStationJobInts = new ArrayList<>();

        for (StationJobInt tmp : stationJobIntList) {
            String tmpStartTime = tmp.getStartTime();
            String tmpDate = tmpStartTime.split(" ")[0];

            if (tmpDate.equals(date)) {
                tmpStationJobInts.add(tmp);
            }
        }

        stationJobInts.clear();
        stationJobInts.addAll(tmpStationJobInts);
        stationJobAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private List<String> createDateList(List<StationJobInt> stationJobIntList) {

        List<String> tmpDateList = new ArrayList<>();

        for (StationJobInt tmp : stationJobIntList) {
            String startTime = tmp.getStartTime();
            String tmpDate = startTime.split(" ")[0];

            if (!tmpDateList.contains(tmpDate)) {
                tmpDateList.add(tmpDate);
            }
        }

        return tmpDateList;
    }
}
