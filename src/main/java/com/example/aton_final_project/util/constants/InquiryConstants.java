package com.example.aton_final_project.util.constants;

public enum InquiryConstants {

    GENERAL_INQUIRY("0001"), // 일반문의
    ACCOUNT_INQUIRY("0002"), // 계정문의
    WITHDRAWAL_INQUIRY("0003"),// 탈퇴문의
    REPORT_INQUIRY("0004");// 신고/이용제한
    private final String value;
    InquiryConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
