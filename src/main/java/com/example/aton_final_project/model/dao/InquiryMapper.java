package com.example.aton_final_project.model.dao;

import com.example.aton_final_project.model.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface InquiryMapper {
    Long findInquiryIdByMemberId(@Param("memberId") Long memberId);
    void saveInquiry(@Param("inquiryRegisterRequestDto") InquiryRegisterRequestDto inquiryRegisterRequestDto, @Param("memberId") Long memberId);
    void saveInquiryFile(@Param("filesDto") FilesDto filesDto, @Param("inquiryId") Long inquiryId);
    List<FilesDto> findFilesById(@Param("memberId") Long memberId);
    List<InquiryRegisterResponseDto> findInquiriesRegisterById(@Param("memberId") Long memberId);
    List<FilesDto> findAllFiles();
    List<InquiryRegisterResponseDto> findAllInquiry() throws Exception;
    AccessTokenDto findMemberInfoByInquiryId(@Param("inquiryId") Long inquiryId);
}
