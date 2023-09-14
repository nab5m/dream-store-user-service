package com.junyounggoat.dreamstore.userservice.entity;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.generator.EventType;

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
}
