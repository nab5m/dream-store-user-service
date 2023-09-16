package com.junyounggoat.dreamstore.userservice.entity;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

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
