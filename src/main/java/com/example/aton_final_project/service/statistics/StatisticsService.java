package com.example.aton_final_project.service.statistics;

import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.model.dto.statistics.MyStatisticsDto;
import com.example.aton_final_project.model.dto.statistics.StatisticsDto;
import org.apache.ibatis.annotations.Param;

public interface StatisticsService {
    StatisticsDto findAdminApprovalChangeRate();
    StatisticsDto findMemberApprovalChangeRate();
    StatisticsDto findServiceApprovalChangeRate();
    StatisticsDto findInquiryApprovalChangeRate();
    StatisticsDto findAdminApproval();
    StatisticsDto findMemberApproval();
    StatisticsDto findServiceApproved();
    StatisticsDto findInquiryApproved();
    StatisticsDto findTotalMember();
    StatisticsDto findServiceMemberCount();
    StatisticsDto findInquiryMemberCount();

    MemberResponseDto findLastOneMember() throws Exception;
    MemberResponseDto findLastOneApprovedMember() throws Exception;
    MemberServiceRegisterResponseDto findLastOneService();
    InquiryRegisterResponseDto findLastOneInquiry();

    MyStatisticsDto findMyService(Long memberId);
    MyStatisticsDto findMyInquiry(Long memberId);
}
