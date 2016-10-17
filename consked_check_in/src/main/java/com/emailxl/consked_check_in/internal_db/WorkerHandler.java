package com.emailxl.consked_check_in.internal_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.emailxl.consked_check_in.utils.AppConstants;

/**
 * Class for handling the worker table
 * <p/>
 * This class is a wrapper for the worker table part of the Content Provider
 *
 * @author ECG
 */
public class WorkerHandler {
    private Context context;
    private Uri uri;

    /**
     * Method to initialize the worker handler
     *
     * @param context The caller's context.
     */
    public WorkerHandler(Context context) {
        this.context = context;
        this.uri = new Uri.Builder()
                .scheme(AppConstants.SCHEME)
                .authority(AppConstants.AUTHORITY)
                .path(ConSkedCheckInProvider.WORKER_TABLE)
                .build();
    }

    /**
     * Method to add an worker to the worker table
     *
     * @param worker The worker object.
     * @param log equals 1 if change should be logged, 0 otherwise
     * @return The URI for the newly inserted item.
     */
    public long addWorker(WorkerInt worker, int log) {

        ContentValues values = new ContentValues();

        values.put(ConSkedCheckInProvider.WORKERIDEXT, worker.getWorkerIdExt());
        values.put(ConSkedCheckInProvider.FIRSTNAME, worker.getFirstName());
        values.put(ConSkedCheckInProvider.LASTNAME, worker.getLastName());
        values.put(ConSkedCheckInProvider.AUTHROLE, worker.getAuthrole());

        Uri newuri = context.getContentResolver().insert(uri, values);

        long lastPathSegment = 0;
        if (newuri != null) {
            lastPathSegment = Long.parseLong(newuri.getLastPathSegment());
        }

        if (log == 1) {
            ChangeLogHandler dbc = new ChangeLogHandler(context);
            ChangeLogInt changeLog = new ChangeLogInt();

            changeLog.setTimestamp(System.currentTimeMillis());
            changeLog.setSource("local");
            changeLog.setOperation("create");
            changeLog.setTableName("worker");
            changeLog.setJson(null);
            changeLog.setIdInt((int) lastPathSegment);
            changeLog.setIdExt(0);
            changeLog.setDone(0);

            dbc.addChangeLog(changeLog);
        }

        return lastPathSegment;
    }

    /**
     * Method to retrieve a worker with a specific external id
     *
     * @return The worker object for the specified id.
     */
    public WorkerInt getWorkerIdExt(int workerIdExt) {

        String selection = ConSkedCheckInProvider.WORKERIDEXT + " = ?";
        String[] selectionArgs = {Integer.toString(workerIdExt)};

        Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, null);

        WorkerInt worker = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                worker = new WorkerInt(
                        cursor.getInt(0),       // idInt
                        cursor.getInt(1),       // workerIdExt
                        cursor.getString(2),    // firstName
                        cursor.getString(3),    // lastName
                        cursor.getString(4));   // authrole
            }

            cursor.close();
        }
        return worker;
    }

    /**
     * Method for deleting all workers
     *
     * @return The number of rows deleted.
     */
    public int deleteWorkerAll() {

        return context.getContentResolver().delete(uri, null, null);
    }
}
