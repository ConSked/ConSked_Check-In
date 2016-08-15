package com.emailxl.consked_check_in;

import android.graphics.Color;
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
import com.emailxl.consked_check_in.utils.StationJobAdapter;

import java.util.ArrayList;
import java.util.List;

public class Stations extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "Stations";
    private static boolean LOG = false;
    private TextView tvExpoName;
    private Spinner spExpos, spDates;
    private ExpoHandler dbe;
    private StationJobHandler dbs;
    private List<StationJobInt> stationJobInts;
    private List<String> dateList;
    private StationJobAdapter stationJobAdapter;
    private ArrayAdapter<String> dateAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations);
        if (LOG) Log.i(TAG, "Create page");

        dbe = new ExpoHandler(this);
        List<ExpoInt> expoInts = dbe.getAllExpos();

        dbs = new StationJobHandler(this);

        if (expoInts != null && expoInts.size() != 0) {

            List<Integer> expoList = new ArrayList<>();

            for (ExpoInt expoInt : expoInts) {
                int expoIdExt = expoInt.getExpoIdExt();

                expoList.add(expoIdExt);
            }

            stationJobInts = dbs.getStationJobExpoIdExt(expoInts.get(0).getExpoIdExt());

            dateList = createDateList(stationJobInts);

            tvExpoName = (TextView) findViewById(R.id.expoName);

            ArrayAdapter<Integer> expoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expoList);
            expoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            dateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dateList);
            dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            stationJobAdapter = new StationJobAdapter(this, R.layout.stationjob_item, stationJobInts);

            spExpos = (Spinner) findViewById(R.id.spinner_expo);
            spExpos.setAdapter(expoAdapter);
            spExpos.setOnItemSelectedListener(this);

            spDates = (Spinner) findViewById(R.id.spinner_date);
            spDates.setAdapter(dateAdapter);
            spDates.setOnItemSelectedListener(this);

            ListView lvStationJob = (ListView) findViewById(R.id.stationJobList);
            lvStationJob.setAdapter(stationJobAdapter);
            lvStationJob.setEmptyView(findViewById(R.id.stationJobList_empty));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int expoIdActive = 1;
        String date = "";

        int id = adapterView.getId();
        switch (id) {
            case R.id.spinner_expo:
                expoIdActive =  (int) adapterView.getItemAtPosition(i);
                break;

            case R.id.spinner_date:
                expoIdActive = (int) spExpos.getSelectedItem();
                break;
        }

        ExpoInt expoInt = dbe.getExpoIdExt(expoIdActive);

        tvExpoName.setText(expoInt.getTitle());
        tvExpoName.setTextColor(Color.BLACK);

        List<StationJobInt> stationJobIntList = dbs.getStationJobExpoIdExt(expoInt.getExpoIdExt());

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
