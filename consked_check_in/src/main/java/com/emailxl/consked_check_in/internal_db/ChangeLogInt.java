package com.emailxl.consked_check_in.internal_db;

/**
 * The changeLog table data class
 *
 * @author ECG
 */

public class ChangeLogInt {
    private long timestamp;
    private String source;      // "local" or "remote"
    private String operation;   // "create", "update" or "delete"
    private String tableName;   // "expo", "shiftassignment", "shiftstatus", "station" or "worker"
    private String json;        // JSON encoded data
    private int idInt;
    private int idExt;
    private int done;

    // Constructors
    public ChangeLogInt() {
    }

    ChangeLogInt(long timestamp, String source, String operation, String tableName, String json,
                        int idInt, int idExt, int done) {
        this.timestamp = timestamp;
        this.source = source;
        this.operation = operation;
        this.tableName = tableName;
        this.json = json;
        this.idInt = idInt;
        this.idExt = idExt;
        this.done = done;
    }

    // timestamp functions
    long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // source functions
    public String getSource() {
        return this.source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    // operation functions
    public String getOperation() {
        return this.operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    // tableName functions
    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    // json functions
    public String getJson() {
        return this.json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    // idInt functions
    public int getIdInt() {
        return this.idInt;
    }

    public void setIdInt(int idInt) {
        this.idInt = idInt;
    }

    // idExt functions
    int getIdExt() {
        return this.idExt;
    }

    public void setIdExt(int idExt) {
        this.idExt = idExt;
    }

    // done functions
    int getDone() {
        return this.done;
    }

    public void setDone(int done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" +
                "timestamp=" + timestamp + ", " +
                "source=" + source + ", " +
                "operation=" + operation + ", " +
                "tableName=" + tableName + ", " +
                "json=" + json + ", " +
                "idInt=" + idInt + ", " +
                "idExt=" + idExt + ", " +
                "done=" + done + ", " + "]";
    }
}
