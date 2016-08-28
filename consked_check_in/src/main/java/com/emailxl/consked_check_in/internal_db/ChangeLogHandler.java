package com.emailxl.consked_check_in.internal_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.emailxl.consked_check_in.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling the changeLog table
 * <p/>
 * This class is a wrapper for the changeLog part of the Content Provider
 *
 * @author ECG
 */

public class ChangeLogHandler {

    private Context context;
    private Uri uri;

    /**
     * Method to initialize the changeLog handler
     *
     * @param context The caller's context.
     */
    public ChangeLogHandler(Context context) {
        this.context = context;
        this.uri = new Uri.Builder()
                .scheme(AppConstants.SCHEME)
                .authority(AppConstants.AUTHORITY)
                .path(ConSkedCheckInProvider.CHANGELOG_TABLE)
                .build();
    }

    /**
     * Method to add a changelog entry to the changelog table
     *
     * @param changeLog The changeLog object.
     * @return The URI for the newly inserted item.
     */
    public long addChangeLog(ChangeLogInt changeLog) {

        ContentValues values = new ContentValues();

        values.put(ConSkedCheckInProvider.TIMESTAMP, changeLog.getTimestamp());
        values.put(ConSkedCheckInProvider.SOURCE, changeLog.getSource());
        values.put(ConSkedCheckInProvider.OPERATION, changeLog.getOperation());
        values.put(ConSkedCheckInProvider.TABLENAME, changeLog.getTableName());
        values.put(ConSkedCheckInProvider.IDINT, changeLog.getIdInt());
        values.put(ConSkedCheckInProvider.IDEXT, changeLog.getIdExt());
        values.put(ConSkedCheckInProvider.DONE, changeLog.getDone());

        Uri newuri = context.getContentResolver().insert(uri, values);
        context.getContentResolver().notifyChange(uri, null);

        long lastPathSegment = 0;
        if (newuri != null) {
            lastPathSegment = Long.parseLong(newuri.getLastPathSegment());
        }
        return lastPathSegment;
    }

    /**
     * Method to retrieve all changeLogs
     *
     * @return The list of all changeLog objects.
     */
    public List<ChangeLogInt> getAllChangeLogs() {

        String sortOrder = ConSkedCheckInProvider.TIMESTAMP + " ASC";

        Cursor cursor = context.getContentResolver().query(uri, null, null, null, sortOrder);

        List<ChangeLogInt> changeLogList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ChangeLogInt changeLog = new ChangeLogInt(
                            cursor.getLong(0),      // timestamp
                            cursor.getString(1),    // source
                            cursor.getString(2),    // operation
                            cursor.getString(3),    // tableName
                            cursor.getInt(4),       // idInt
                            cursor.getInt(5),       // idExt
                            cursor.getInt(6));      // done

                    changeLogList.add(changeLog);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        return changeLogList;
    }

    /**
     * Method to retrieve the active changeLog entries
     *
     * @return A list of active changeLog objects.
     */
    public List<ChangeLogInt> getActiveChangeLogs() {

        String selection = ConSkedCheckInProvider.DONE + " = ?";
        String[] selectionArgs = {"0"};
        String sortOrder = ConSkedCheckInProvider.TIMESTAMP + " ASC";

        Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, sortOrder);

        List<ChangeLogInt> changeLogList = new ArrayList<>();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ChangeLogInt changeLog = new ChangeLogInt(
                            cursor.getLong(0),      // timestamp
                            cursor.getString(1),    // source
                            cursor.getString(2),    // operation
                            cursor.getString(3),    // tableName
                            cursor.getInt(4),       // idInt
                            cursor.getInt(5),       // idExt
                            cursor.getInt(6));      // done

                    changeLogList.add(changeLog);
                } while (cursor.moveToNext());
            }

            cursor.close();
        }
        return changeLogList;
    }

    /**
     * Method to retrieve a changeLog entry with a specific timestamp
     *
     * @param timestamp The timestamp of the changeLog entry to be retrieved.
     * @return The changeLog object for the specified timestamp.
     */
    public ChangeLogInt getChangeLogTimestamp(long timestamp) {

        String selection = ConSkedCheckInProvider.TIMESTAMP + " = ?";
        String[] selectionArgs = {Long.toString(timestamp)};

        Cursor cursor = context.getContentResolver().query(uri, null, selection, selectionArgs, null);

        ChangeLogInt changeLog = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {

                changeLog = new ChangeLogInt(
                        cursor.getLong(0),      // timestamp
                        cursor.getString(1),    // source
                        cursor.getString(2),    // operation
                        cursor.getString(3),    // tableName
                        cursor.getInt(4),       // idInt
                        cursor.getInt(5),       // idExt
                        cursor.getInt(6));      // done
            }

            cursor.close();
        }
        return changeLog;
    }

    /**
     * Method for updating a specific changeLog entry by tiemstamp
     *
     * @param changeLog The changeLog object for the changeLog entry to be updated.
     * @return The number of rows updated.
     */
    public int updateChangeLogTimestamp(ChangeLogInt changeLog) {

        ContentValues values = new ContentValues();

        values.put(ConSkedCheckInProvider.TIMESTAMP, changeLog.getTimestamp());
        values.put(ConSkedCheckInProvider.SOURCE, changeLog.getSource());
        values.put(ConSkedCheckInProvider.OPERATION, changeLog.getOperation());
        values.put(ConSkedCheckInProvider.TABLENAME, changeLog.getTableName());
        values.put(ConSkedCheckInProvider.IDINT, changeLog.getIdInt());
        values.put(ConSkedCheckInProvider.IDEXT, changeLog.getIdExt());
        values.put(ConSkedCheckInProvider.DONE, changeLog.getDone());

        String selection = ConSkedCheckInProvider.TIMESTAMP + " = ?";
        String[] selectionArgs = {Long.toString(changeLog.getTimestamp())};

        int count = context.getContentResolver().update(uri, values, selection, selectionArgs);
        context.getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * Method for deleting a specific changeLog entry by timestamp
     *
     * @param timestamp The timestamp of the changeLog entry to be deleted.
     * @return The number of rows deleted.
     */
    public int deleteChangeLogTimestamp(long timestamp) {

        String selection = ConSkedCheckInProvider.TIMESTAMP + " = ?";
        String[] selectionArgs = {Long.toString(timestamp)};

        int count = context.getContentResolver().delete(uri, selection, selectionArgs);
        context.getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * Method for deleting all changeLog entries.
     *
     * @return The number of rows deleted.
     */
    public int deleteChangeLogAll() {

        int count = context.getContentResolver().delete(uri, null, null);
        context.getContentResolver().notifyChange(uri, null);
        return count;
    }
}
