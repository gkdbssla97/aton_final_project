package com.example.aton_final_project.service.file;

import com.example.aton_final_project.model.dao.MemberMapper;
import com.example.aton_final_project.model.dao.ServiceRegisterMapper;
import com.example.aton_final_project.model.dto.*;
import com.example.aton_final_project.service.member.MemberService;
import com.example.aton_final_project.util.AESCipher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileService{
    private final ServiceRegisterMapper serviceRegisterMapper;
    private final MemberMapper memberMapper;
    private final MemberService memberService;

    @Override
    public Long findServiceIdByMemberId(Long memberId) {
        return serviceRegisterMapper.findServiceIdByMemberId(memberId);
    }

    @Override
    public List<MemberServiceRegisterResponseDto> findServiceByServiceId(Long serviceId) throws Exception {
        List<MemberServiceRegisterResponseDto> findServiceByServiceId = serviceRegisterMapper.findServiceByServiceId(serviceId);

        AESCipher aesCipher = new AESCipher(memberMapper.findEncryptKeyByMemberId(findServiceByServiceId.get(0).getMemberId()));
        String encryptUsername = findServiceByServiceId.get(0).getUsername();
        findServiceByServiceId.get(0).setUsername(aesCipher.decrypt(encryptUsername));

        return findServiceByServiceId;
    }

    @Override
    public void saveFile(FilesDto filesDto, Long serviceId) {
        serviceRegisterMapper.saveFile(filesDto, serviceId);
    }

    @Override
    public void saveServiceRegister(MemberServiceRegisterRequestDto memberServiceRegisterRequestDto, Long memberId) throws Exception {
        MemberResponseDto memberById = memberService.findMemberById(memberId);
        memberServiceRegisterRequestDto.setUsername(memberById.getUsername());
        serviceRegisterMapper.saveServiceRegister(memberServiceRegisterRequestDto, memberId);
    }
    @Override
    public List<MemberServiceRegisterResponseDto> findServiceRegisterById(Long memberId) {
        return serviceRegisterMapper.findServiceRegisterById(memberId);
    }

    @Override
    public List<MemberServiceRegisterResponseDto> findAllServiceRegister() throws Exception {
        return decryptUsername(serviceRegisterMapper.findAllServiceRegister());
    }

    @Override
    public List<MemberServiceRegisterResponseDto> decryptUsername(List<MemberServiceRegisterResponseDto> serviceRegisterList) throws Exception {
        AESCipher aesCipher;
        for (MemberServiceRegisterResponseDto service : serviceRegisterList) {
            AccessTokenDto findMemberInfo = findMemberInfoByServiceId(service.getServiceId());
            aesCipher = new AESCipher(findMemberInfo.getEncryptKey());
            System.out.println("암호키: " + findMemberInfo.getEncryptKey());
            service.setUsername(aesCipher.decrypt(findMemberInfo.getUsername()));
        }
        return serviceRegisterList;
    }

    @Override
    public AccessTokenDto findMemberInfoByServiceId(Long serviceId) {
        return serviceRegisterMapper.findMemberInfoByServiceId(serviceId);
    }

    @Override
    public void updateApprovalReason(MemberRequestDto memberRequestDto) {
        serviceRegisterMapper.updateApprovalReason(memberRequestDto, LocalDateTime.now());
    }

    @Override
    public void updateDenyReason(MemberServiceRegisterResponseDto memberServiceRegisterResponseDto) {
        serviceRegisterMapper.updateDenyReason(memberServiceRegisterResponseDto);
    }
}
