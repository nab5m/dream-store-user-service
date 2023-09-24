package com.junyounggoat.dreamstore.user.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;

@Embeddable
@Getter
public class DeletionDateTime {
    private LocalDateTime deletionDateTime;
}
