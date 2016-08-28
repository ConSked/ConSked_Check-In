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
    private String tableName;   // "attendee", "event", "member", "organization" or "user"
    private int idInt;
    private int idExt;
    private int done;

    // Constructors
    public ChangeLogInt() {
    }

    public ChangeLogInt(long timestamp, String source, String operation, String tableName,
                        int idInt, int idExt, int done) {
        this.timestamp = timestamp;
        this.source = source;
        this.operation = operation;
        this.tableName = tableName;
        this.idInt = idInt;
        this.idExt = idExt;
        this.done = done;
    }

    // timestamp functions
    public long getTimestamp() {
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

    // idInt functions
    public int getIdInt() {
        return this.idInt;
    }

    public void setIdInt(int idInt) {
        this.idInt = idInt;
    }

    // idExt functions
    public int getIdExt() {
        return this.idExt;
    }

    public void setIdExt(int idExt) {
        this.idExt = idExt;
    }

    // done functions
    public int getDone() {
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
                "idInt=" + idInt + ", " +
                "idExt=" + idExt + ", " +
                "done=" + done + ", " + "]";
    }
}
