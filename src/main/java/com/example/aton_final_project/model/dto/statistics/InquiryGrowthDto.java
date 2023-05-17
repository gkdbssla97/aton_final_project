package com.example.aton_final_project.model.dto.statistics;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class InquiryGrowthDto {
    private int allInquiry;
    private int yesterdayInquiry;
    private int todayInquiry;
    private double growth_inquiry;

    public double parsing_inquiry() {
        return Math.round(((this.todayInquiry - this.yesterdayInquiry) / (double)(this.allInquiry)) * 100);
    }
}
