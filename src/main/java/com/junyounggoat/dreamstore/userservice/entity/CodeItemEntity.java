package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;


@Entity
public class CodeItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long codeItemId;

    @ManyToOne
    @JoinColumn(name = "code_category_id", nullable = false)
    private CodeCategoryEntity codeCategory;

    @Column(nullable = false, length = 30)
    @NotBlank
    @Size(max = 30)
    private String codeName;

    @ManyToOne
    @JoinColumn(name = "parentCodeItemId")
    private CodeItemEntity parentCodeItem;

    @Column(nullable = false)
    @NotNull
    private int code;

    @Column(nullable = false)
    @Min(value = 0)
    @ColumnDefault("0")
    @Generated
    private long sortPriority = 0L;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
