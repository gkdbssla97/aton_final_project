package com.example.aton_final_project.model.dao;

import com.example.aton_final_project.model.domain.member.MemberAuthoritiesCode;
import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.model.dto.statistics.MembershipGrowthDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface MemberMapper {
    void joinAdmin(@Param("memberRequestDto") MemberRequestDto memberRequestDto);

    void authorizeAdmin(@Param("memberId") Long memberId);

    void joinMember(@Param("memberRequestDto") MemberRequestDto memberRequestDto);

    void authorizeMember(@Param("memberId") Long memberId);

    void saveMemberAccessKey(@Param("memberId") Long memberId, @Param("authorization") String authorization,
                             @Param("encryptKey") String encryptKey, @Param("email") String email);

    MemberResponseDto findMemberById(@Param("memberId") Long memberId);

    MemberResponseDto findMemberByEmail(@Param("email") String email);

    String findEncryptKeyByMemberId(@Param("memberId") Long memberId);
    int findLoginFailureCountByMemberId(@Param("memberId") Long memberId);
    AccessTokenDto findMemberKeyByEmail(@Param("email") String email);
    Long findMemberAuthorityByMemberId(@Param("memberId") Long memberId);
    LogInResponseDto findMemberAuthorityInfoByMemberId(@Param("memberId") Long memberId);
    void editMemberInformation(@Param("memberId") Long memberId, @Param("newPassword") String newPassword, @Param("updateDate") LocalDateTime updateDate);
    List<MemberResponseDto> findAllMember();
    void resetLoginFailCount(@Param("memberId") Long memberId);
    void updateLoginFailCount(@Param("failCount") int failCount, @Param("memberId") Long memberId);
    void updateMemberApproval(@Param("memberId") Long memberId, @Param("approvalDate") LocalDateTime localDateTime);
    void updateMemberToAdmin(@Param("member_authorities_mapping_id") Long mappingId);
    void updateLastLoginDate(@Param("memberId") Long memberId, @Param("lastLoginDate") LocalDateTime localDateTime);
    void activeLongTermMember(@Param("memberId") Long memberId, @Param("accountStatus") String accountStatus);
    void inactiveLongTermMember(@Param("memberId") Long memberId, @Param("lockDate") LocalDateTime localDateTime, @Param("accountStatus") String accountStatus);
    void pauseMember(@Param("memberId") Long memberId, @Param("lockDate") LocalDateTime localDateTime, @Param("accountStatus") String accountStatus);
    void lockMember(@Param("memberId") Long memberId, @Param("lockDate") LocalDateTime localDateTime, @Param("accountStatus") String accountStatus);
    void deleteMember(@Param("memberId") Long memberId);
    int count();
    int countTodayMember();
    List<MemberResponseDto> getListWithPaging(Map<String, Object> params);
    List<MemberAuthoritiesMappingDto> findAuthoritiesMappingByUserId(String userId);
    MemberAuthoritiesCode findAuthoritiesCodeByCodeId(Long memberAuthoritiesMappingId);
    MembershipGrowthDto countMemberGrowth();
    MembershipGrowthDto countMemberLogin();
}
