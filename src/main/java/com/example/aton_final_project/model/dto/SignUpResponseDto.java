package com.example.aton_final_project.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SignUpResponseDto {
    private String username;
    private String phoneNo;
    private String email;
    private String password;

}
