package com.example.aton_final_project.model.dao;

import com.example.aton_final_project.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ServiceRegisterMapper {
    Long findServiceIdByMemberId(@Param("memberId") Long memberId);
    List<MemberServiceRegisterResponseDto> findServiceByServiceId(@Param("serviceId") Long serviceId);

    void saveServiceRegister(@Param("memberServiceRegisterRequestDto") MemberServiceRegisterRequestDto memberServiceRegisterRequestDto, @Param("memberId") Long memberId);

    void saveFile(@Param("filesDto") FilesDto filesDto, @Param("serviceId") Long serviceId);

    List<FilesDto> findFilesById(@Param("memberId") Long memberId);

    List<MemberServiceRegisterResponseDto> findServiceRegisterById(@Param("memberId") Long memberId);

    List<FilesDto> findAllFiles();

    List<MemberServiceRegisterResponseDto> findAllServiceRegister();

    AccessTokenDto findMemberInfoByServiceId(@Param("serviceId") Long serviceId);

    void updateApprovalReason(@Param("memberRequestDto") MemberRequestDto memberRequestDto);

    void updateDenyReason(@Param("memberServiceRegisterResponseDto") MemberServiceRegisterResponseDto memberServiceRegisterResponseDto);
}
