package com.example.aton_final_project.util.error.customexception;

import com.example.aton_final_project.util.error.code.AuthenticationLoginError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginCustomException extends RuntimeException {
    private AuthenticationLoginError authenticationLoginError;

    private String key;
    private int loginFailureCount = 0;

    public LoginCustomException(AuthenticationLoginError authenticationLoginError) {
        this.authenticationLoginError = authenticationLoginError;
    }

    public LoginCustomException(AuthenticationLoginError authenticationLoginError, String key) {
        this.authenticationLoginError = authenticationLoginError;
        this.key = key;
    }

    public LoginCustomException(AuthenticationLoginError authenticationLoginError, int loginFailureCount) {
        this.authenticationLoginError = authenticationLoginError;
        this.loginFailureCount = loginFailureCount;
    }
}
