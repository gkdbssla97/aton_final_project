package com.example.aton_final_project.service.file.inquiry;

import com.example.aton_final_project.model.dao.InquiryMapper;
import com.example.aton_final_project.model.dto.FilesDto;
import com.example.aton_final_project.model.dto.InquiryRegisterRequestDto;
import com.example.aton_final_project.model.dto.InquiryRegisterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryMapper inquiryMapper;

    @Override
    public Long findInquiryIdByMemberId(Long memberId) {
        return inquiryMapper.findInquiryIdByMemberId(memberId);
    }

    @Override
    public void saveInquiryFile(FilesDto filesDto, Long inquiryId) {
        inquiryMapper.saveInquiryFile(filesDto, inquiryId);
    }

    @Override
    public void saveInquiry(InquiryRegisterRequestDto inquiryRegisterRequestDto, Long memberId) {
        inquiryMapper.saveInquiry(inquiryRegisterRequestDto, memberId);
    }

    @Override
    public List<FilesDto> findFilesById(Long memberId) {
        return inquiryMapper.findFilesById(memberId);
    }

    @Override
    public List<FilesDto> findAllFiles() {
        return inquiryMapper.findAllFiles();
    }

    @Override
    public List<InquiryRegisterResponseDto> findInquiriesRegisterById(Long memberId) {
        return inquiryMapper.findInquiriesRegisterById(memberId);
    }
}
