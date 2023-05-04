package com.example.aton_final_project.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberResponseDto {
    private Long id;
    private String username;
    private String email;
    private String password;
    private String phoneNo;
    private String telcoTycd; //통신사 구분 코드
    private LocalDateTime registerDate;
    private String authority; // 관리자 / 일반회원

}
