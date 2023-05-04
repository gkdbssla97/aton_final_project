package com.example.aton_final_project.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AccessTokenDto {
    private Long memberId;
    private String encryptKey;
    private String authorization;
    private String username;
}
