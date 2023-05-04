package com.example.aton_final_project.service.member;

import com.example.aton_final_project.model.dto.*;
import org.apache.ibatis.annotations.Param;

public interface MemberService {
    public void joinAdmin(MemberRequestDto memberRequestDto) throws Exception;
    void authorizeAdmin(Long memberId);
    void joinMember(MemberRequestDto memberRequestDto) throws Exception;
    void authorizeMember(Long memberId);
    void saveMemberAccessKey(Long memberId, String authorization, String encryptKey, String email);
    MemberResponseDto findMemberById(Long id) throws Exception;
//    MemberResponseDto findMemberByEmailAndPassword(String email, String password, Long memberId, String encryptKey) throws Exception;
    String findEncryptKeyByMemberId(Long memberId);
    AccessTokenDto findMemberKeyByEmail(String email);
    LogInResponseDto maskingInformationByLogIn(String username, String phoneNo) throws Exception;
    SignUpResponseDto maskingInformationBySignUp(MemberRequestDto memberRequestDto);
    Boolean isVerifiedMember(String email, String password, Long memberId) throws Exception;
    Long findMemberAuthorityByMemberId(@Param("memberId") Long memberId);
    MemberResponseDto maskingInformationByEdit(MemberResponseDto memberResponseDto);
    void editMemberInformation(Long memberId, String newPassword) throws Exception;
}
