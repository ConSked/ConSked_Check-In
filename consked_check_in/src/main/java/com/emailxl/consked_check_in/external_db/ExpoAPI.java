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

class ExpoAPI {

    private static final String SERVER_URL = AppConstants.SERVER_DIR + "Expo/";
    private static final String TAG = "ExpoAPI";
    private static final boolean LOG = AppConstants.LOG_EXT;

    /*public static ExpoExt[] readExpo(int id) {

        if (LOG) Log.i(TAG, "ExpoExt called");

        String stringUrl = SERVER_URL;

        if (id != 0) {
            stringUrl = stringUrl + id;
        }

        InputStream is = null;
        ExpoExt[] output = null;

        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-li-format", "json");

            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String result = readStream(is);
                output = loadExpo(result);
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
    }*/

    static ExpoExt[] searchExpo(int workerId) {

        if (LOG) Log.i(TAG, "searchExpo called");

        String stringUrl = SERVER_URL + "Search/" + workerId;

        InputStream is = null;
        ExpoExt[] output = null;

        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-li-format", "json");

            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String result = readStream(is);
                output = loadExpo(result);
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

    private static ExpoExt[] loadExpo(String result) throws Exception {

        JSONArray jArray = new JSONArray(result);

        int len = jArray.length();
        ExpoExt[] output = new ExpoExt[len];

        for (int i = 0; i < len; i++) {
            JSONObject json = jArray.getJSONObject(i);

            int jexpoid = json.optInt("expoid");
            JSONObject jstartTime = json.optJSONObject("startTime");
            JSONObject jstopTime = json.optJSONObject("stopTime");
            int jexpoHourCeiling = json.optInt("expoHourCeiling");
            String jtitle = json.optString("title");
            String jdescription = json.optString("description");
            boolean jscheduleAssignAsYouGo = json.optBoolean("scheduleAssignAsYouGo");
            boolean jscheduleVisible = json.optBoolean("scheduleVisible");
            boolean jallowScheduleTimeConflict = json.optBoolean("allowScheduleTimeConflict");
            boolean jnewUserAddedOnRegistration = json.optBoolean("newUserAddedOnRegistration");
            int jworkerid = json.optInt("workerid");

            ExpoExt expo = new ExpoExt();
            expo.setExpoIdExt(jexpoid);
            expo.setStartTime(TimestampUtils.loadTimestamp(jstartTime));
            expo.setStopTime(TimestampUtils.loadTimestamp(jstopTime));
            expo.setExpoHourCeiling(jexpoHourCeiling);
            expo.setTitle(jtitle);
            expo.setDescription(jdescription);
            expo.setScheduleAssignAsYouGo(jscheduleAssignAsYouGo ? 1 : 0);
            expo.setScheduleVisible(jscheduleVisible ? 1 : 0);
            expo.setAllowScheduleTimeConflict(jallowScheduleTimeConflict ? 1 : 0);
            expo.setNewUserAddedOnRegistration(jnewUserAddedOnRegistration ? 1 : 0);
            expo.setWorkerIdExt(jworkerid);

            output[i] = expo;
        }

        return output;
    }
}
