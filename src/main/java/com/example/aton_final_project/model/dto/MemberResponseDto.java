package com.example.aton_final_project.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberResponseDto {
    private Long memberId;
    private String username;
    private String email;
    private String password;
    private String phoneNo;
    private String telcoTycd; //통신사 구분 코드
    private Boolean memberStatus;
    private String accountStatus;
    private LocalDateTime registerDate;
    private LocalDateTime approvalDate;
    private LocalDateTime lastLoginDate;
    private LocalDateTime lockDate;
    private String authority; // 관리자 / 일반회원
    private String encryptKey;
    private String newPassword; // 새 비밀번호 변경
    private int loginFailCount;
}
