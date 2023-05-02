package com.sbu.dj.domain.user;

import com.sbu.dj.security.BearerTokenProvider;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    public User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new BadCredentialsException("AnonymousAuthenticationToken");
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
        User currentUser = getCurrentUser();
        User updatedUser = userMapper.partialUpdate(request, currentUser);
        return userMapper.toUserDto(updatedUser);
    }
}
