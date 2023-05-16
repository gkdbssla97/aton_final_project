package com.example.aton_final_project.service.file.service;

import com.example.aton_final_project.model.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    Long findServiceIdByMemberId(Long memberId);
    List<MemberServiceRegisterResponseDto> findServiceByServiceId(Long serviceId) throws Exception;
    void saveFile(FilesDto filesDto, Long serviceId);
    void saveServiceRegister(MemberServiceRegisterRequestDto memberServiceRegisterRequestDto, Long memberId) throws Exception;
    List<MemberServiceRegisterResponseDto> findServiceRegisterById(Long memberId);
    List<MemberServiceRegisterResponseDto> findAllServiceRegister() throws Exception;
    AccessTokenDto findMemberInfoByServiceId(Long serviceId);
    void updateApprovalReason(MemberRequestDto memberRequestDto);
    List<MemberServiceRegisterResponseDto> decryptUsername(List<MemberServiceRegisterResponseDto> serviceRegisterList) throws Exception;
    void updateDenyReason(MemberServiceRegisterResponseDto memberServiceRegisterResponseDto);
    void confirmUploadedFileDataType(MultipartFile uploadFile);
}
