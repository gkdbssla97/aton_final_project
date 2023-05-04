package com.example.aton_final_project.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class EditProfileDto {
    private String oldPassword; // 기존 비밀번호
    private String newPassword; // 새 비밀번호
    private String renewPassword; // 재 입력한 새 비밀번호
}
