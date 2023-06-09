package com.example.aton_final_project.model.dto.statistics;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ServiceGrowthDto {
    private int allService;
    private int yesterdayService;
    private int todayService;
    private double growth_service;
}
