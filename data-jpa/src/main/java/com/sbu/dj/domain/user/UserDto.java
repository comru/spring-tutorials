package com.sbu.dj.domain.user;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@JsonTypeName("user")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public record UserDto(String email,
                      String token,
                      String username,
                      String bio,
                      String image) {

    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public record LoginUser(String email,
                            String password) {
    }

    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public record NewUser(String email,
                          String username,
                          String password) {
    }

    @JsonTypeName("user")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public record UpdateUser(String email,
                             String username,
                             String password,
                             String bio,
                             String image) {
    }

    @Data
    @RequiredArgsConstructor
    @JsonTypeName("profile")
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
    public static class Profile {
        private final String username;
        private final String bio;
        private final String image;
        private boolean following;
    }
}

