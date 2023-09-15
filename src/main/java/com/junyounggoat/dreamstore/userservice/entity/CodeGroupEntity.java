package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class CodeGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long codeGroupId;

    @Column(unique = true, nullable = false, length = 30, updatable = false)
    @Size(max = 30)
    @NotBlank
    private String codeGroupName;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
