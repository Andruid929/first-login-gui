package com.andrewjones.logingui.form;

public enum MaritalStatus {

    MARRIED("married", "m"),
    SINGLE("single", "s"),
    WIDOW("widow", "w");

    private final String status;
    private final String statusShort;

    MaritalStatus(String status, String statusShort) {
        this.status = status;
        this.statusShort = statusShort;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusShort() {
        return statusShort;
    }
}
