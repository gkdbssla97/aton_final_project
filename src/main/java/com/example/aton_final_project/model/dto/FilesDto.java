package com.example.aton_final_project.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class FilesDto {
    private String filename;
    private String originalFileName;
    private String fileUrl;
}
