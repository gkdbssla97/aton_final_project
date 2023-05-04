package com.example.aton_final_project.model.dto;

import com.example.aton_final_project.util.constants.ServiceStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberServiceRegisterRequestDto {
    private String companyName;
    private String businessNo;
    private ServiceStatus serviceStatus;
    private LocalDateTime registerDate = LocalDateTime.now();
}
