package com.example.aton_final_project.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberServiceRegisterResponseDto {
    private Long serviceId;
    private Long memberId;
    private String username;
    private String email;
    private String denyReason;
    private String companyName;
    private String businessNo;
    private int serviceStatus;
    private String filename;
    private String originalFileName;
    private String fileUrl;
    private LocalDateTime registerDate;
    private LocalDateTime completionDate;
}
