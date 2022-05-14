package com.example.presentation.user.login;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.configuration.jwt.JwtFilter;
import com.example.domain.login.LoginService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
        @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {

        String jwt = loginService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());

        return new ResponseEntity<>(
            new LoginResponseDto(jwt),
            this.createBearerJwtHeader(jwt),
            HttpStatus.OK
        );
    }

    private HttpHeaders createBearerJwtHeader(String jwt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, JwtFilter.JWT_PREFIX + jwt);
        return httpHeaders;
    }
}
