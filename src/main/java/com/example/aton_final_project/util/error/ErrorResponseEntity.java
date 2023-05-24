package com.example.aton_final_project.util.error;

import com.example.aton_final_project.util.error.code.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorResponseEntity {

    private int errorCode;
    private String message;
    private String loginFailureCount;
    private String errorPointCd;
    private HttpStatus httpStatus;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntityRegister(AuthenticationRegistrationError e, String key) {
        String msg = e.getErrorMessage();
        if(key != null) {
            msg = String.format(e.getErrorMessage(), key);
        }
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .errorCode(e.getErrorCd())
                        .message(msg)
                        .errorPointCd(e.getErrorPointCd())
                        .httpStatus(e.getHttpStatus())
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponseEntity> toResponseEntityLogin(AuthenticationLoginError e, String key, int failureCount) {
        String msg = e.getErrorMessage();
        if (key != null) {
            msg = String.format(e.getErrorMessage(), key);
        }
        String failureMsg = e.getLoginFailureCount();
        if(failureMsg != null) {
            failureMsg = String.format(e.getLoginFailureCount(), failureCount);
        }
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .errorCode(e.getErrorCd())
                        .message(msg)
                        .loginFailureCount(failureMsg)
                        .errorPointCd(e.getErrorPointCd())
                        .httpStatus(e.getHttpStatus())
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponseEntity> toResponseEntityCommon(CommonError e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .errorCode(e.getErrorCd())
                        .message(e.getErrorMessage())
                        .errorPointCd(e.getErrorPointCd())
                        .httpStatus(e.getHttpStatus())
                        .build()
                );
    }

    public static ResponseEntity<ErrorResponseEntity> toResponseEntityService(ServiceRegisterError e, String key) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .errorCode(e.getErrorCd())
                        .message(String.format(e.getErrorMessage(), key))
                        .errorPointCd(e.getErrorPointCd())
                        .httpStatus(e.getHttpStatus())
                        .build()
                );
    }
    public static ResponseEntity<ErrorResponseEntity> toResponseEntityInquiry(InquiryError e, String key) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ErrorResponseEntity.builder()
                        .errorCode(e.getErrorCd())
                        .message(String.format(e.getErrorMessage(), key))
                        .errorPointCd(e.getErrorPointCd())
                        .httpStatus(e.getHttpStatus())
                        .build()
                );
    }

}
