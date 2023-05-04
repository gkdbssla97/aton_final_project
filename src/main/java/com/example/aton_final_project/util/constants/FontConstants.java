package com.example.aton_final_project.util.constants;

public enum FontConstants {
    CUSTOM_FONT("맑은 고딕");
    private final String value;
    FontConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
