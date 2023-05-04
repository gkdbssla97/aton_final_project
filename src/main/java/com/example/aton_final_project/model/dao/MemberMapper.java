package com.example.aton_final_project.model.dao;

import com.example.aton_final_project.model.dto.AccessTokenDto;
import com.example.aton_final_project.model.dto.MemberRequestDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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

    //    MemberResponseDto findMemberByEmailAndPassword(@Param("email") String email, @Param("password") String password);
    String findEncryptKeyByMemberId(@Param("memberId") Long memberId);

    AccessTokenDto findMemberKeyByEmail(@Param("email") String email);

    Long findMemberAuthorityByMemberId(@Param("memberId") Long memberId);

    void editMemberInformation(@Param("memberId") Long memberId, @Param("newPassword") String newPassword, @Param("updateDate") LocalDateTime updateDate);
}
