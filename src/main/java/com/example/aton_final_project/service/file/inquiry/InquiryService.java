package com.example.aton_final_project.service.file.inquiry;

import com.example.aton_final_project.model.dto.AccessTokenDto;
import com.example.aton_final_project.model.dto.FilesDto;
import com.example.aton_final_project.model.dto.InquiryRegisterRequestDto;
import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;

import java.util.List;

public interface InquiryService {
    Long findInquiryIdByMemberId(Long memberId);

    void saveInquiryFile(FilesDto filesDto, Long inquiryId);

    void saveInquiry(InquiryRegisterRequestDto inquiryRegisterRequestDto, Long memberId) throws Exception;

    List<FilesDto> findFilesById(Long memberId);

    List<FilesDto> findAllFiles();

    List<InquiryRegisterResponseDto> findInquiriesRegisterById(Long memberId);

    List<InquiryRegisterResponseDto> findAllInquiry() throws Exception;

    AccessTokenDto findMemberInfoByInquiryId(Long inquiryId);

    void updateInquiryAnswer(InquiryRegisterRequestDto inquiryRegisterRequestDto);

    List<InquiryRegisterResponseDto> findInquiryByInquiryId(Long inquiryId) throws Exception;

    List<InquiryRegisterResponseDto> decryptUsername(List<InquiryRegisterResponseDto> inquiryList) throws Exception;
}