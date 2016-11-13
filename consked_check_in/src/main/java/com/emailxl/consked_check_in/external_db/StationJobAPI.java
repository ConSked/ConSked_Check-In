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

class StationJobAPI {

    private static final String SERVER_URL = AppConstants.SERVER_DIR + "StationJob/";
    private static final String TAG = "StationJobAPI";
    private static final boolean LOG = AppConstants.LOG_EXT;

    /*public static StationJobExt[] readStationJob(int id) {

        if (LOG) Log.i(TAG, "readStationJob called");

        String stringUrl = SERVER_URL;

        if (id != 0) {
            stringUrl = stringUrl + id;
        }

        InputStream is = null;
        StationJobExt[] output = null;

        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-li-format", "json");

            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String result = readStream(is);
                output = loadStationJob(result);
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

    static StationJobExt[] searchStationJob(int expoId) {

        if (LOG) Log.i(TAG, "searchStationJob called");

        String stringUrl = SERVER_URL + "Search/" + expoId;

        InputStream is = null;
        StationJobExt[] output = null;

        try {
            URL url = new URL(stringUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("x-li-format", "json");

            if (conn.getResponseCode() == 200) {

                is = conn.getInputStream();
                String result = readStream(is);
                output = loadStationJob(result);
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

    private static StationJobExt[] loadStationJob(String result) throws Exception {

        JSONArray jArray = new JSONArray(result);

        int len = jArray.length();
        StationJobExt[] output = new StationJobExt[len];

        for (int i = 0; i < len; i++) {
            JSONObject json = jArray.getJSONObject(i);

            int jstationid = json.optInt("stationid");
            int jexpoid = json.optInt("expoid");
            JSONObject jstartTime = json.optJSONObject("startTime");
            JSONObject jstopTime = json.optJSONObject("stopTime");
            String jstationTitle = json.optString("title");
            String jdescription = json.optString("description");
            String jlocation = json.optString("location");
            String jURL = json.optString("URL");
            String jinstruction = json.optString("instruction");
            int jjobid = json.optInt("jobid");
            String jjobTitle = json.optString("jobTitle");
            int jmaxCrew = json.optInt("maxCrew");
            int jminCrew = json.optInt("minCrew");
            int jassignedCrew = json.optInt("assignedCrew");
            int jmaxSupervisor = json.optInt("maxSupervisor");
            int jminSupervisor = json.optInt("minSupervisor");
            int jassignedSupervisor = json.optInt("assignedSupervisor");


            StationJobExt stationjob = new StationJobExt();
            stationjob.setStationIdExt(jstationid);
            stationjob.setExpoIdExt(jexpoid);
            stationjob.setStartTime(TimestampUtils.loadTimestamp(jstartTime));
            stationjob.setStopTime(TimestampUtils.loadTimestamp(jstopTime));
            stationjob.setStationTitle(jstationTitle);
            stationjob.setDescription(jdescription);
            stationjob.setLocation(jlocation);
            stationjob.setURL(jURL);
            stationjob.setInstruction(jinstruction);
            stationjob.setJobIdExt(jjobid);
            stationjob.setJobTitle(jjobTitle);
            stationjob.setMaxCrew(jmaxCrew);
            stationjob.setMinCrew(jminCrew);
            stationjob.setAssignedCrew(jassignedCrew);
            stationjob.setMaxSupervisor(jmaxSupervisor);
            stationjob.setMinSupervisor(jminSupervisor);
            stationjob.setAssignedSupervisor(jassignedSupervisor);

            output[i] = stationjob;
        }

        return output;
    }
}
