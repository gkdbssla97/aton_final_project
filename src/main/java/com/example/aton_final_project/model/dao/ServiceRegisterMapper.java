package com.example.aton_final_project.model.dao;

import com.example.aton_final_project.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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
    int count();
    AccessTokenDto findMemberInfoByServiceId(@Param("serviceId") Long serviceId);
    void updateApprovalReason(@Param("memberRequestDto") MemberRequestDto memberRequestDto, @Param("completionDate") LocalDateTime localDateTime);
    void updateDenyReason(@Param("memberServiceRegisterResponseDto") MemberServiceRegisterResponseDto memberServiceRegisterResponseDto);

    List<MemberServiceRegisterResponseDto> getListWithPaging(Map<String, Object> params);
}
