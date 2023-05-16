package com.example.aton_final_project.util.error;

import com.example.aton_final_project.util.error.code.CommonError;
import com.example.aton_final_project.util.error.customexception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLSyntaxErrorException;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandler {
    @ExceptionHandler(RegisterCustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionRegister(RegisterCustomException e) {
        return ErrorResponseEntity.toResponseEntityRegister(e.getAuthenticationRegistrationError(), e.getKey());
    }
    @ExceptionHandler(LoginCustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionLogin(LoginCustomException e) {
        return ErrorResponseEntity.toResponseEntityLogin(e.getAuthenticationLoginError(), e.getKey(), e.getLoginFailureCount());
    }
    @ExceptionHandler(CommonCustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionCommon(CommonCustomException e) {
        return ErrorResponseEntity.toResponseEntityCommon(e.getCommonError());
    }
    @ExceptionHandler(ServiceCustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionService(ServiceCustomException e) {
        return ErrorResponseEntity.toResponseEntityService(e.getServiceRegisterError(), e.getKey());
    }
    @ExceptionHandler(InquiryCustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionInquiry(InquiryCustomException e) {
        return ErrorResponseEntity.toResponseEntityInquiry(e.getInquiryError(), e.getKey());
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionHttpMessageNotReadable() {
        return ErrorResponseEntity.toResponseEntityCommon(CommonError.REQUESTED_BODY_FORMAT_ERROR);
    }
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionHttpRequestMethodNotSupported() {
        return ErrorResponseEntity.toResponseEntityCommon(CommonError.UNSUPPORTED_HTTP_METHOD);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionMethodArgumentNotValid() {
        return ErrorResponseEntity.toResponseEntityCommon(CommonError.REQUESTED_BODY_FORMAT_ERROR);
    }
    @ExceptionHandler(SQLSyntaxErrorException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionSQLSyntaxError() {
        return ErrorResponseEntity.toResponseEntityCommon(CommonError.TEMPORARY_ERROR_REQUEST);
    }
    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomExceptionIllegalState() {
        return ErrorResponseEntity.toResponseEntityCommon(CommonError.TEMPORARY_ERROR_REQUEST);
    }
}
