package com.example.aton_final_project.util.constants;

public enum AuthoritiesConstants {

    ROLE_ADMIN("관리자"), ROLE_MEMBER("일반회원");
    private final String value;
    AuthoritiesConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
