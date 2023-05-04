package com.example.aton_final_project.util.constants;

public enum ServiceStatus {
    WAIT(0), APPROVAL(1), DENY(2);
    private final int value;
    ServiceStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
