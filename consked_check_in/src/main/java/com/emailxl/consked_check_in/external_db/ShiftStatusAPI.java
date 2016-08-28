package com.emailxl.consked_check_in.external_db;

import android.util.Log;

import com.emailxl.consked_check_in.utils.AppConstants;

import org.json.JSONArray;
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

public class ShiftStatusAPI {

    private static final String SERVER_URL = AppConstants.SERVER_DIR + "ShiftStatus/";
    private static final String TAG = "ShiftStatusAPI";
    private static final boolean LOG = false;

    public static int createShiftStatus(ShiftStatusExt params) {

        InputStream is = null;
        int output = 0;

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
                output = Integer.parseInt(readStream(is));
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

    public static ShiftStatusExt[] readShiftStatus(int expoIdExt, int stationIdExt, int workerIdExt) {

        String stringUrl = SERVER_URL + "Search/";

        if (expoIdExt != 0) {
            stringUrl += expoIdExt;

            if (stationIdExt != 0) {
                stringUrl += "/" + stationIdExt;

                if (workerIdExt != 0) {
                    stringUrl += "/" + workerIdExt;
                }
            }
        }

        InputStream is = null;
        ShiftStatusExt[] output = null;

        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-li-format", "json");

            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String result = readStream(is);
                output = loadShiftStatus(result);
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

    private static String loadJson(ShiftStatusExt params) throws Exception {

        JSONObject json = new JSONObject();

        if (params.getShiftstatusIdExt() != 0) json.put("shiftstatusIdExt", params.getShiftstatusIdExt());
        if (params.getWorkerIdExt() != 0) json.put("workerIdExt", params.getWorkerIdExt());
        if (params.getStationIdExt() != 0) json.put("stationIdExt", params.getStationIdExt());
        if (params.getExpoIdExt() != 0) json.put("expoIdExt", params.getExpoIdExt());
        if (params.getStatusType() != null && !params.getStatusType().isEmpty())
            json.put("statusType", params.getStatusType());
        if (params.getStatusTime() != null && !params.getStatusType().isEmpty())
            json.put("statusTime", params.getStatusTime().getDate());

        return json.toString();
    }

    private static ShiftStatusExt[] loadShiftStatus(String result) throws Exception {

        JSONArray jArray = new JSONArray(result);

        int len = jArray.length();
        ShiftStatusExt[] output = new ShiftStatusExt[len];

        for (int i = 0; i < len; i++) {
            JSONObject json = jArray.getJSONObject(i);

            int jshiftstatusid = json.has("shiftstatusid") ? json.getInt("shiftstatusid") : 0;
            int jworkerid = json.has("workerid") ? json.getInt("workerid") : 0;
            int jstationid = json.has("stationid") ? json.getInt("stationid") : 0;
            int jexpoid = json.has("expoid") ? json.getInt("expoid") : 0;
            String jstatusType = json.has("statusType") ? json.getString("statusType") : null;
            JSONObject jStatusTime = json.has("statusTime") ? json.getJSONObject("statusTime") : null;

            ShiftStatusExt shiftstatus = new ShiftStatusExt();
            shiftstatus.setShiftstatusIdExt(jshiftstatusid);
            shiftstatus.setWorkerIdExt(jworkerid);
            shiftstatus.setStationIdExt(jstationid);
            shiftstatus.setExpoIdExt(jexpoid);
            shiftstatus.setStatusType(jstatusType);
            shiftstatus.setStatusTime(TimestampUtils.loadTimestamp(jStatusTime));

            output[i] = shiftstatus;
        }

        return output;
    }
}
