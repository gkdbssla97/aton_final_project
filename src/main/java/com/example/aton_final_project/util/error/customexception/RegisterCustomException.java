package com.example.aton_final_project.util.error.customexception;

import com.example.aton_final_project.util.error.code.AuthenticationRegistrationError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterCustomException extends RuntimeException {
    private AuthenticationRegistrationError authenticationRegistrationError;
    private String key;

    public RegisterCustomException(AuthenticationRegistrationError authenticationRegistrationError) {
        this.authenticationRegistrationError = authenticationRegistrationError;
    }
}
