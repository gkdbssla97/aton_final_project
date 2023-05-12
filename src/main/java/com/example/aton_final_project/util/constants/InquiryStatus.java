package com.example.aton_final_project.util.constants;

public enum InquiryStatus {
    WAIT(0), COMPLETE(1); // 대기 / 답변완료
    private final int value;
    InquiryStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
