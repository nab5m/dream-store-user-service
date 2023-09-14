package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
public class CodeCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private Long codeCategoryId;

    @Column(nullable = false, unique = true, updatable = false, length = 30)
    @NotBlank
    @Size(max = 30)
    private String codeCategoryName;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
