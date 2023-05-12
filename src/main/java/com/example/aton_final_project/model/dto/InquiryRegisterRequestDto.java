package com.example.aton_final_project.model.dto;

import com.example.aton_final_project.util.constants.InquiryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class InquiryRegisterRequestDto {
    private Long inquiryId;
    private Long memberId;
    private String username;
    private String answerInquiry;
    private String category;
    private String title;
    private String contents;
    private InquiryStatus inquiryStatus;
    private LocalDateTime registerDate = LocalDateTime.now();
    private LocalDateTime answerDate = LocalDateTime.now();
}
