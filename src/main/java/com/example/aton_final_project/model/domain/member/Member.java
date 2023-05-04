package com.example.aton_final_project.model.domain.member;

import com.example.aton_final_project.model.domain.inquiry.Inquiry;
import com.example.aton_final_project.model.domain.service.MemberServiceRegister;
import com.example.aton_final_project.model.vo.AccessToken;
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
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long id;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "phoneNo", nullable = false)
    private String phoneNo;
    @Column(name = "telcoTycd", length = 1, nullable = false)
    private String telcoTycd; //통신사 구분 코드
    @Column
    @CreationTimestamp
    private LocalDateTime registerDate;
    @Column
    @CreationTimestamp
    private LocalDateTime updateDate;
    @Column
    private LocalDateTime deleteDate;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<MemberAuthoritiesMapping> memberAuthoritiesMappingList;
    @OneToOne(mappedBy = "member")
    private AccessToken accessToken;
    @OneToOne(mappedBy = "member")
    private MemberServiceRegister memberServiceRegister;
    @OneToOne(mappedBy = "member")
    private Inquiry inquiry;
}
