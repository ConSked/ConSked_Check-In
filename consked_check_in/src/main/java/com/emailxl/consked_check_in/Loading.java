package com.emailxl.consked_check_in;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.emailxl.consked_check_in.utils.AppConstants;

import static com.emailxl.consked_check_in.utils.Utils.createSyncAccount;
import static com.emailxl.consked_check_in.utils.Utils.toastError;

public class Loading extends AppCompatActivity {
    public static final String ACTION_FINISHED_SYNC = "com.emailxl.consked_check_in.ACTION_FINISHED_SYNC";
    private static final String TAG = "Loading";
    private static final boolean LOG = AppConstants.LOG_MAIN;

    private static IntentFilter syncIntentFilter = new IntentFilter(ACTION_FINISHED_SYNC);
    private ProgressDialog pd;
    private BroadcastReceiver loadingReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        if (LOG) Log.i(TAG, "Create page");

        // Check network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            Intent intent = getIntent();
            String email = intent.getStringExtra("email");
            /*String password = intent.getStringExtra("password");*/

            Account account = createSyncAccount(this);

            Bundle extras = new Bundle();
            extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
            extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            extras.putString("action", "read");
            extras.putString("username", email);

            ContentResolver.requestSync(account, AppConstants.AUTHORITY, extras);
            pd = ProgressDialog.show(this, "", getString(R.string.loading), true);

            loadingReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {

                    Intent inStation = new Intent(context, Stations.class);
                    startActivity(inStation);
                    finish();

                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                }
            };

        } else {
            toastError(Loading.this, R.string.error_network);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(loadingReceiver, syncIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (loadingReceiver != null) {
            unregisterReceiver(loadingReceiver);
        }
    }
}
