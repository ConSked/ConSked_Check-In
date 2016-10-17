package com.emailxl.consked_check_in.external_db;

import org.json.JSONObject;

/**
 * @author ECG
 */

class TimestampUtils {

    static Timestamp loadTimestamp(JSONObject json) throws Exception {

        String jdate = json.optString("date");
        int jtimezoneType = json.optInt("timezone_type");
        String jtimezone = json.optString("timezone");

        return new Timestamp(jdate, jtimezoneType, jtimezone);
    }
}
