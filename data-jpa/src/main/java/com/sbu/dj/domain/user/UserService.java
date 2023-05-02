package com.sbu.dj.domain.user;

import com.sbu.dj.security.BearerTokenProvider;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BearerTokenProvider bearerTokenProvider;
    private final UserMapper userMapper;

    @Transactional
    public User signUp(UserDto.NewUser request) {
        this.validateUsernameAndEmail(request);
        User newUser = userMapper.toUser(request);
        newUser.setPasswordHash(passwordEncoder.encode(request.password()));
        return userRepository.save(newUser);
    }

    private void validateUsernameAndEmail(UserDto.NewUser request) {
        String username = request.username();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username(`%s`) already exists.".formatted(username));
        }

        String email = request.email();
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email(`%s`) already exists.".formatted(email));
        }
    }

    @Transactional(readOnly = true)
    public UserDto login(UserDto.LoginUser request) {
        return userRepository
                .findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPasswordHash()))
                .map(user -> {
                    String token = bearerTokenProvider.provide(user);
                    user.setToken(token);
                    return userMapper.toUserDto(user);
                })
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password."));
    }

    @Nonnull
    public User getCurrentUserNN() {
        User currentUser = getCurrentUser();
        if (currentUser == null) {
            throw new BadCredentialsException("AnonymousAuthenticationToken");
        }
        return currentUser;
    }

    @Nullable
    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }

        JwtAuthenticationToken jwt = (JwtAuthenticationToken) authentication;
        String userId = jwt.getName();
        String token = jwt.getToken().getTokenValue();

        return userRepository
                .findById(Long.parseLong(userId))
                .map(it -> {
                    it.setToken(token);
                    return it;
                })
                .orElseThrow(() -> new BadCredentialsException("Invalid token"));
    }

    @Transactional
    public UserDto updateCurrentUser(UserDto.UpdateUser request) {
        User currentUser = getCurrentUserNN();
        User updatedUser = userMapper.partialUpdate(request, currentUser);
        return userMapper.toUserDto(updatedUser);
    }

    @Transactional(readOnly = true)
    public UserDto.Profile getProfile(String username) {
        return userRepository
                .findByUsername(username)
                .map(it -> {
                    UserDto.Profile profile = userMapper.toDto(it);
                    User currentUser = getCurrentUser();
                    profile.setFollowing(currentUser != null && currentUser.getFollowings().contains(it));
                    return profile;
                })
                .orElseThrow(() -> new NoSuchElementException("User(`%s`) not found".formatted(username)));
    }

    @Transactional
    public UserDto.Profile follow(String username) {
        return userRepository
                .findByUsername(username)
                .map(it -> {
                    User currentUser = getCurrentUserNN();
                    Set<User> followings = currentUser.getFollowings();
                    followings.add(it);

                    UserDto.Profile profile = userMapper.toDto(it);
                    profile.setFollowing(true);
                    return profile;
                })
                .orElseThrow(() -> new NoSuchElementException("User(`%s`) not found".formatted(username)));
    }

    @Transactional
    public UserDto.Profile unfollow(String username) {
        return userRepository
                .findByUsername(username)
                .map(it -> {
                    User currentUser = getCurrentUserNN();
                    Set<User> followings = currentUser.getFollowings();
                    followings.remove(it);

                    UserDto.Profile profile = userMapper.toDto(it);
                    profile.setFollowing(false);
                    return profile;
                })
                .orElseThrow(() -> new NoSuchElementException("User(`%s`) not found".formatted(username)));
    }
}
