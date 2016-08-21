package com.emailxl.consked_check_in.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Utility functions
 *
 * @author ECG
 */

public class Utils {
    private static final boolean LOG = false;

    public static void toastError(Context context, int errorId) {
        CharSequence text = context.getString(errorId);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    public static Account createSyncAccount(Context context) {
        final String TAG = "createSyncAccount";

        // Create the account type and default account
        Account newAccount = new Account(AppConstants.ACCOUNT, AppConstants.ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            if (LOG) Log.i(TAG, "Account added");
            ContentResolver.setSyncAutomatically(newAccount, AppConstants.AUTHORITY, true);
        } else {
            if (LOG) Log.i(TAG, "Account already exists");
        }

        return newAccount;
    }

    public static String getDate(String startTime) {
        String[] split1 = startTime.split(" ");
        String[] split2 = split1[0].split("-");

        return split2[0] + "/" + split2[1] + "/" + split2[2];
    }

    public static String getTime(String startTime, String stopTime) {
        String[] startsplit1 = startTime.split(" ");
        String[] stopsplit1 = stopTime.split(" ");

        String[] startsplit2 = startsplit1[1].split(":");
        String[] stopsplit2 = stopsplit1[1].split(":");

        return startsplit2[0] + ":" + startsplit2[1] + " - " +
                stopsplit2[0] + ":" + stopsplit2[1];
    }
}
