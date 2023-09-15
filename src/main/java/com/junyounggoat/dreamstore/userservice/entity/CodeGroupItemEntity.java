package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;


@Entity
public class CodeGroupItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long codeGroupItemId;

    @ManyToOne
    @JoinColumn(name = "codeGroupId", nullable = false)
    private CodeGroupEntity codeGroup;

    @ManyToOne
    @JoinColumn(name = "codeItemId", nullable = false)
    private CodeItemEntity codeItem;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
