package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Embeddable
public class LastUpdateDateTime {
    @Column(nullable = false, insertable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime lastUpdateDateTime;
}
