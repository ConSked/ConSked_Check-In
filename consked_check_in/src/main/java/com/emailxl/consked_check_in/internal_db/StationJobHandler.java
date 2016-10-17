package com.emailxl.consked_check_in.internal_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.emailxl.consked_check_in.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling the station table
 * <p/>
 * This class is a wrapper for the station table part of the Content Provider
 *
 * @author ECG
 */

public class StationJobHandler {
    private Context context;
    private Uri uri;

    /**
     * Method to initialize the station handler
     *
     * @param context The caller's context.
     */
    public StationJobHandler(Context context) {
        this.context = context;
        this.uri = new Uri.Builder()
                .scheme(AppConstants.SCHEME)
                .authority(AppConstants.AUTHORITY)
                .path(ConSkedCheckInProvider.STATIONJOB_TABLE)
                .build();
    }

    /**
     * Method to add an station to the station table
     *
     * @param stationJob The station object.
     * @param log equals 1 if change should be logged, 0 otherwise
     * @return The URI for the newly inserted item.
     */
    public long addStationJob(StationJobInt stationJob, int log) {

        ContentValues values = new ContentValues();

        values.put(ConSkedCheckInProvider.STATIONIDEXT, stationJob.getStationIdExt());
        values.put(ConSkedCheckInProvider.EXPOIDEXT, stationJob.getExpoIdExt());
        values.put(ConSkedCheckInProvider.STARTTIME, stationJob.getStartTime());
        values.put(ConSkedCheckInProvider.STOPTIME, stationJob.getStopTime());
        values.put(ConSkedCheckInProvider.STATIONTITLE, stationJob.getStationTitle());
        values.put(ConSkedCheckInProvider.LOCATION, stationJob.getStationTitle());

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
            changeLog.setTableName("stationjob");
            changeLog.setJson(null);
            changeLog.setIdInt((int) lastPathSegment);
            changeLog.setIdExt(0);
            changeLog.setDone(0);

            dbc.addChangeLog(changeLog);
        }

        return lastPathSegment;
    }

    /**
     * Method to retrieve a station with a specific external id
     *
     * @param stationIdExt The id of the station to be retrieved.
     * @return The station object for the specified id.
     */
    public StationJobInt getStationJobIdExt(int stationIdExt) {

        String selection = ConSkedCheckInProvider.STATIONIDEXT + " = ?";
        String[] selectionArgs = {Integer.toString(stationIdExt)};

        Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, null);

        StationJobInt stationJob = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                stationJob = new StationJobInt(
                        cursor.getInt(0),       // idInt
                        cursor.getInt(1),       // stationIdExt
                        cursor.getInt(2),       // expoIdExt
                        cursor.getString(3),    // startTime
                        cursor.getString(4),    // stopTime
                        cursor.getString(5),    // stationTitle
                        cursor.getString(6));   // location
            }

            cursor.close();
        }
        return stationJob;
    }

    /**
     * Method to retrieve the stations for a specific expo
     *
     * @param expoIdExt The id of the expo to be retrieved.
     * @return The station object for the specified id.
     */
    public List<StationJobInt> getStationJobExpoIdExt(int expoIdExt) {

        String selection = ConSkedCheckInProvider.EXPOIDEXT + " = ?";
        String[] selectionArgs = {Integer.toString(expoIdExt)};
        String sortOrder = ConSkedCheckInProvider.STARTTIME + "," +
                ConSkedCheckInProvider.STATIONTITLE + " ASC";

        Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, sortOrder);

        List<StationJobInt> stationJobList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    StationJobInt stationJob = new StationJobInt(
                            cursor.getInt(0),       // idInt
                            cursor.getInt(1),       // stationIdExt
                            cursor.getInt(2),       // expoIdExt
                            cursor.getString(3),    // startTime
                            cursor.getString(4),    // stopTime
                            cursor.getString(5),    // stationTitle
                            cursor.getString(6));   // location

                    stationJobList.add(stationJob);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        return stationJobList;
    }

    /**
     * Method for deleting all stations
     *
     * @return The number of stations deleted.
     */
    public int deleteStationJobAll() {

        return context.getContentResolver().delete(uri, null, null);
    }
}
