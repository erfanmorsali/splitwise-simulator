package com.splitwise.application.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@MappedSuperclass
public class BaseEntity {
    @Column(name = "deleted")
    private LocalDateTime deleted;
    @CreationTimestamp
    @Column(name = "created", updatable = false)
    private LocalDateTime created;
    @UpdateTimestamp
    @Column(name = "updated")
    private LocalDateTime updated;
}
