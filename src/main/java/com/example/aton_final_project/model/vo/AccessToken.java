package com.example.aton_final_project.model.vo;

import com.example.aton_final_project.model.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_token_id", nullable = false)
    private Long id;
    @Column(name = "encrypt_key", length = 32)
    private String encryptKey; // ENCRYPT_KEY
    @Column(name = "authorization", length = 16)
    private String authorization; // ACCESS_TOKEN
    @Column(name = "email")
    private String email; // user Email

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
