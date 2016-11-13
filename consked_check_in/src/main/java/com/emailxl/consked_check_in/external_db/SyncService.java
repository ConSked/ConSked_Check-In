package com.emailxl.consked_check_in.external_db;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.emailxl.consked_check_in.utils.AppConstants;

/**
 * @author ECG
 */
public class SyncService extends Service {
    private static final String TAG = "SyncService";
    private static final boolean LOG = AppConstants.LOG_EXT;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();
    // Storage for an instance of the sync adapter
    private static SyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LOG) Log.i(TAG, "onCreate called");
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (LOG) Log.i(TAG, "onDestroy called");
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (LOG) Log.i(TAG, "onBind called");
        return sSyncAdapter.getSyncAdapterBinder();
    }
}
