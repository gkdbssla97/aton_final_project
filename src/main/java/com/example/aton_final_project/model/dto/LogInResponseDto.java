package com.example.aton_final_project.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class LogInResponseDto {
    private String username;
    private String phoneNo;

}
