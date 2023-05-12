package com.example.aton_final_project.util.constants;

public enum LoginConstants {
    NO_ACCOUNT("0001"), // 존재하지 않는 회원입니다.
    NO_MATCH_INFO("0002"), // 로그인 정보가 일치하지 않습니다.
    LOGIN_SUCCESS("0003");// 탈퇴문의
    private final String value;
    LoginConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
