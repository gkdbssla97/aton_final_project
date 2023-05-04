package com.example.aton_final_project.model.domain.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberAuthoritiesCode {

    @Id
    @GeneratedValue
    @Column(name = "member_authorities_code_id")
    private long memberAuthoritiesCodeId;

    @Column(nullable = false)
    private String authority;

    @Column
    @CreationTimestamp
    private LocalDateTime registerDate;

    @OneToMany(mappedBy = "memberAuthoritiesCode", fetch = FetchType.LAZY)
    private List<MemberAuthoritiesMapping> memberAuthoritiesMappingList = new ArrayList<>();
}
