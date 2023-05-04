package com.example.aton_final_project.service.mail;

import com.example.aton_final_project.model.dto.FilesDto;
import com.example.aton_final_project.model.dto.MailDto;
import com.example.aton_final_project.model.dto.MemberResponseDto;

public interface MailService {
    void sendMail(MailDto dto, String username);

    void execute(MemberResponseDto memberResponseDto, FilesDto filesDto) throws Exception;
}
