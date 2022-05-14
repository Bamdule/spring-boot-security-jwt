package com.example.domain.login;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;

import com.example.configuration.jwt.UserTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class LoginService {
    private final UserTokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public String login(String username, String password) {

        LoginUser loginUser = this.authenticate(username, password);

        return tokenProvider.createToken(loginUser);

    }

    /**
     * 아이디와 비밀번호가 일치하는지 검증한다.
     * @param username
     * @param password
     * @return
     */
    private LoginUser authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);

        return (LoginUser)authenticationManagerBuilder.getObject().authenticate(authenticationToken)
            .getPrincipal();
    }
}
