package com.emailxl.consked_check_in.external_db;

/**
 * @author ECG
 */

class WorkerExt {
    private int workerIdExt;
    private int isDisabled;
    private String lastLoginTime;
    private String phone;
    private String email;
    private String smsemail;
    private String passwordHash;
    private String resetCodeHash;
    private String firstName;
    private String middleName;
    private String lastName;
    private String externalAuthentication;
    private String authrole;

    // Constructors
    WorkerExt() {
    }

    /*public WorkerExt(int workerIdExt, int isDisabled, String lastLoginTime, String phone, String email,
                     String smsemail, String passwordHash, String resetCodeHash, String firstName,
                     String middleName, String lastName, String externalAuthentication,
                     String authrole) {
        this.workerIdExt = workerIdExt;
        this.isDisabled = isDisabled;
        this.lastLoginTime = lastLoginTime;
        this.phone = phone;
        this.email = email;
        this.smsemail = smsemail;
        this.passwordHash = passwordHash;
        this.resetCodeHash = resetCodeHash;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.externalAuthentication = externalAuthentication;
        this.authrole = authrole;
    }*/

    // workerIdExt functions
    public int getWorkerIdExt() {
        return this.workerIdExt;
    }

    public void setWorkerIdExt(int workerIdExt) {
        this.workerIdExt = workerIdExt;
    }

    // isDisabled functions
    /*public int getIsDisabled() {
        return this.isDisabled;
    }*/

    void setIsDisabled(int isDisabled) {
        this.isDisabled = isDisabled;
    }

    // lastLoginTime functions
    /*public String getLastLoginTime() {
        return this.lastLoginTime;
    }*/

    void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    // phone functions
    /*public String getPhone() {
        return this.phone;
    }*/

    void setPhone(String phone) {
        this.phone = phone;
    }

    // email functions
    /*public String getEmail() {
        return this.email;
    }*/

    public void setEmail(String email) {
        this.email = email;
    }

    // smsemail functions
    /*public String getSmsemail() {
        return this.smsemail;
    }*/

    void setSmsemail(String smsemail) {
        this.smsemail = smsemail;
    }

    // passwordHash functions
    /*public String getPasswordHash() {
        return this.passwordHash;
    }*/

    void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // resetCodeHash functions
    /*public String getResetCodeHash() {
        return this.resetCodeHash;
    }*/

    void setResetCodeHash(String resetCodeHash) {
        this.resetCodeHash = resetCodeHash;
    }

    // firstName functions
    String getFirstName() {
        return this.firstName;
    }

    void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    // middleName functions
    /*public String getMiddleName() {
        return this.middleName;
    }*/

    void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    // lastName functions
    String getLastName() {
        return this.lastName;
    }

    void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // externalAuthentication functions
    /*public String getExternalAuthentication() {
        return this.externalAuthentication;
    }*/

    void setExternalAuthentication(String externalAuthentication) {
        this.externalAuthentication = externalAuthentication;
    }

    // authrole functions
    String getAuthrole() {
        return this.authrole;
    }

    void setAuthrole(String authrole) {
        this.authrole = authrole;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" +
                "workerIdExt=" + workerIdExt + ", " +
                "isDisabled=" + isDisabled + ", " +
                "lastLoginTime=" + lastLoginTime + ", " +
                "phone=" + phone + ", " +
                "email=" + email + ", " +
                "smsemail=" + smsemail + ", " +
                "passwordHash=" + passwordHash + ", " +
                "resetCodeHash=" + resetCodeHash + ", " +
                "firstName=" + firstName + ", " +
                "middleName=" + middleName + ", " +
                "lastName=" + lastName + ", " +
                "externalAuthentication=" + externalAuthentication + ", " +
                "authrole=" + authrole + "]";
    }
}
