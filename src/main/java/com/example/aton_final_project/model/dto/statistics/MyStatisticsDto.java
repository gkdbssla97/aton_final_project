package com.example.aton_final_project.model.dto.statistics;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MyStatisticsDto {
    private int myAdmin;
    private int myMember;
    private int myService;
    private int myInquiry;
    private int myAdminApproved;
    private int myMemberApproved;
    private int myServiceApproved;
    private int myInquiryApproved;
    private double myAdminRate;//
    private double myMemberRate;//
    private double myServiceRate;//
    private double myInquiryRate;//

}
