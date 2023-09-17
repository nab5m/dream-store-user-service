package com.junyounggoat.dreamstore.userservice.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CodeGroupItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long codeGroupItemId;

    @ManyToOne
    @JoinColumn(name = "codeGroupId", nullable = false)
    private CodeGroup codeGroup;

    @ManyToOne
    @JoinColumn(name = "codeItemId", nullable = false)
    private CodeItem codeItem;

    @Embedded
    @JsonUnwrapped
    private TimestampEmbeddable timestamp;
}
