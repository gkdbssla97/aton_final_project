package com.example.aton_final_project.model.dto.statistics;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MembershipGrowthDto {
    private int allMember;
    private int yesterdayMember;
    private int yesterdayLogin;
    private int todayLogin;
    private int todayMember;
    private double growth_member;
    private double growth_login;
}
