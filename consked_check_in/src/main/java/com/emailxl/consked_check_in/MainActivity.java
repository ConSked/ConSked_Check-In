package com.emailxl.consked_check_in;

import android.accounts.Account;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.emailxl.consked_check_in.internal_db.ExpoHandler;
import com.emailxl.consked_check_in.internal_db.ExpoInt;
import com.emailxl.consked_check_in.internal_db.StationJobHandler;
import com.emailxl.consked_check_in.internal_db.StationJobInt;
import com.emailxl.consked_check_in.internal_db.WorkerHandler;
import com.emailxl.consked_check_in.internal_db.WorkerInt;
import com.emailxl.consked_check_in.utils.AppConstants;

import static com.emailxl.consked_check_in.utils.Utils.createSyncAccount;
import static com.emailxl.consked_check_in.utils.Utils.toastError;

public class MainActivity extends Activity {
    public static final String ACTION_FINISHED_SYNC = "com.emailxl.consked_check_in.ACTION_FINISHED_SYNC";
    private static final String TAG = "MainActivity";
    private static boolean LOG = false;

    private EditText etId;
    private RadioButton rbExpo, rbStationJob, rbWorker;
    private TextView tvOutput;
    private Account account;
    private static IntentFilter syncIntentFilter = new IntentFilter(ACTION_FINISHED_SYNC);
    private ProgressDialog pd;
    private BroadcastReceiver mainBroadcastReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (LOG) Log.i(TAG, "Create page");

        // Check network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            etId = (EditText) findViewById(R.id.edit_id);
            etId.setText("1");

            rbExpo = (RadioButton) findViewById(R.id.radio_Expo);
            rbStationJob = (RadioButton) findViewById(R.id.radio_StationJob);
            rbWorker = (RadioButton) findViewById(R.id.radio_Worker);

            tvOutput = (TextView) findViewById(R.id.output);
            tvOutput.setText("");

            account = createSyncAccount(this);

            findViewById(R.id.button_test).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            int id = Integer.parseInt(etId.getText().toString().trim());

                            String table = "";
                            if (rbExpo.isChecked()) {
                                table = "Expo";
                            } else if (rbStationJob.isChecked()) {
                                table = "StationJob";
                            } else if (rbWorker.isChecked()) {
                                table = "Worker";
                            }

                            Bundle extras = new Bundle();
                            extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                            extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                            extras.putInt("id", id);
                            extras.putString("table", table);

                            ContentResolver.requestSync(account, AppConstants.AUTHORITY, extras);
                            pd = ProgressDialog.show(view.getContext(), "", getString(R.string.loading), true);
                        }
                    }
            );

            findViewById(R.id.button_next).setOnClickListener(
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            Intent inShiftStatus = new Intent(view.getContext(), ShiftStatus.class);
                            startActivity(inShiftStatus);
                            finish();
                        }
                    }
            );

        } else {
            toastError(MainActivity.this, R.string.error_network);
        }

        mainBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String output = "";

                int id = Integer.parseInt(etId.getText().toString().trim());

                if (rbExpo.isChecked()) {
                    ExpoHandler db = new ExpoHandler(context);
                    ExpoInt expo = db.getExpoIdExt(id);
                    if (expo != null) {
                        output = expo.toString();
                    }

                } else if (rbStationJob.isChecked()) {
                    StationJobHandler db = new StationJobHandler(context);
                    StationJobInt stationJob = db.getStationJobIdExt(id);
                    if (stationJob != null) {
                        output = stationJob.toString();
                    }

                } else if (rbWorker.isChecked()) {
                    WorkerHandler db = new WorkerHandler(context);
                    WorkerInt worker = db.getWorkerIdExt(id);
                    if (worker != null) {
                        output = worker.toString();
                    }
                }

                tvOutput.setText(output);

                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(mainBroadcastReceiver, syncIntentFilter);
    }

    protected void onPause() {
        super.onPause();

        if (mainBroadcastReceiver != null) {
            unregisterReceiver(mainBroadcastReceiver);
        }
    }
}
