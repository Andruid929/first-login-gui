package com.andrewjones.logingui.form;

public enum Gender {

    MALE("Male", "m"),
    FEMALE("Female", "f"),
    RATHER_NOT_SAY("Rather not say", "r");

    private final String value;
    private final String alternativeValue;

    Gender(String value, String alternativeValue) {
        this.value = value;
        this.alternativeValue = alternativeValue;
    }

    public String getValue() {
        return value;
    }

    public String getAlternativeValue() {
        return alternativeValue;
    }
}
