package com.example.testAi.user.global.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter(AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTime extends BaseEntity {
    @CreatedDate
    @Column(name="create_date", updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name="modify_date")
    private LocalDateTime modifyDate;

    public void setModified() {
        setModifyDate(LocalDateTime.now());
    }
}