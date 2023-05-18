package com.example.aton_final_project.model.dto.statistics;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class StatisticsDto {
    private int allAdmin;
    private int allMember;
    private int allService;
    private int allInquiry;
    private int adminApproved;
    private int memberApproved;
    private int serviceApproved;
    private int inquiryApproved;
    private int adminRate;//
    private int memberRate;//
    private int serviceRate;//
    private int inquiryRate;//

}
