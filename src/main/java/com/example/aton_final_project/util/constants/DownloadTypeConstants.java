package com.example.aton_final_project.util.constants;

public enum DownloadTypeConstants {

    IMAGE("image"), PDF("pdf");
    private final String value;
    DownloadTypeConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
