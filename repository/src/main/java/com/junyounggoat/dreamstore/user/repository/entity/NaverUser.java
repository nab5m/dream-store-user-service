package com.junyounggoat.dreamstore.user.repository.entity;

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
public class NaverUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Min(value = 0)
    private long naverUserId;

    @ManyToOne
    @JoinColumn(name = "userLoginCategoryId", nullable = false)
    private UserLoginCategory userLoginCategory;
}
