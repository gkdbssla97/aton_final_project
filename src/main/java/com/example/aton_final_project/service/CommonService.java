package com.example.aton_final_project.service;

import com.example.aton_final_project.model.dto.MemberResponseDto;
import com.example.aton_final_project.util.constants.DataTypeConstants;
import org.springframework.web.multipart.MultipartFile;

public interface CommonService {
    String verificationAuthority();
    void uploadFiles(MultipartFile[] uploadFiles, MemberResponseDto loginMember, DataTypeConstants type);
}
