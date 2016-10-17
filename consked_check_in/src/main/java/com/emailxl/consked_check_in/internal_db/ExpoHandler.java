package com.emailxl.consked_check_in.internal_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.emailxl.consked_check_in.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling the expo table
 * <p/>
 * This class is a wrapper for the expo table part of the Content Provider
 *
 * @author ECG
 */

public class ExpoHandler {

    private Context context;
    private Uri uri;

    /**
     * Method to initialize the expo handler
     *
     * @param context The caller's context.
     */
    public ExpoHandler(Context context) {
        this.context = context;
        this.uri = new Uri.Builder()
                .scheme(AppConstants.SCHEME)
                .authority(AppConstants.AUTHORITY)
                .path(ConSkedCheckInProvider.EXPO_TABLE)
                .build();
    }

    /**
     * Method to add an expo to the expo table
     *
     * @param expo The expo object.
     * @param log equals 1 if change should be logged, 0 otherwise.
     * @return The URI for the newly inserted item.
     */
    public long addExpo(ExpoInt expo, int log) {

        ContentValues values = new ContentValues();

        values.put(ConSkedCheckInProvider.EXPOIDEXT, expo.getExpoIdExt());
        values.put(ConSkedCheckInProvider.STARTTIME, expo.getStartTime());
        values.put(ConSkedCheckInProvider.STOPTIME, expo.getStopTime());
        values.put(ConSkedCheckInProvider.TITLE, expo.getTitle());

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
            changeLog.setTableName("expo");
            changeLog.setJson(null);
            changeLog.setIdInt((int) lastPathSegment);
            changeLog.setIdExt(0);
            changeLog.setDone(0);

            dbc.addChangeLog(changeLog);
        }

        return lastPathSegment;
    }

    /**
     * Method to retrieve all expos
     *
     * @return The list of all expo objects.
     */
    public List<ExpoInt> getAllExpos() {

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);

        List<ExpoInt> expoList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ExpoInt expo = new ExpoInt(
                            cursor.getInt(0),       // idInt
                            cursor.getInt(1),       // expoIdExt
                            cursor.getString(2),    // startTime
                            cursor.getString(3),    // stopTime
                            cursor.getString(4));   // title

                    expoList.add(expo);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        return expoList;
    }

    /**
     * Method to retrieve an expo with a specific external id
     *
     * @param expoIdExt The id of the expo to be retrieved.
     * @return The expo object for the specified id.
     */
    public ExpoInt getExpoIdExt(int expoIdExt) {

        String selection = ConSkedCheckInProvider.EXPOIDEXT + " = ?";
        String[] selectionArgs = {Integer.toString(expoIdExt)};

        Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, null);

        ExpoInt expo = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                expo = new ExpoInt(
                        cursor.getInt(0),       // idInt
                        cursor.getInt(1),       // expoIdExt
                        cursor.getString(2),    // startTime
                        cursor.getString(3),    // stopTime
                        cursor.getString(4));   // title
            }

            cursor.close();
        }
        return expo;
    }

    /**
     * Method for deleting all expos
     *
     * @return The number of rows deleted.
     */
    public int deleteExpoAll() {

        return context.getContentResolver().delete(uri, null, null);
    }
}
