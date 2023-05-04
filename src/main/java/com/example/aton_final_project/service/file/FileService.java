package com.example.aton_final_project.service.file;

import com.example.aton_final_project.model.dto.*;

import java.util.List;

public interface FileService {
    Long findServiceIdByMemberId(Long memberId);
    List<MemberServiceRegisterResponseDto> findServiceByServiceId(Long serviceId) throws Exception;
    void saveFile(FilesDto filesDto, Long serviceId);
    void saveServiceRegister(MemberServiceRegisterRequestDto memberServiceRegisterRequestDto, Long memberId);
    List<FilesDto> findFilesById(Long memberId);
    List<FilesDto> findAllFiles();
    List<MemberServiceRegisterResponseDto> findServiceRegisterById(Long memberId);
    List<MemberServiceRegisterResponseDto> findAllServiceRegister() throws Exception;
    AccessTokenDto findMemberInfoByServiceId(Long serviceId);
    void updateApprovalReason(MemberRequestDto memberRequestDto);

    void updateDenyReason(MemberServiceRegisterResponseDto memberServiceRegisterResponseDto);
}
