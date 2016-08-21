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

public class ExpoAPI {

    private static final String SERVER_URL = AppConstants.SERVER_DIR + "Expo/";
    private static final String TAG = "ExpoAPI";
    private static final boolean LOG = false;

    public static ExpoExt[] readExpo(int id) {

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
    }

    public static ExpoExt[] searchExpo(int workerId) {

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

            int jexpoid = json.has("expoid") ? json.getInt("expoid") : 0;
            JSONObject jstartTime = json.has("startTime") ? json.getJSONObject("startTime") : null;
            JSONObject jstopTime = json.has("stopTime") ? json.getJSONObject("stopTime") : null;
            int jexpoHourCeiling = json.has("expoHourCeiling") ? json.getInt("expoHourCeiling") : 0;
            String jtitle = json.has("title") ? json.getString("title") : null;
            String jdescription = json.has("description") ? json.getString("description") : null;
            boolean jscheduleAssignAsYouGo = json.has("scheduleAssignAsYouGo") || json.getBoolean("scheduleAssignAsYouGo");
            boolean jscheduleVisible = json.has("scheduleVisible") || json.getBoolean("scheduleVisible");
            boolean jallowScheduleTimeConflict = json.has("allowScheduleTimeConflict") || json.getBoolean("allowScheduleTimeConflict");
            boolean jnewUserAddedOnRegistration = json.has("newUserAddedOnRegistration") || json.getBoolean("newUserAddedOnRegistration");
            int jworkerid = json.has("workerid") ? json.getInt("workerid") : 0;

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
