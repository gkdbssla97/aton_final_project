package com.example.aton_final_project.model.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MemberAuthoritiesMappingDto {

    private Long memberAuthoritiesMappingId;
    private Long memberId;
    private Long memberAuthoritiesCodeId;
}
