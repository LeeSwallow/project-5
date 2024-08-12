package com.example.testAi.subject;


import com.example.testAi.user.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int priority;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "expired_date")
    private int expiredDate;

    private Integer depth;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 200)
    private String subject;
    private String description;

    // Tree 구조를 위한 필드
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Subject parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.REMOVE)
    List<Subject> children;

    @Column(name = "is_done")
    private boolean isDone;
    @Column(name = "is_divide")
    private boolean isDivide;
    @Column(name = "is_favorite")
    private boolean isFavorite;
}