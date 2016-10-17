package com.emailxl.consked_check_in.external_db;

/**
 * @author ECG
 */

class StationJobExt {
    private int stationIdExt;
    private int expoIdExt;
    private Timestamp startTime;
    private Timestamp stopTime;
    private String stationTitle;
    private String description;
    private String location;
    private String URL;
    private String instruction;
    private int jobIdExt;
    private String jobTitle;
    private int maxCrew;
    private int minCrew;
    private int assignedCrew;
    private int maxSupervisor;
    private int minSupervisor;
    private int assignedSupervisor;

    // Constructors
    StationJobExt() {
    }

    /*public StationJobExt(int stationIdExt, int expoIdExt, Timestamp startTime, Timestamp stopTime,
                         String stationTitle, String description, String location, String URL,
                         String instruction, int jobIdExt, String jobTitle,
                         int maxCrew, int minCrew, int assignedCrew,
                         int maxSupervisor, int minSupervisor, int assignedSupervisor) {
        this.stationIdExt = stationIdExt;
        this.expoIdExt = expoIdExt;
        this.startTime = startTime;
        this.stopTime = stopTime;
        this.stationTitle = stationTitle;
        this.description = description;
        this.location = location;
        this.URL = URL;
        this.instruction = instruction;
        this.jobIdExt = jobIdExt;
        this.jobTitle = jobTitle;
        this.maxCrew = maxCrew;
        this.minCrew = minCrew;
        this.assignedCrew = assignedCrew;
        this.maxSupervisor = maxSupervisor;
        this.minSupervisor = minSupervisor;
        this.assignedSupervisor = assignedSupervisor;
    }*/

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

    // startTime functions
    public Timestamp getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    // stopTime functions
    public Timestamp getStopTime() {
        return this.stopTime;
    }

    public void setStopTime(Timestamp stopTime) {
        this.stopTime = stopTime;
    }

    // stationTitle functions
    String getStationTitle() {
        return this.stationTitle;
    }

    void setStationTitle(String stationTitle) {
        this.stationTitle = stationTitle;
    }

    // description functions
    /*String getDescription() {
        return this.description;
    }*/

    void setDescription(String description) {
        this.description = description;
    }

    // location functions
    /*public String getLocation() {
        return this.location;
    }*/

    void setLocation(String location) {
        this.location = location;
    }

    // URL functions
    /*public String getURL() {
        return this.URL;
    }*/

    void setURL(String URL) {
        this.URL = URL;
    }

    // instruction functions
    /*public String getInstruction() {
        return this.instruction;
    }*/

    void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    // jobIdExt functions
    /*public int getJobIdExt() {
        return this.jobIdExt;
    }*/

    void setJobIdExt(int jobIdExt) {
        this.jobIdExt = jobIdExt;
    }

    // jobTitle functions
    /*public String getJobTitle() {
        return this.jobTitle;
    }*/

    void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    // maxCrew functions
    /*public int getMaxCrew() {
        return this.maxCrew;
    }*/

    void setMaxCrew(int maxCrew) {
        this.maxCrew = maxCrew;
    }

    // minCrew functions
    /*public int getMinCrew() {
        return this.minCrew;
    }*/

    void setMinCrew(int minCrew) {
        this.minCrew = minCrew;
    }

    // assignedCrew functions
    /*public int getAssignedCrew() {
        return this.assignedCrew;
    }*/

    void setAssignedCrew(int assignedCrew) {
        this.assignedCrew = assignedCrew;
    }

    // maxSupervisor functions
    /*public int getMaxSupervisor() {
        return this.maxSupervisor;
    }*/

    void setMaxSupervisor(int maxSupervisor) {
        this.maxSupervisor = maxSupervisor;
    }

    // minSupervisor functions
    /*public int getMinSupervisor() {
        return this.minSupervisor;
    }*/

    void setMinSupervisor(int minSupervisor) {
        this.minSupervisor = minSupervisor;
    }

    // assignedSupervisor functions
    /*public int getAssignedSupervisor() {
        return this.assignedSupervisor;
    }*/

    void setAssignedSupervisor(int assignedSupervisor) {
        this.assignedSupervisor = assignedSupervisor;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" +
                "stationIdExt=" + stationIdExt + ", " +
                "expoIdExt=" + expoIdExt + ", " +
                "startTime=" + startTime.toString() + ", " +
                "stopTime=" + stopTime.toString() + ", " +
                "stationTitle=" + stationTitle + ", " +
                "description=" + description + ", " +
                "location=" + location + ", " +
                "URL=" + URL + ", " +
                "instruction=" + instruction + ", " +
                "jobIdExt=" + jobIdExt + ", " +
                "jobTitle=" + jobTitle + ", " +
                "maxCrew=" + maxCrew + ", " +
                "minCrew=" + minCrew + ", " +
                "assignedCrew=" + assignedCrew + ", " +
                "maxSupervisor=" + maxSupervisor + ", " +
                "minSupervisor=" + minSupervisor + ", " +
                "assignedSupervisor=" + assignedSupervisor + "]";
    }
}
