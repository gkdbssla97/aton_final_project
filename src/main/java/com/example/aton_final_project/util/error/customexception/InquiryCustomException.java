package com.example.aton_final_project.util.error.customexception;

import com.example.aton_final_project.util.error.code.InquiryError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class InquiryCustomException extends RuntimeException {
    private InquiryError inquiryError;
    private String key;

    public InquiryCustomException(InquiryError inquiryError) {
        this.inquiryError = inquiryError;
    }
}
