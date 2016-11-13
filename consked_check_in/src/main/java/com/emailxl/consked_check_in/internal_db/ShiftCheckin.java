package com.emailxl.consked_check_in.internal_db;

/**
 * @author ECG
 */

public class ShiftCheckin {
    private ShiftAssignmentInt shiftAssignment;
    private String name;
    private String statusType;

    // Constructors
    public ShiftCheckin() {
    }

    /*public ShiftCheckin(ShiftAssignmentInt shiftAssignment, String name, String statusType) {

        this.shiftAssignment = shiftAssignment;
        this.name = name;
        this.statusType = statusType;
    }*/

    // ShiftAssignment functions
    public ShiftAssignmentInt getShiftAssignment() {
        return this.shiftAssignment;
    }

    public void setShiftAssignment(ShiftAssignmentInt shiftAssignment) {
        this.shiftAssignment = shiftAssignment;
    }

    // name functions
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // statusType functions
    public String getStatusType() {
        return this.statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" +
                "shiftAssignment=" + shiftAssignment.toString() + ", " +
                "name=" + name + ", " +
                "statusType=" + statusType + "]";
    }
}
