package com.emailxl.consked_check_in.external_db;

import android.util.Log;

import com.emailxl.consked_check_in.utils.AppConstants;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.emailxl.consked_check_in.utils.Utils.readStream;

/**
 * @author ECG
 */

public class WorkerLoginAPI {

    private static final String SERVER_URL = AppConstants.SERVER_DIR + "WorkerLogin/";
    private static final String TAG = "WorkerLoginAPI";
    private static final boolean LOG = AppConstants.LOG_EXT;

    public static String WorkerLogin(WorkerLoginExt params) {

        if (LOG) Log.i(TAG, "WorkerLogin called");

        InputStream is = null;
        String output = "";

        try {
            URL url = new URL(SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String input = loadJson(params);

            OutputStream os = conn.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");

            osw.write(input);
            osw.flush();
            osw.close();

            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String result = readStream(is);
                output = loadOutput(result);
            }
        } catch (Exception e) {
            if (LOG) Log.e(TAG, e.getMessage());
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    if (LOG) Log.e(TAG, e.getMessage());
                }
            }
        }

        return output;
    }

    private static String loadJson(WorkerLoginExt params) throws Exception {

        JSONObject json = new JSONObject();

        if (params.getEmail() != null && !params.getEmail().isEmpty())
            json.put("email", params.getEmail());
        if (params.getPassword() != null && !params.getPassword().isEmpty())
            json.put("password", params.getPassword());

        return json.toString();
    }

    private static String loadOutput(String result) throws Exception {

        JSONObject json = new JSONObject(result);

        return json.optString("status");
    }
}
