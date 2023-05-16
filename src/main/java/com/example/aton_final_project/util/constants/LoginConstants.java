package com.example.aton_final_project.util.constants;

public enum LoginConstants {
    NO_ACCOUNT("0001"), // 존재하지 않는 회원입니다.
    NO_MATCH_INFO_ID("0002"), // 로그인 정보가 일치하지 않습니다.
    NO_MATCH_INFO_PWD("0003"), // 로그인 정보가 일치하지 않습니다.
    LOGIN_SUCCESS("0004"), // 탈퇴문의
    ID("아이디"),
    PASSWORD("비밀번호"),
    USERNAME("이름"),
    PHONE_NO("전화번호"),
    EMAIL("이메일"),
    NEW_PASSWORD("새 비밀번호");

    private final String value;
    LoginConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
