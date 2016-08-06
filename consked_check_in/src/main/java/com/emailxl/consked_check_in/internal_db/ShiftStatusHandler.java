package com.emailxl.consked_check_in.internal_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.emailxl.consked_check_in.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling the shiftstatus table
 * <p/>
 * This class is a wrapper for the shiftstatus table part of the Content Provider
 *
 * @author ECG
 */

public class ShiftStatusHandler {
    private Context context;
    private Uri uri;

    /**
     * Method to initialize the expo handler
     *
     * @param context The caller's context.
     */
    public ShiftStatusHandler(Context context) {
        this.context = context;
        this.uri = new Uri.Builder()
                .scheme(AppConstants.SCHEME)
                .authority(AppConstants.AUTHORITY)
                .path(ConSkedCheckInProvider.SHIFTSTATUS_TABLE)
                .build();
    }

    /**
     * Method to add a shiftstatus to the shiftstatus table
     *
     * @param shiftstatus The shiftstatus object.
     * @return The URI for the newly inserted item.
     */
    public long addShiftStatus(ShiftStatusInt shiftstatus) {

        ContentValues values = new ContentValues();

        values.put(ConSkedCheckInProvider.SHIFTSTATUSIDEXT, shiftstatus.getShiftstatusIdExt());
        values.put(ConSkedCheckInProvider.WORKERIDEXT, shiftstatus.getWorkerIdExt());
        values.put(ConSkedCheckInProvider.STATIONIDEXT, shiftstatus.getStationIdExt());
        values.put(ConSkedCheckInProvider.EXPOIDEXT, shiftstatus.getExpoIdExt());
        values.put(ConSkedCheckInProvider.STATUSTYPE, shiftstatus.getStatusType());
        values.put(ConSkedCheckInProvider.STATUSTIME, shiftstatus.getStatusTime());

        Uri newuri = context.getContentResolver().insert(uri, values);

        long lastPathSegment = 0;
        if (newuri != null) {
            lastPathSegment = Long.parseLong(newuri.getLastPathSegment());
        }

        return lastPathSegment;
    }

    /**
     * Method to retrieve a shiftstatus with a specific external id
     *
     * @param expoIdExt The id of the shift expo.
     * @param stationIdExt The id of the shift station.
     * @param workerIdExt The id of the shift worker.
     * @return The shiftstatus object for the specified id.
     */
    public List<ShiftStatusInt> getShiftStatusIdExt(int expoIdExt, int stationIdExt, int workerIdExt) {

        String selection = ConSkedCheckInProvider.EXPOIDEXT + " = ? AND " +
                           ConSkedCheckInProvider.STATIONIDEXT + " = ? AND " +
                           ConSkedCheckInProvider.WORKERIDEXT + " = ?";
        String[] selectionArgs = {Integer.toString(expoIdExt),
                                  Integer.toString(stationIdExt),
                                  Integer.toString(workerIdExt)};
        String sortOrder = ConSkedCheckInProvider.STATUSTIME + " ASC";

        Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, sortOrder);

        List<ShiftStatusInt> shiftstatusList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ShiftStatusInt shiftstatus = new ShiftStatusInt(
                            cursor.getInt(0),       // idInt
                            cursor.getInt(1),       // shiftstatusIdExt
                            cursor.getInt(2),       // workerIdExt
                            cursor.getInt(3),       // stationIdExt
                            cursor.getInt(4),       // expoIdExt
                            cursor.getString(5),    // statusType
                            cursor.getString(6));   // statusTime

                    shiftstatusList.add(shiftstatus);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        return shiftstatusList;
    }

    /**
     * Method for deleting all shiftstatus
     *
     * @return The number of rows deleted.
     */
    public int deleteShiftStatusAll() {

        return context.getContentResolver().delete(uri, null, null);
    }
}
