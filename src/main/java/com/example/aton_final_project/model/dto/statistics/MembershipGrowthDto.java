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

    public double parsing_member() {
        return Math.round(((this.todayMember - this.yesterdayMember) / (double)(this.allMember)) * 100);
    }

    public double parsing_login() {
        return Math.round(((this.todayLogin - this.yesterdayLogin) / (double)(this.allMember)) * 100);
    }
}
