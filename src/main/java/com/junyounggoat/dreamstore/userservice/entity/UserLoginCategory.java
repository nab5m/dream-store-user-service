package com.junyounggoat.dreamstore.userservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;


@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserLoginCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long userLoginCategoryId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false)
    @NotNull
    private int userLoginCategoryCode;

    private CreationDateTime creationDateTime;

    private DeletionDateTime deletionDateTime;

    public @Nullable LocalDateTime getCreationDateTime() {
        if (creationDateTime == null) {
            return null;
        }

        return creationDateTime.getCreationDateTime();
    }

    public @Nullable LocalDateTime getDeletionDateTime() {
        if (deletionDateTime == null) {
            return null;
        }

        return deletionDateTime.getDeletionDateTime();
    }
}
