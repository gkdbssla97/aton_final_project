package com.example.aton_final_project.util.constants;

public enum AccessConstants {

    ACCESS_TOKEN(16), ENCRYPT_KEY(32);
    private final int value;
    AccessConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
