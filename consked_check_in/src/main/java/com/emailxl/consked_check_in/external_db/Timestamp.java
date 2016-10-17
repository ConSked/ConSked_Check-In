package com.emailxl.consked_check_in.external_db;

/**
 * @author ECG
 */

class Timestamp {
    private String date;
    private int timezoneType;
    private String timezone;

    // Constructors
    Timestamp() {
    }

    Timestamp(String date, int timezoneType, String timezone) {
        this.date = date;
        this.timezoneType = timezoneType;
        this.timezone = timezone;
    }

    // date functions
    String getDate() {
        return this.date;
    }

    void setDate(String date) {
        this.date = date;
    }

    // timezoneType functions
    /*public int getTimezoneType() {
        return this.timezoneType;
    }*/

    void setTimezoneType(int timezoneType) {
        this.timezoneType = timezoneType;
    }

    // timezone functions
    /*public String getTimezone() {
        return this.timezone;
    }*/

    void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" +
                "date=" + date + ", " +
                "timezoneType=" + timezoneType + ", " +
                "timezone=" + timezone + ", " + "]";
    }
}
