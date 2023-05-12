package com.example.aton_final_project.util.constants;

/**
 * 계정 권한 상태
 */
public enum AccountStatus {
    NORMAL("0000"), // 일반
    LONG_TERM_NO_LOGIN("0001"), // 장기미접속자
    MEMBER_LOCK("0002"), // 계정 잠금
    MEMBER_PAUSE("0003"), // 계정 중지
    MEMBER_SECESSION("0004"); // 계정 탈퇴
    private final String value;
    AccountStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
