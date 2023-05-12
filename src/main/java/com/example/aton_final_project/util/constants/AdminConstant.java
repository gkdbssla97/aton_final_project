package com.example.aton_final_project.util.constants;

public enum AdminConstant {
    SUPER_ADMIN_USERNAME("하윤"), // 슈퍼관리자 사용자명
    SUPER_ADMIN_EMAIL("hayoon@atoncorp.com"), // 슈퍼관리자 이메일
    SUPER_ADMIN_AUTHORITY("슈퍼관리자");
    private final String value;
    AdminConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
