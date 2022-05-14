package com.example.presentation.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.common.SecurityUtil;
import com.example.domain.login.LoginUser;
import com.example.domain.user.UserInfo;
import com.example.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping(value = "/api/user")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserResponseDto> getMyUserInfo() {

        LoginUser user = SecurityUtil.getLoginUser();

        UserInfo userInfo = userService.getUserByUsername(user.getUsername());

        return ResponseEntity.ok(UserResponseDto.of(userInfo));
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> getUserInfo(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.noContent().build();
    }
}
