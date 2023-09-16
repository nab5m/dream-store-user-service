package com.junyounggoat.dreamstore.userservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JsonIgnoreProperties(value = {
        "userId",
        "userLoginCategoryId",
        "userLoginCredentialsId",
        "userNonmemberFlag",
        "user",
        "userLoginCategory",
        "creationDateTime",
        "lastUpdateDateTime",
        "deletionDateTime"
})
public @interface RequestDTOBlackList {
}
