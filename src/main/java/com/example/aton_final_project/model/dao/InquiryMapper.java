package com.example.aton_final_project.model.dao;

import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.model.dto.statistics.InquiryGrowthDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    void updateInquiryAnswer(@Param("inquiryRegisterRequestDto") InquiryRegisterRequestDto inquiryRegisterRequestDto);
    List<InquiryRegisterResponseDto> findInquiryByInquiryId(@Param("inquiryId") Long inquiryId);
    int count();
    int count_private(Map<String, Object> params);
    List<InquiryRegisterResponseDto> getListWithPaging(Map<String, Object> params);
    List<InquiryRegisterResponseDto> getListWithPagingForPrivate(Map<String, Object> params);
    InquiryGrowthDto countInquiryRequest();
    List<InquiryRegisterResponseDto>findLastInquiry(@Param("memberId") Long memberId);
}
