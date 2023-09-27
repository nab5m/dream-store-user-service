package com.junyounggoat.dreamstore.userservice.entity;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Embeddable
public class TimestampEmbeddable {
    @Embedded
    @JsonUnwrapped
    private CreationDateTime creationDateTime;

    @Embedded
    @JsonUnwrapped
    private LastUpdateDateTime lastUpdateDateTime;

    @Embedded
    @JsonUnwrapped
    private DeletionDateTime deletionDateTime;

    public @Nullable LocalDateTime getCreationDateTime() {
        if (creationDateTime == null) {
            return null;
        }

        return creationDateTime.getCreationDateTime();
    }

    public @Nullable LocalDateTime getLastUpdateDateTime() {
        if (lastUpdateDateTime == null) {
            return null;
        }

        return lastUpdateDateTime.getLastUpdateDateTime();
    }

    public @Nullable LocalDateTime getDeletionDateTime() {
        if (deletionDateTime == null) {
            return null;
        }

        return deletionDateTime.getDeletionDateTime();
    }
}
