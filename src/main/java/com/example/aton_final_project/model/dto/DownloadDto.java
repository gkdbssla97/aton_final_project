package com.example.aton_final_project.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DownloadDto {
    Long serviceId;
    Long memberId;
    private String downloadDataType; //IMG, PDF
}
