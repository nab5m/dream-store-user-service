package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class DeletionDateTime {
    private LocalDateTime deletionDateTime;
}
