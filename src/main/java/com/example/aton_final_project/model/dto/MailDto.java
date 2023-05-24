package com.example.aton_final_project.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MailDto {
    private String sender;
    private String receiver;
    private String subject; // 제목
    private String content; // 메일 내용
    private boolean isUseHtmlYn; // 메일 형식이 HTML인지 여부(true, false)
    private List<AttachFileDto> attachFileList = new ArrayList<>();

    public MailDto(String sender, String receiver, String subject, String content, boolean isUseHtmlYn) {
        this.sender = sender;
        this.receiver = receiver;
        this.subject = subject;
        this.content = content;
        this.isUseHtmlYn = isUseHtmlYn;
    }
}
