package com.example.aton_final_project.util.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ServiceRegisterError {
    MISSING_REQUIRED_ITEM(5101, "필수항목 %s이(가) 누락되었습니다.", "SERVICE", HttpStatus.BAD_REQUEST),
    INVALID_VALUE(5102, "%s이(가) 유효하지 않습니다.", "SERVICE", HttpStatus.BAD_REQUEST),
    NUMBERS_OF_IMAGES(5103, "이미지는 2개를 업로드해야 합니다.", "SERVICE", HttpStatus.BAD_REQUEST),

    TEMPORARY_ERROR(5199, "일시적인 오류가 발생했습니다. 잠시 후 다시 요청해주세요.", "SERVICE", HttpStatus.INTERNAL_SERVER_ERROR);

    private final Integer errorCd;
    private final String errorMessage;
    private final String errorPointCd;
    private final HttpStatus httpStatus;
}
