package com.example.aton_final_project.util.constants;

public enum DataTypeConstants {
    SERVICE("service"), INQUIRY("inquiry"), MEMBER_APPROVAL("approval"), ADMIN_REGISTER("register");
    private final String value;
    DataTypeConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
