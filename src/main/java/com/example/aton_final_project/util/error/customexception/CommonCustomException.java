package com.example.aton_final_project.util.error.customexception;

import com.example.aton_final_project.util.error.code.CommonError;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CommonCustomException extends RuntimeException {
    private CommonError commonError;
}
