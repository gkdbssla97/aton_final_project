package com.example.aton_final_project.service.file.inquiry;

import com.example.aton_final_project.model.dao.InquiryMapper;
import com.example.aton_final_project.model.dao.MemberMapper;
import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.util.AESCipher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryMapper inquiryMapper;
    private final MemberMapper memberMapper;
    private final MemberService memberService;

    @Override
    public Long findInquiryIdByMemberId(Long memberId) {
        return inquiryMapper.findInquiryIdByMemberId(memberId);
    }

    @Override
    public void saveInquiryFile(FilesDto filesDto, Long inquiryId) {
        inquiryMapper.saveInquiryFile(filesDto, inquiryId);
    }

    @Override
    public void saveInquiry(InquiryRegisterRequestDto inquiryRegisterRequestDto, Long memberId) throws Exception {
        MemberResponseDto memberById = memberService.findMemberById(memberId);
        inquiryRegisterRequestDto.setUsername(memberById.getUsername());
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

    @Override
    public List<InquiryRegisterResponseDto> decryptUsername(List<InquiryRegisterResponseDto> inquiryList) throws Exception {
        AESCipher aesCipher;
        for (InquiryRegisterResponseDto inquiry : inquiryList) {
            AccessTokenDto findMemberInfo = findMemberInfoByInquiryId(inquiry.getInquiryId());
            aesCipher = new AESCipher(findMemberInfo.getEncryptKey());
            System.out.println("암호키: " + findMemberInfo.getEncryptKey());
            inquiry.setUsername(aesCipher.decrypt(findMemberInfo.getUsername()));
        }
        return inquiryList;
    }

    @Override
    public void updateInquiryAnswer(InquiryRegisterRequestDto inquiryRegisterRequestDto) {
        inquiryMapper.updateInquiryAnswer(inquiryRegisterRequestDto);
    }

    @Override
    public List<InquiryRegisterResponseDto> findInquiryByInquiryId(Long inquiryId) throws Exception {
        List<InquiryRegisterResponseDto> findInquiryByInquiryId = inquiryMapper.findInquiryByInquiryId(inquiryId);

        AESCipher aesCipher = new AESCipher(memberMapper.findEncryptKeyByMemberId(findInquiryByInquiryId.get(0).getMemberId()));
        String encryptUsername = findInquiryByInquiryId.get(0).getUsername();
        findInquiryByInquiryId.get(0).setUsername(aesCipher.decrypt(encryptUsername));

        return findInquiryByInquiryId;
    }
}
