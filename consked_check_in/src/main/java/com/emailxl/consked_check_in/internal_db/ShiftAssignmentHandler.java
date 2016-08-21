package com.emailxl.consked_check_in.internal_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.emailxl.consked_check_in.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ECG
 */

public class ShiftAssignmentHandler {
    private Context context;
    private Uri uri;

    /**
     * Method to initialize the expo handler
     *
     * @param context The caller's context.
     */
    public ShiftAssignmentHandler(Context context) {
        this.context = context;
        this.uri = new Uri.Builder()
                .scheme(AppConstants.SCHEME)
                .authority(AppConstants.AUTHORITY)
                .path(ConSkedCheckInProvider.SHIFTASSIGNMENT_TABLE)
                .build();
    }

    /**
     * Method to add a shiftassignment to the shiftassignment table
     *
     * @param shiftassignment The shiftassignment object.
     * @return The URI for the newly inserted item.
     */
    public long addShiftAssignment(ShiftAssignmentInt shiftassignment) {

        ContentValues values = new ContentValues();

        values.put(ConSkedCheckInProvider.WORKERIDEXT, shiftassignment.getWorkerIdExt());
        values.put(ConSkedCheckInProvider.JOBIDEXT, shiftassignment.getJobIdExt());
        values.put(ConSkedCheckInProvider.STATIONIDEXT, shiftassignment.getStationIdExt());
        values.put(ConSkedCheckInProvider.EXPOIDEXT, shiftassignment.getExpoIdExt());

        Uri newuri = context.getContentResolver().insert(uri, values);

        long lastPathSegment = 0;
        if (newuri != null) {
            lastPathSegment = Long.parseLong(newuri.getLastPathSegment());
        }

        return lastPathSegment;
    }

    /**
     * Method to retrieve a shiftassignment for a specific expo and station
     *
     * @param expoIdExt The id of the shift expo.
     * @param stationIdExt The id of the shift station.
     * @return The shiftassignment object for the specified id.
     */
    public List<ShiftAssignmentInt> getShiftAssignmentIdExt(int expoIdExt, int stationIdExt) {

        String selection = ConSkedCheckInProvider.EXPOIDEXT + " = ? AND " +
                ConSkedCheckInProvider.STATIONIDEXT + " = ?";
        String[] selectionArgs = {Integer.toString(expoIdExt),
                Integer.toString(stationIdExt)};

        Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, null);

        List<ShiftAssignmentInt> shiftassignmentList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ShiftAssignmentInt shiftassignment = new ShiftAssignmentInt(
                            cursor.getInt(0),       // idInt
                            cursor.getInt(1),       // workerIdExt
                            cursor.getInt(2),       // jobIdext
                            cursor.getInt(3),       // stationIdExt
                            cursor.getInt(4));      // expoIdExt

                    shiftassignmentList.add(shiftassignment);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        return shiftassignmentList;
    }

    /**
     * Method for deleting all shiftassignments
     *
     * @return The number of rows deleted.
     */
    public int deleteShiftAssignmentAll() {

        return context.getContentResolver().delete(uri, null, null);
    }
}
