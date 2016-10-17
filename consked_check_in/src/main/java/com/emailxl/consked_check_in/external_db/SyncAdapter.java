package com.emailxl.consked_check_in.external_db;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.emailxl.consked_check_in.Loading;
import com.emailxl.consked_check_in.Stations;
import com.emailxl.consked_check_in.Workers;
import com.emailxl.consked_check_in.internal_db.ChangeLogHandler;
import com.emailxl.consked_check_in.internal_db.ChangeLogInt;
import com.emailxl.consked_check_in.internal_db.ExpoHandler;
import com.emailxl.consked_check_in.internal_db.ExpoInt;
import com.emailxl.consked_check_in.internal_db.ShiftAssignmentHandler;
import com.emailxl.consked_check_in.internal_db.ShiftAssignmentInt;
import com.emailxl.consked_check_in.internal_db.ShiftStatusHandler;
import com.emailxl.consked_check_in.internal_db.ShiftStatusInt;
import com.emailxl.consked_check_in.internal_db.StationJobHandler;
import com.emailxl.consked_check_in.internal_db.StationJobInt;
import com.emailxl.consked_check_in.internal_db.WorkerHandler;
import com.emailxl.consked_check_in.internal_db.WorkerInt;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.emailxl.consked_check_in.external_db.ExpoAPI.searchExpo;
import static com.emailxl.consked_check_in.external_db.ShiftAssignmentAPI.searchShiftAssignment;
import static com.emailxl.consked_check_in.external_db.ShiftStatusAPI.createShiftStatus;
import static com.emailxl.consked_check_in.external_db.ShiftStatusAPI.readShiftStatus;
import static com.emailxl.consked_check_in.external_db.StationJobAPI.searchStationJob;
import static com.emailxl.consked_check_in.external_db.WorkerAPI.readWorker;
import static com.emailxl.consked_check_in.external_db.WorkerAPI.searchWorker;

/**
 * handle the transfer of data between a server and an app.
 * using the Android sync adapter framework.
 *
 * @author ECG
 */
class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    private static final boolean LOG = true;

    private ContentResolver mContentResolver;
    private Context mContext;

    SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
        this.mContext = context;
    }

    // For compatibility with Android 3.0 and later platform versions.
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);

        mContentResolver = context.getContentResolver();
        this.mContext = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        if (LOG) Log.i(TAG, "Starting sync");

        String action = extras.containsKey("action") ? extras.getString("action") : "";
        String username = extras.containsKey("username") ? extras.getString("username") : "";
        int expoIdExt = extras.containsKey("expoIdExt") ? extras.getInt("expoIdExt") : 0;
        int stationIdExt = extras.containsKey("stationIdExt") ? extras.getInt("stationIdExt") : 0;

        if ("read".equals(action)) {

            syncReadExt(username);

        } else if ("readShift".equals(action)) {

            syncReadShiftExt(expoIdExt, stationIdExt);

        } else {
            ChangeLogHandler dbc = new ChangeLogHandler(mContext);
            List<ChangeLogInt> changeLogs = dbc.getActiveChangeLogs();

            if (changeLogs.size() > 0) {
                for (ChangeLogInt changeLog : changeLogs) {
                    if ("local".equals(changeLog.getSource())) {
                        if ("shiftstatus".equals(changeLog.getTableName())) {

                            syncShiftStatusExtLocal(changeLog, dbc);
                        }
                    } else if ("remote".equals(changeLog.getSource())) {
                        if ("shiftstatus".equals(changeLog.getTableName())) {

                            syncShiftStatusExtRemote(changeLog, dbc);
                        }
                    }
                }
            }
        }

        if (LOG) Log.i(TAG, "Ending sync");
    }

    private void syncReadExt(String username) {

        if (LOG) Log.i(TAG, "Read");

        WorkerHandler dbw = new WorkerHandler(mContext);
        dbw.deleteWorkerAll();

        ExpoHandler dbe = new ExpoHandler(mContext);
        dbe.deleteExpoAll();

        StationJobHandler dbs = new StationJobHandler(mContext);
        dbs.deleteStationJobAll();

        // get worker from the external database
        WorkerExt[] workerExts = searchWorker(username);

        // load worker into internal database
        if (workerExts != null && workerExts.length != 0) {
            for (WorkerExt workerExt : workerExts) {

                WorkerInt workerInt = new WorkerInt();
                workerInt.setWorkerIdExt(workerExt.getWorkerIdExt());
                workerInt.setFirstName(workerExt.getFirstName());
                workerInt.setLastName(workerExt.getLastName());
                workerInt.setAuthrole(workerExt.getAuthrole());
                dbw.addWorker(workerInt, 0);
            }

            // get expos from the external database
            ExpoExt[] expoExts = searchExpo(workerExts[0].getWorkerIdExt());

            // load expos into internal database
            if (expoExts != null && expoExts.length != 0) {
                for (ExpoExt expoExt : expoExts) {

                    // test if future expo
                    String strStopDate = expoExt.getStopTime().getDate();
                    Date stopDate = new Date();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                    try {
                        stopDate = sdf.parse(strStopDate);
                    } catch (ParseException e) {
                        System.out.println(e.getMessage());
                    }

                    if (stopDate.after(new Date())) {

                        int expoIdExt = expoExt.getExpoIdExt();

                        ExpoInt expoInt = new ExpoInt();
                        expoInt.setExpoIdExt(expoIdExt);
                        expoInt.setStartTime(expoExt.getStartTime().getDate());
                        expoInt.setStopTime(expoExt.getStopTime().getDate());
                        expoInt.setTitle(expoExt.getTitle());
                        dbe.addExpo(expoInt, 0);

                        // get stationJobs from the external database
                        StationJobExt[] stationJobExts = searchStationJob(expoIdExt);

                        // load stationJobs into internal database
                        if (stationJobExts != null && stationJobExts.length != 0) {
                            for (StationJobExt stationJobExt : stationJobExts) {

                                StationJobInt stationJobInt = new StationJobInt();
                                stationJobInt.setStationIdExt(stationJobExt.getStationIdExt());
                                stationJobInt.setExpoIdExt(stationJobExt.getExpoIdExt());
                                stationJobInt.setStartTime(stationJobExt.getStartTime().getDate());
                                stationJobInt.setStopTime(stationJobExt.getStopTime().getDate());
                                stationJobInt.setStationTitle(stationJobExt.getStationTitle());
                                dbs.addStationJob(stationJobInt, 0);
                            }
                        }
                    }
                }
            }
        }

        mContext.sendBroadcast(new Intent(Loading.ACTION_FINISHED_SYNC));
    }

    private void syncReadShiftExt(int expoIdExt, int stationIdExt) {

        if (LOG) Log.i(TAG, "ReadShift");

        ShiftAssignmentHandler dba = new ShiftAssignmentHandler(mContext);
        dba.deleteShiftAssignmentAll();

        WorkerHandler dbw = new WorkerHandler(mContext);
        dbw.deleteWorkerAll();

        ShiftStatusHandler dbs = new ShiftStatusHandler(mContext);
        dbs.deleteShiftStatusAll();

        // get shiftAssignments from the external database
        ShiftAssignmentExt[] shiftAssignmentExts = searchShiftAssignment(expoIdExt, stationIdExt);

        // load shiftAssignments into internal database
        if (shiftAssignmentExts != null && shiftAssignmentExts.length != 0) {
            for (ShiftAssignmentExt shiftAssignmentExt : shiftAssignmentExts) {

                ShiftAssignmentInt shiftAssignmentInt = new ShiftAssignmentInt();
                shiftAssignmentInt.setWorkerIdExt(shiftAssignmentExt.getWorkerIdExt());
                shiftAssignmentInt.setJobIdExt(shiftAssignmentExt.getJobIdExt());
                shiftAssignmentInt.setStationIdExt(shiftAssignmentExt.getStationIdExt());
                shiftAssignmentInt.setExpoIdExt(shiftAssignmentExt.getExpoIdExt());
                dba.addShiftAssignment(shiftAssignmentInt, 0);

                // get worker from the external database
                WorkerExt[] workerExts = readWorker(shiftAssignmentExt.getWorkerIdExt());

                // load worker into internal database
                if (workerExts != null && workerExts.length != 0) {
                    for (WorkerExt workerExt : workerExts) {

                        WorkerInt workerInt = new WorkerInt();
                        workerInt.setWorkerIdExt(workerExt.getWorkerIdExt());
                        workerInt.setFirstName(workerExt.getFirstName());
                        workerInt.setLastName(workerExt.getLastName());
                        workerInt.setAuthrole(workerExt.getAuthrole());
                        dbw.addWorker(workerInt, 0);

                        // get shiftStatus from the external database
                        ShiftStatusExt[] shiftStatusExts = readShiftStatus(expoIdExt, stationIdExt, workerExt.getWorkerIdExt());

                        // load shiftStatus into internal database
                        if (shiftStatusExts != null && shiftStatusExts.length != 0) {
                            for (ShiftStatusExt shiftstatusExt : shiftStatusExts) {

                                ShiftStatusInt shiftstatusInt = new ShiftStatusInt();
                                shiftstatusInt.setShiftstatusIdExt(shiftstatusExt.getShiftstatusIdExt());
                                shiftstatusInt.setWorkerIdExt(shiftstatusExt.getWorkerIdExt());
                                shiftstatusInt.setStationIdExt(shiftstatusExt.getStationIdExt());
                                shiftstatusInt.setExpoIdExt(shiftstatusExt.getExpoIdExt());
                                shiftstatusInt.setStatusType(shiftstatusExt.getStatusType());
                                shiftstatusInt.setStatusTime(shiftstatusExt.getStatusTime().getDate());
                                dbs.addShiftStatus(shiftstatusInt, 0);
                            }
                        }
                    }
                }
            }
        }

        mContext.sendBroadcast(new Intent(Stations.ACTION_FINISHED_SYNC));
    }

    private void syncShiftStatusExtLocal(ChangeLogInt changeLog, ChangeLogHandler dbc) {

        if (LOG) Log.i(TAG, "ShiftStatusExtLocal");
        ShiftStatusHandler dbs = new ShiftStatusHandler(mContext);
        ShiftStatusInt shiftStatusInt = dbs.getShiftStatusIdInt(changeLog.getIdInt());

        ShiftStatusExt params = new ShiftStatusExt();
        int output = 0;

        if ("create".equals(changeLog.getOperation())) {

            if (LOG) Log.i(TAG, "Create");
            params.setWorkerIdExt(shiftStatusInt.getWorkerIdExt());
            params.setStationIdExt(shiftStatusInt.getStationIdExt());
            params.setExpoIdExt(shiftStatusInt.getExpoIdExt());
            params.setStatusType(shiftStatusInt.getStatusType());

            Timestamp timestamp = new Timestamp();
            timestamp.setDate(shiftStatusInt.getStatusTime());
            timestamp.setTimezoneType(3);
            timestamp.setTimezone(TimeZone.getDefault().toString());

            params.setStatusTime(timestamp);
            output = createShiftStatus(params);

            shiftStatusInt.setShiftstatusIdExt(output);
            dbs.updateShiftStatusIdInt(shiftStatusInt, 0);
        }

        if (output != 0) {
            changeLog.setDone(1);
            dbc.updateChangeLogTimestamp(changeLog);
        } else {
            if (LOG) Log.e(TAG, "syncShiftStatusExtLocal Fail");
        }
    }

    private void syncShiftStatusExtRemote(ChangeLogInt changeLog, ChangeLogHandler dbc) {

        if (LOG) Log.i(TAG, "ShiftStatusExtRemote");

        ShiftStatusInt shiftstatus = new ShiftStatusInt();
        try {
            JSONObject json = new JSONObject(changeLog.getJson());

            shiftstatus.setShiftstatusIdExt(json.optInt("shiftstatusid"));
            shiftstatus.setWorkerIdExt(json.optInt("workerid"));
            shiftstatus.setStationIdExt(json.optInt("stationid"));
            shiftstatus.setExpoIdExt(json.optInt("expoid"));
            shiftstatus.setStatusType(json.optString("statusType"));

            JSONObject json_statusTime = new JSONObject(json.optString("statusTime"));

            shiftstatus.setStatusTime(json_statusTime.optString("date"));

        } catch (JSONException e) {
            if (LOG) Log.e(TAG, "JSON error");
        }

        String operation = changeLog.getOperation();
        ShiftStatusHandler dbs = new ShiftStatusHandler(mContext);
        if ("create".equals(operation)) {

            dbs.addShiftStatus(shiftstatus, 0);

        } else if ("update".equals(operation)) {

            dbs.updateShiftStatusIdExt(shiftstatus, 0);
        }

        changeLog.setDone(1);
        dbc.updateChangeLogTimestamp(changeLog);

        mContext.sendBroadcast(new Intent(Workers.ACTION_FINISHED_SYNC));
    }
}
