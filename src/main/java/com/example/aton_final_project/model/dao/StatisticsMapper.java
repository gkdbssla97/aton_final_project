package com.example.aton_final_project.model.dao;

import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.model.dto.statistics.MyStatisticsDto;
import com.example.aton_final_project.model.dto.statistics.StatisticsDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface StatisticsMapper {
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
    MemberResponseDto findLastOneMember();
    MemberResponseDto findLastOneApprovedAdmin();
    MemberServiceRegisterResponseDto findLastOneService();
    InquiryRegisterResponseDto findLastOneInquiry();
    MyStatisticsDto findMyService(@Param("memberId") Long memberId);
    MyStatisticsDto findMyInquiry(@Param("memberId") Long memberId);
}
