package com.junyounggoat.dreamstore.user.repository.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CodeGroup {
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
