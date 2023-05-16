package com.example.aton_final_project.util.constants;

public enum ServiceConstants {

    GENERAL_INQUIRY("0001"), // 일반문의
    ACCOUNT_INQUIRY("0002"), // 계정문의
    WITHDRAWAL_INQUIRY("0003"),// 탈퇴문의
    REPORT_INQUIRY("0004"),// 신고/이용제한
    COMPANY_NAME("회사명"),
    BUSINESS_NO("사업자번호"),
    IMG_FILE("이미지 파일"),
    PDF_FILE("PDF 파일"),
    DENY_REASON("반려 사유");

    private final String value;
    ServiceConstants(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
