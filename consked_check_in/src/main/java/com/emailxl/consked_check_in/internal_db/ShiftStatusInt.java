package com.emailxl.consked_check_in.internal_db;

/**
 * @author ECG
 */

public class ShiftStatusInt {
    private int idInt;
    private int shiftstatusIdExt;
    private int workerIdExt;
    private int stationIdExt;
    private int expoIdExt;
    private String statusType;
    private String statusTime;

    // Constructors
    public ShiftStatusInt() {
    }

    public ShiftStatusInt(int idInt, int shiftstatusIdExt,
                          int workerIdExt, int stationIdExt, int expoIdExt,
                          String statusType, String statusTime) {
        this.idInt = idInt;
        this.shiftstatusIdExt = shiftstatusIdExt;
        this.workerIdExt = workerIdExt;
        this.stationIdExt = stationIdExt;
        this.expoIdExt = expoIdExt;
        this.statusType = statusType;
        this.statusTime = statusTime;
    }

    // idInt functions
    public int getIdInt() {
        return this.idInt;
    }

    public void setIdInt(int idInt) {
        this.idInt = idInt;
    }

    // shiftstatusIdExt functions
    public int getShiftstatusIdExt() {
        return this.shiftstatusIdExt;
    }

    public void setShiftstatusIdExt(int shiftstatusIdExt) {
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
    public String getStatusType() {
        return this.statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    // statusTime functions
    public String getStatusTime() {
        return this.statusTime;
    }

    public void setStatusTime(String statusTime) {
        this.statusTime = statusTime;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" +
                "idInt=" + idInt + ", " +
                "shiftstatusIdExt=" + shiftstatusIdExt + ", " +
                "workerIdExt=" + workerIdExt + ", " +
                "stationIdExt=" + stationIdExt + ", " +
                "expoIdExt=" + expoIdExt + ", " +
                "statusType=" + statusType + ", " +
                "statusTime=" + statusTime + "]";
    }
}
