package com.example.aton_final_project.model.domain.inquiry;

import com.example.aton_final_project.model.domain.member.Member;
import com.example.aton_final_project.util.constants.InquiryStatus;
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
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiry_id", nullable = false)
    private Long id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name ="category", nullable = false, length = 4)
    private String category;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "contents", nullable = false, length = 300)
    private String contents;
    @Column(name = "inquiry_status", nullable = false, length = 1, columnDefinition = "integer default 0")
    private InquiryStatus inquiryStatus;
    @Column(name = "answer_inquiry", nullable = true, columnDefinition = "VARCHAR(1000)")
    private String answerInquiry;
    @Column
    @CreationTimestamp
    private LocalDateTime registerDate;
    @Column
    @CreationTimestamp
    private LocalDateTime answerDate;
    @Column
    @CreationTimestamp
    private LocalDateTime completionDate;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @OneToMany(mappedBy = "inquiry", cascade = CascadeType.ALL)
    private List<InquiryFiles> files;
}
