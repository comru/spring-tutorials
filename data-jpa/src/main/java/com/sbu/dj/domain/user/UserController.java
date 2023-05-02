package com.sbu.dj.domain.user;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @ResponseStatus(CREATED)
    @PostMapping("/users")
    public UserDto signUp(@Valid @RequestBody UserDto.NewUser newUser) {
        User user = userService.signUp(newUser);
        return userMapper.toUserDto(user);
    }

    @PostMapping("/users/login")
    public UserDto login(@RequestBody UserDto.LoginUser loginUser) {
        return userService.login(loginUser);
    }

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @GetMapping("/user")
    public ResponseEntity<UserDto> getCurrentUser() {
        User user = userService.getCurrentUserNN();
        UserDto userDto = userMapper.toUserDto(user);
        return ResponseEntity.ok(userDto);
    }

    @SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
    @PutMapping("/user")
    public UserDto updateCurrentUser(@RequestBody UserDto.UpdateUser request) {
        return userService.updateCurrentUser(request);
    }
}
