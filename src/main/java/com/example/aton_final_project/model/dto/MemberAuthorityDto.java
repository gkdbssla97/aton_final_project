package com.example.aton_final_project.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberAuthorityDto {
    private Long memberId;
    private String accountStatus;
}
