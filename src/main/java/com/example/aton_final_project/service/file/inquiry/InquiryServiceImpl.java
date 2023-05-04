package com.example.aton_final_project.service.file.inquiry;

import com.example.aton_final_project.model.dao.InquiryMapper;
import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.util.AESCipher;
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

    @Override
    public List<InquiryRegisterResponseDto> findAllInquiry() throws Exception {
        AESCipher aesCipher;

        List<InquiryRegisterResponseDto> inquiryList = inquiryMapper.findAllInquiry();
        for (InquiryRegisterResponseDto inquiry : inquiryList) {
            System.out.println("회원이름: " + inquiry.getUsername());
            AccessTokenDto findMemberInfo = findMemberInfoByInquiryId(inquiry.getInquiryId());
            aesCipher = new AESCipher(findMemberInfo.getEncryptKey());
            System.out.println("암호키: " + findMemberInfo.getEncryptKey());
            inquiry.setUsername(aesCipher.decrypt(findMemberInfo.getUsername()));
            System.out.println("복호화된: " + inquiry);
        }
        return inquiryList;
    }

    @Override
    public AccessTokenDto findMemberInfoByInquiryId(Long inquiryId) {
        return inquiryMapper.findMemberInfoByInquiryId(inquiryId);
    }
}
