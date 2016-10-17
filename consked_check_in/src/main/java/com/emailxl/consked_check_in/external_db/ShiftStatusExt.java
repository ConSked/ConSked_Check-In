package com.emailxl.consked_check_in.external_db;

/**
 * @author ECG
 */

class ShiftStatusExt {
    private int shiftstatusIdExt;
    private int workerIdExt;
    private int stationIdExt;
    private int expoIdExt;
    private String statusType;
    private Timestamp statusTime;

    // Constructors
    ShiftStatusExt() {
    }

    /*public ShiftStatusExt(int shiftstatusIdExt, int workerIdExt, int stationIdExt, int expoIdExt,
                          String statusType, Timestamp statusTime) {
        this.shiftstatusIdExt = shiftstatusIdExt;
        this.workerIdExt = workerIdExt;
        this.stationIdExt = stationIdExt;
        this.expoIdExt = expoIdExt;
        this.statusType = statusType;
        this.statusTime = statusTime;
    }*/

    // shiftstatusIdExt functions
    int getShiftstatusIdExt() {
        return this.shiftstatusIdExt;
    }

    void setShiftstatusIdExt(int shiftstatusIdExt) {
        this.shiftstatusIdExt = shiftstatusIdExt;
    }

    // workerIdExt functions
    public int getWorkerIdExt() {
        return this.workerIdExt;
    }

    public void setWorkerIdExt(int workerIdExt) {
        this.workerIdExt = workerIdExt;
    }

    // stationIdExt functions
    public int getStationIdExt() {
        return this.stationIdExt;
    }

    public void setStationIdExt(int stationIdExt) {
        this.stationIdExt = stationIdExt;
    }

    // expoIdExt functions
    public int getExpoIdExt() {
        return this.expoIdExt;
    }

    public void setExpoIdExt(int expoIdExt) {
        this.expoIdExt = expoIdExt;
    }

    // statusType functions
    String getStatusType() {
        return this.statusType;
    }

    void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    // statusTime functions
    Timestamp getStatusTime() {
        return this.statusTime;
    }

    void setStatusTime(Timestamp statusTime) {
        this.statusTime = statusTime;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" +
                "shiftstatusIdExt=" + shiftstatusIdExt + ", " +
                "workerIdExt=" + workerIdExt + ", " +
                "stationIdExt=" + stationIdExt + ", " +
                "expoIdExt=" + expoIdExt + ", " +
                "statusType=" + statusType + ", " +
                "statusTime=" + statusTime.toString() + "]";
    }
}
