package com.junyounggoat.dreamstore.user.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class LastUpdateDateTime {
    @Column(nullable = false, insertable = false, updatable = false)
    @ColumnDefault("CURRENT_TIMESTAMP")
    private LocalDateTime lastUpdateDateTime;
}
