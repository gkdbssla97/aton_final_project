package com.example.aton_final_project.model.domain.service;

import com.example.aton_final_project.model.domain.member.Member;
import com.example.aton_final_project.util.constants.ServiceStatus;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberServiceRegister {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id", nullable = false)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "company_name", nullable = false)
    private String companyName;
    @Column(name = "business_no", nullable = false)
    private String businessNo;
    @Column(name = "service_status", nullable = false, columnDefinition = "integer default 0")
    private ServiceStatus serviceStatus;
    @Column(name = "deny_reason", nullable = true, columnDefinition = "VARCHAR(1000)")
    private String denyReason;
    @Column
    @CreationTimestamp
    private LocalDateTime registerDate;
    @Column
    @CreationTimestamp
    private LocalDateTime completionDate;
    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "memberServiceRegister", cascade = CascadeType.ALL)
    private List<Files> file;
}
