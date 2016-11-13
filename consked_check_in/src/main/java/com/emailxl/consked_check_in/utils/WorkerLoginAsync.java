package com.emailxl.consked_check_in.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.emailxl.consked_check_in.external_db.WorkerLoginExt;

import static com.emailxl.consked_check_in.external_db.WorkerLoginAPI.WorkerLogin;

/**
 * @author ECG
 */

public class WorkerLoginAsync extends AsyncTask<WorkerLoginExt, Void, String> {
    private static final String TAG = "WorkerLoginAsync";
    private static final boolean LOG = AppConstants.LOG_UTILS;

    private Context context;
    private int messageId;
    private ProgressDialog pd;

    public WorkerLoginAsync(Context context, int messageId) {
        this.context = context;
        this.messageId = messageId;
    }

    @Override
    protected void onPreExecute() {
        pd = ProgressDialog.show(context, "", context.getString(messageId), true);
        if (LOG) Log.i(TAG, "Starting AsyncTask");
    }

    @Override
    protected String doInBackground(WorkerLoginExt... input) {

        return WorkerLogin(input[0]);
    }

    @Override
    protected void onPostExecute(String output) {
        if (pd != null && pd.isShowing()) {
            pd.dismiss();
        }
        if (LOG) Log.i(TAG, "Ending AsyncTask");
    }
}
