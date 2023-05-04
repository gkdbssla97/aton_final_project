package com.example.aton_final_project.model.domain.service;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id", nullable = false)
    private Long id;
    @Column(name = "filename", nullable = false)
    private String filename;
    @Column(name = "original_filename", nullable = false)
    private String originalFileName;
    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private MemberServiceRegister memberServiceRegister;
}
