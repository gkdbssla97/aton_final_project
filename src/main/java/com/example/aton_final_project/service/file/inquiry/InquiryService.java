package com.example.aton_final_project.service.file.inquiry;

import com.example.aton_final_project.model.dto.FilesDto;
import com.example.aton_final_project.model.dto.InquiryRegisterRequestDto;
import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;

import java.util.List;

public interface InquiryService {
    Long findInquiryIdByMemberId(Long memberId);
    void saveInquiryFile(FilesDto filesDto, Long inquiryId);
    void saveInquiry(InquiryRegisterRequestDto inquiryRegisterRequestDto, Long memberId);
    List<FilesDto> findFilesById(Long memberId);
    List<FilesDto> findAllFiles();
    List<InquiryRegisterResponseDto> findInquiriesRegisterById(Long memberId);
}
