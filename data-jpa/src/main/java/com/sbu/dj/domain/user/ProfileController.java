package com.sbu.dj.domain.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService userService;

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/{username}")
    public UserDto.Profile getProfile(@PathVariable("username") String username) {
        return userService.getProfile(username);
    }

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PostMapping("/{username}/follow")
    public UserDto.Profile follow(@PathVariable("username") String username) {
        return userService.follow(username);
    }

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @DeleteMapping("/{username}/follow")
    public UserDto.Profile unfollow(@PathVariable("username") String username) {
        return userService.unfollow(username);
    }
}

