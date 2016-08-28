package com.emailxl.consked_check_in.internal_db;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.emailxl.consked_check_in.utils.AppConstants;

import static com.emailxl.consked_check_in.utils.Utils.createSyncAccount;

/**
 * @author ECG
 */

public class TableObserver extends ContentObserver {
    private static final String TAG = "TableObserver";
    private static final boolean LOG = true;
    private Context context;

    public TableObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        onChange(selfChange, null);
    }

    @Override
    public void onChange(boolean selfChange, Uri changeUrl) {
        if (LOG) Log.i(TAG, "onChange");
        Account account = createSyncAccount(context);

        Bundle extras = new Bundle();

        ContentResolver.requestSync(account, AppConstants.AUTHORITY, extras);
    }
}
