package com.example.aton_final_project.service.statistics;

import com.example.aton_final_project.model.dao.StatisticsMapper;
import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.model.dto.MemberServiceRegisterResponseDto;
import com.example.aton_final_project.model.dto.statistics.MyStatisticsDto;
import com.example.aton_final_project.model.dto.statistics.StatisticsDto;
import com.example.aton_final_project.util.AESCipher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsMapper statisticsMapper;

    @Override
    public StatisticsDto findAdminApprovalChangeRate() {
        StatisticsDto adminApprovalChangeRate = statisticsMapper.findAdminApprovalChangeRate();
        adminApprovalChangeRate.setAdminRate(
                findAdminApproval().getAdminApproved() * 100 / adminApprovalChangeRate.getAllAdmin()
        );
        return adminApprovalChangeRate;
    }

    @Override
    public StatisticsDto findMemberApprovalChangeRate() {
        StatisticsDto memberApprovalChangeRate = statisticsMapper.findMemberApprovalChangeRate();
        memberApprovalChangeRate.setMemberRate(
                findMemberApproval().getMemberApproved() * 100 / memberApprovalChangeRate.getAllMember()
        );
        return memberApprovalChangeRate;
    }

    @Override
    public StatisticsDto findServiceApprovalChangeRate() {
        StatisticsDto serviceApprovalChangeRate = statisticsMapper.findServiceApprovalChangeRate();
        serviceApprovalChangeRate.setServiceRate(
                findServiceApproved().getServiceApproved() * 100 / serviceApprovalChangeRate.getAllService()
        );
        return serviceApprovalChangeRate;
    }

    @Override
    public StatisticsDto findInquiryApprovalChangeRate() {
        StatisticsDto inquiryApprovalChangeRate = statisticsMapper.findInquiryApprovalChangeRate();
        inquiryApprovalChangeRate.setInquiryRate(
                findInquiryApproved().getInquiryApproved() * 100 / inquiryApprovalChangeRate.getAllInquiry()
        );
        return inquiryApprovalChangeRate;
    }

    @Override
    public StatisticsDto findAdminApproval() {
        return statisticsMapper.findAdminApproval();
    }

    @Override
    public StatisticsDto findMemberApproval() {
        return statisticsMapper.findMemberApproval();
    }

    @Override
    public StatisticsDto findServiceApproved() {
        return statisticsMapper.findServiceApproved();
    }

    @Override
    public StatisticsDto findInquiryApproved() {
        return statisticsMapper.findInquiryApproved();
    }

    @Override
    public StatisticsDto findTotalMember() {
        return statisticsMapper.findTotalMember();
    }

    @Override
    public StatisticsDto findServiceMemberCount() {
        return statisticsMapper.findServiceMemberCount();
    }

    @Override
    public StatisticsDto findInquiryMemberCount() {
        return statisticsMapper.findInquiryMemberCount();
    }

    @Override
    public MemberResponseDto findLastOneMember() throws Exception {
        MemberResponseDto lastOneMember = statisticsMapper.findLastOneMember();
        AESCipher aesCipher = new AESCipher(lastOneMember.getEncryptKey());
        lastOneMember.setUsername(aesCipher.decrypt(lastOneMember.getUsername()));
        return lastOneMember;
    }

    @Override
    public MemberResponseDto findLastOneApprovedAdmin() throws Exception {
        MemberResponseDto lastOneApprovedMember = statisticsMapper.findLastOneApprovedAdmin();
        AESCipher aesCipher = new AESCipher(lastOneApprovedMember.getEncryptKey());
        lastOneApprovedMember.setUsername(aesCipher.decrypt(lastOneApprovedMember.getUsername()));
        return lastOneApprovedMember;
    }

    @Override
    public MemberServiceRegisterResponseDto findLastOneService() {
        return statisticsMapper.findLastOneService();
    }

    @Override
    public InquiryRegisterResponseDto findLastOneInquiry() {
        return statisticsMapper.findLastOneInquiry();
    }

    @Override
    public MyStatisticsDto findMyService(Long memberId) {
        return statisticsMapper.findMyService(memberId);
    }

    @Override
    public MyStatisticsDto findMyInquiry(Long memberId) {
        return statisticsMapper.findMyInquiry(memberId);
    }
}
