package com.example.aton_final_project.model.dto;

import com.example.aton_final_project.model.domain.member.Member;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberRequestDto {
    private Long id;
    private Long serviceId;
    private String username;
    private String email;
    private String password;
    private String phoneNo;
    private String telcoTycd; //통신사 구분 코드
    private LocalDateTime registerDate;

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .email(email)
                .username(username)
                .password(password)
                .phoneNo(phoneNo)
                .telcoTycd(telcoTycd)
                .registerDate(registerDate)
                .build();
    }
}