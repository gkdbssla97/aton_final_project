package com.example.aton_final_project.util.error.customexception;

import com.example.aton_final_project.util.error.code.ServiceRegisterError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ServiceCustomException extends RuntimeException {
    private ServiceRegisterError serviceRegisterError;
    private String key;

    public ServiceCustomException(ServiceRegisterError serviceRegisterError) {
        this.serviceRegisterError = serviceRegisterError;
    }
}
