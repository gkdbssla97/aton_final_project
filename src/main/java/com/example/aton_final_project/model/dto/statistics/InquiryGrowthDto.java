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
}
