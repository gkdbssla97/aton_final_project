package com.example.aton_final_project.service.member;

import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.util.constants.AccountStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface MemberService {
    public void joinAdmin(MemberRequestDto memberRequestDto) throws Exception;
    void authorizeAdmin(Long memberId);
    void joinMember(MemberRequestDto memberRequestDto) throws Exception;
    void authorizeMember(Long memberId);
    void saveMemberAccessKey(Long memberId, String authorization, String encryptKey, String email);
    MemberResponseDto findMemberById(Long id) throws Exception;
    MemberResponseDto findMemberByEmail(String email) throws Exception;
    String findEncryptKeyByMemberId(Long memberId);
    AccessTokenDto findMemberKeyByEmail(String email);
    LogInResponseDto maskingInformationByLogIn(String username, String phoneNo) throws Exception;
    SignUpResponseDto maskingInformationBySignUp(MemberRequestDto memberRequestDto);
    String isVerifiedMember(String email, String password, Long memberId) throws Exception;
    Long findMemberAuthorityByMemberId(Long memberId);
    LogInResponseDto findMemberAuthorityInfoByMemberId(Long memberId);

    MemberResponseDto maskingInformationByEdit(MemberResponseDto memberResponseDto);
    void editMemberInformation(Long memberId, String newPassword) throws Exception;
    List<MemberResponseDto> getListWithPaging(Map<String, Object> map) throws Exception;
    void resetLoginFailCount(Long memberId);
    void updateLoginFailCount(int failCount, Long memberId);
    void updateMemberApproval(Long memberId);
    void updateMemberToAdmin(Long memberId);
    void updateLastLoginDate(Long memberId, LocalDateTime localDateTime);
    void activeLongTermMember(Long memberId, AccountStatus accountStatus);
    void inactiveLongTermMember(Long memberId, LocalDateTime localDateTime, AccountStatus accountStatus);
    void pauseMember(Long memberId, LocalDateTime localDateTime, AccountStatus accountStatus);
    void lockMember(Long memberId, LocalDateTime localDateTime, AccountStatus accountStatus);
    void deleteMember(Long memberId);
}
