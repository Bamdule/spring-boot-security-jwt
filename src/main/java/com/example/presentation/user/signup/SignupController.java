package com.example.presentation.user.signup;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.domain.user.UserInfo;
import com.example.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping(value = "/api/signup")
@RestController
public class SignupController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

        UserInfo userInfo = userService.signup(
            signupRequestDto.getUsername(),
            signupRequestDto.getNickname(),
            signupRequestDto.getPassword()
        );

        return ResponseEntity.ok(SignupResponseDto.to(userInfo));
    }
}
