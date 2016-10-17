package com.emailxl.consked_check_in.external_db;

import android.util.Log;

import com.emailxl.consked_check_in.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.emailxl.consked_check_in.utils.Utils.readStream;

/**
 * @author ECG
 */

class WorkerAPI {

    private static final String SERVER_URL = AppConstants.SERVER_DIR + "Worker/";
    private static final String TAG = "WorkerAPI";
    private static final boolean LOG = false;

    static WorkerExt[] readWorker(int id) {

        String stringUrl = SERVER_URL;

        if (id != 0) {
            stringUrl = stringUrl + id;
        }

        InputStream is = null;
        WorkerExt[] output = null;

        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-li-format", "json");

            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String result = readStream(is);
                output = loadWorker(result);
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

    static WorkerExt[] searchWorker(String username) {

        String stringUrl = SERVER_URL + "Search/" + username;

        InputStream is = null;
        WorkerExt[] output = null;

        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-li-format", "json");

            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String result = readStream(is);
                output = loadWorker(result);
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

    private static WorkerExt[] loadWorker(String result) throws Exception {

        JSONArray jArray = new JSONArray(result);

        int len = jArray.length();
        WorkerExt[] output = new WorkerExt[len];

        for (int i = 0; i < len; i++) {
            JSONObject json = jArray.getJSONObject(i);

            int jworkerid = json.optInt("workerid");
            int jisDisabled = json.optInt("isDisabled");
            String jlastLoginTime = json.optString("lastLoginTime");
            String jphone = json.optString("phone");
            String jemail = json.optString("email");
            String jsmsemail = json.optString("smsemail");
            String jpasswordHash = json.optString("passwordHash");
            String jresetCodeHash = json.optString("resetCodeHash");
            String jfirstName = json.optString("firstName");
            String jmiddleName = json.optString("middleName");
            String jlastName = json.optString("lastName");
            String jexternalAuthentication = json.optString("externalAuthentication");
            String jauthrole = json.optString("authrole");

            WorkerExt worker = new WorkerExt();
            worker.setWorkerIdExt(jworkerid);
            worker.setIsDisabled(jisDisabled);
            worker.setLastLoginTime(jlastLoginTime);
            worker.setPhone(jphone);
            worker.setEmail(jemail);
            worker.setSmsemail(jsmsemail);
            worker.setPasswordHash(jpasswordHash);
            worker.setResetCodeHash(jresetCodeHash);
            worker.setFirstName(jfirstName);
            worker.setMiddleName(jmiddleName);
            worker.setLastName(jlastName);
            worker.setExternalAuthentication(jexternalAuthentication);
            worker.setAuthrole(jauthrole);

            output[i] = worker;
        }

        return output;
    }
}
