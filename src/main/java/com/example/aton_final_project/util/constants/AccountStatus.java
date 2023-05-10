package com.example.aton_final_project.util.constants;

/**
 * 계정 권한 상태
 */
public enum AccountStatus {
    WAIT('N'), APPROVAL('Y'); // 대기 / 승인
    private final char value;
    AccountStatus(char value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
