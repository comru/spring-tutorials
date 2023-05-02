package com.sbu.dj.domain.user;

import com.sbu.dj.domain.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "user_")
public class User extends BaseEntity {

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @NotNull
    @Column(name = "password_hash", nullable = false)
    private  String passwordHash;

    @Column(name = "bio", length = Integer.MAX_VALUE)
    private String bio;

    @Column(name = "image", length = Integer.MAX_VALUE)
    private String image;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "users_follow",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id"))
    private Set<User> followings = new HashSet<>();

    @ManyToMany(mappedBy = "followings", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    private final Set<User> followers = new HashSet<>();

    @Transient
    private String token;
}