package com.example.aton_final_project.util.constants;

/**
 * 회원 가입 상태
 */
public enum MemberStatus {
    WAIT('N'), APPROVAL('Y'); // 대기 / 승인
    private final char value;
    MemberStatus(char value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
