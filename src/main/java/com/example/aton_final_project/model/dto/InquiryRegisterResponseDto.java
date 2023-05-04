package com.example.aton_final_project.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class InquiryRegisterResponseDto {
    private Long inquiryId;
    private Long memberId;
    private String username;
    private String category;
    private String title;
    private String contents;
    private int inquiryStatus;
    private String filename;
    private String originalFileName;
    private String fileUrl;
    private LocalDateTime registerDate;
}
