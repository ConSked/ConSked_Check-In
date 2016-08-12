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
import com.emailxl.consked_check_in.internal_db.ExpoHandler;
import com.emailxl.consked_check_in.internal_db.ExpoInt;
import com.emailxl.consked_check_in.internal_db.WorkerHandler;
import com.emailxl.consked_check_in.internal_db.WorkerInt;

import static com.emailxl.consked_check_in.external_db.ExpoAPI.searchExpo;
import static com.emailxl.consked_check_in.external_db.WorkerAPI.searchWorker;

/**
 * handle the transfer of data between a server and an app.
 * using the Android sync adapter framework.
 *
 * @author ECG
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = "SyncAdapter";
    private static final boolean LOG = true;

    ContentResolver mContentResolver;
    private Context mContext;

    public SyncAdapter(Context context, boolean autoInitialize) {
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

        String action = extras.getString("action");
        String username = extras.getString("username");

        if ("read".equals(action)) {
            syncReadExt(username);
        }

        if (LOG) Log.i(TAG, "Ending sync");
    }

    private void syncReadExt(String username) {

        if (LOG) Log.i(TAG, "Read");

        WorkerHandler dbw = new WorkerHandler(mContext);
        dbw.deleteWorkerAll();

        ExpoHandler dbe = new ExpoHandler(mContext);
        dbe.deleteExpoAll();

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
                dbw.addWorker(workerInt);
            }

            // get expos from the external database
            ExpoExt[] expoExts = searchExpo(workerExts[0].getWorkerIdExt());

            // load expos into internal database
            if (expoExts != null && expoExts.length != 0) {
                for (ExpoExt expoExt : expoExts) {

                    ExpoInt expoInt = new ExpoInt();
                    expoInt.setExpoIdExt(expoExt.getExpoIdExt());
                    expoInt.setStartTime(expoExt.getStartTime().getDate());
                    expoInt.setStopTime(expoExt.getStopTime().getDate());
                    expoInt.setTitle(expoExt.getTitle());
                    dbe.addExpo(expoInt);
                }
            }
        }

        mContext.sendBroadcast(new Intent(Loading.ACTION_FINISHED_SYNC));
    }
}
