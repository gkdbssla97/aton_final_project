package com.example.aton_final_project.util.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@AllArgsConstructor
@Getter
public enum AuthenticationLoginError {
    MISSING_REQUIRED_ITEM(4101, "필수항목 %s이(가) 누락되었습니다.", "LOGIN", BAD_REQUEST),
    INVALID_VALUE(4102, "%s이(가) 유효하지 않습니다. (5회 실패 시 잠금)", "비밀번호 실패 횟수: %d", "LOGIN", BAD_REQUEST),
    NO_ACCESS_TOKEN_OR_NO_MATCHING_DATA(4103, "Access Token이 없거나 일치하는 데이터가 없습니다.", "LOGIN", BAD_REQUEST),
    USER_NOT_FOUND(4103, "존재하지 않는 사용자입니다.", "LOGIN", BAD_REQUEST),
    LOCK_ACCOUNT(4104, "계정 잠금 회원입니다.", "LOCK", BAD_REQUEST),
    PAUSE_ACCOUNT(4105, "계정 중지 회원입니다.", "PAUSE", BAD_REQUEST),
    UNAPPROVED(4106, "미승인 회원입니다.", "UNAPPROVED", BAD_REQUEST),

    TEMPORARY_ERROR(4199, "일시적인 오류가 발생했습니다. 잠시 후 다시 요청해주세요.", "LOGIN", INTERNAL_SERVER_ERROR);

    private final Integer errorCd;
    private final String errorMessage;
    private String loginFailureCount;
    private final String errorPointCd;
    private final HttpStatus httpStatus;

    AuthenticationLoginError(Integer errorCd, String errorMessage, String errorPointCd, HttpStatus httpStatus) {
        this.errorCd = errorCd;
        this.errorMessage = errorMessage;
        this.errorPointCd = errorPointCd;
        this.httpStatus = httpStatus;
    }
}
