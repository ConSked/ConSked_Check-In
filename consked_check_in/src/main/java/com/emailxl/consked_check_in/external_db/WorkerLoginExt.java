package com.emailxl.consked_check_in.external_db;

/**
 * @author ECG
 */

public class WorkerLoginExt {
    private String email;
    private String password;

    // Constructors
    public WorkerLoginExt() {
    }

    /*public WorkerLoginExt(String email, String password) {
        this.email = email;
        this.password = password;
    }*/

    // username functions
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // password functions
    String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" +
                "email=" + email + ", " +
                "password=" + password + "]";
    }
}
